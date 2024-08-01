package com.example.Sparta.service;

import com.example.Sparta.dto.request.*;
import com.example.Sparta.dto.response.*;
import com.example.Sparta.entity.*;
import com.example.Sparta.repository.*;
import com.example.Sparta.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    @Autowired
    private final FTPService ftpService;

    @Autowired
    public ProductsService(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, FTPService ftpService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.ftpService = ftpService;
    }

    /*_____________________제품_______________________*/

    /* 제품 목록 불러오기 : 페이지 */
    public ResponseEntity<Page<ProductResponseDTO>> findProducts(int page, String option, boolean desc) {
        Sort sort = option.isEmpty() ? Sort.unsorted() : Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, option);
        Pageable pageable = PageRequest.of(page, 12, sort);
        Page<Product> productsPage = productRepository.findAll(pageable);
        // 리스트에서 이미지 찾아서 저장
        List<ProductResponseDTO> productDTOs = productsPage.getContent().stream()
                .map(product -> {
                    try {
                        String imgStr = ftpService.downloadImage("/product/" + product.getId());
                        return new ProductResponseDTO(product,imgStr);
                    } catch (IOException e) {
                        return new ProductResponseDTO(product, null);
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PageImpl<>(productDTOs, pageable, productsPage.getTotalElements()));
    }

    /* 제품 정보 불러오기 */
    public ResponseEntity<ProductResponseDTO> findProduct(int id) throws IOException {// DB 조회
            Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다!"));
            /* 이미지 String 받아오기 */
            String imgStr = ftpService.downloadImage("/product/" + String.valueOf(id));
            return ResponseEntity.ok(new ProductResponseDTO(product,imgStr));
    }

    /* 제품 추가 */
    @Transactional
    public ResponseEntity<String> createProduct(ProductRequestDTO productRequestDTO){
        Product product = productRepository.save(new Product(productRequestDTO));
        return ResponseEntity.ok("제품 정보 생성 완료");
    }

    /* 제품 이미지 업로드 */
    @Transactional
    public ResponseEntity<String> uploadProductImage(String filename,MultipartFile file ) throws IOException {
        boolean uploaded = ftpService.uploadImageToFtp(filename, file);
        if (uploaded) {
            return ResponseEntity.ok("이미지 업로드 성공");
        } else {
            return ResponseEntity.ok("이미지 업로드 실패");
        }
    }

    /* 제품 삭제 */
    @Transactional
    public ResponseEntity<String> removeProduct(int id){
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다!"));
        cartRepository.deleteByProductId(product.getId());
        productRepository.delete(product);
        return ResponseEntity.ok("제품 정보 삭제 완료");
    }

    /*_____________________장바구니_______________________*/

    /* 장바구니 정보 불러오기 */
    public ResponseEntity<List<CartResponseDTO>> findCart(int id) {// DB 조회
        List<CartResponseDTO> cart = cartRepository.findByUserId(id).stream().map(CartResponseDTO::new).toList();
        return ResponseEntity.ok(cart);
    }

    /* 장바구니 추가 */
    @Transactional
    public ResponseEntity<String> createCart(CartCreateRequestDTO cartCreateRequestDTO){
            Product product = productRepository.findById(cartCreateRequestDTO.getProduct_id()).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다."));
            User user = userRepository.findById(cartCreateRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            // 제품 재고량 초과
            if(cartCreateRequestDTO.getCount() > product.getCount()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("제품 재고량을 초과했습니다.");
            }
            // 이미 장바구니에 제품이 있는 경우
            if(cartRepository.findByUserIdAndProductId(cartCreateRequestDTO.getUser_id(),cartCreateRequestDTO.getProduct_id()).isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 제품이 장바구니에 있습니다.");
            }
            Cart cart = new Cart(user,product,cartCreateRequestDTO);
            cartRepository.save(cart);
            return ResponseEntity.ok("장바구니 추가 완료");
    }

    /* 장바구니 삭제 */
    @Transactional
    public ResponseEntity<String> removeCart(int id,UserDetailsImpl userDetails){
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다!"));
        if(userDetails.getUser().getId() != cart.getUser().getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 장바구니 항목이 아닙니다.");
        }
        cartRepository.delete(cart);
        return ResponseEntity.ok("제품 정보 삭제 완료");
    }

    /* 장바구니 수정 */
    @Transactional
    public ResponseEntity<String> updateCart(CartUpdateRequestDTO cartUpdateRequestDTO,UserDetailsImpl userDetails) {
        Cart cart = cartRepository.findById(cartUpdateRequestDTO.getId()).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다!"));
        if(userDetails.getUser().getId() != cart.getUser().getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 장바구니 항목이 아닙니다.");
        }
        if(cart.getProduct().getCount() < cartUpdateRequestDTO.getCount()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("최대 제품 수량입니다.");
        }
        cart.update(cartUpdateRequestDTO);
        return ResponseEntity.ok("수량 변경 완료");
    }
}
