package com.example.Sparta.service;

import com.example.Sparta.dto.request.*;
import com.example.Sparta.dto.response.*;
import com.example.Sparta.entity.*;
import com.example.Sparta.enums.LectureCategory;
import com.example.Sparta.repository.*;
import com.example.Sparta.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Autowired
    public GoodsService(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    /*_____________________제품_______________________*/

    /* 제품 목록 불러오기 : 페이지 */
    public ResponseDTO findProducts(int page, String option, boolean desc) {
        Sort sort = option.isEmpty() ? Sort.unsorted() : Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, option);
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<ProductResponseDTO> products = productRepository.findAll(pageable).map(ProductResponseDTO::new);
        return new ResponseDTO(HttpStatus.OK.value(), "제품 목록 검색 완료", products);
    }

    /* 제품 정보 불러오기 */
    public ResponseDTO findProduct(int id) {// DB 조회
        try{
            Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다!"));
            return new ResponseDTO(HttpStatus.OK.value(), "제품 정보 검색 완료", new ProductResponseDTO(product));
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 제품 추가 */
    @Transactional
    public ResponseDTO createProduct(ProductRequestDTO productRequestDTO){
        try {
            Product product = new Product(productRequestDTO);
            productRepository.save(product);
            return new ResponseDTO(HttpStatus.OK.value(), "제품 정보 생성 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 제품 삭제 */
    @Transactional
    public ResponseDTO removeProduct(int id){
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다!"));
            productRepository.delete(product);
            return new ResponseDTO(HttpStatus.OK.value(), "제품 정보 삭제 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /*_____________________장바구니_______________________*/

    /* 장바구니 정보 불러오기 */
    public ResponseDTO findCart(int id) {// DB 조회
        List<CartResponseDTO> cart = cartRepository.findByUserId(id).stream().map(CartResponseDTO::new).toList();
        return new ResponseDTO(HttpStatus.OK.value(), "장바구니 검색 완료", cart);
    }

    /* 장바구니 추가 */
    @Transactional
    public ResponseDTO createCart(CartCreateRequestDTO cartCreateRequestDTO){
        try {
            Product product = productRepository.findById(cartCreateRequestDTO.getProduct_id()).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다."));
            User user = userRepository.findById(cartCreateRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            // 제품 재고량 초과
            if(cartCreateRequestDTO.getCount() > product.getCount()){
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "제품 재고량을 초과했습니다.", null);
            }
            // 이미 장바구니에 제품이 있는 경우
            if(cartRepository.findByUserIdAndProductId(cartCreateRequestDTO.getUser_id(),cartCreateRequestDTO.getProduct_id()).isPresent()){
                return new ResponseDTO(HttpStatus.MULTIPLE_CHOICES.value(), "이미 제품이 장바구니에 있습니다.", null);
            }
            Cart cart = new Cart(user,product,cartCreateRequestDTO);
            cartRepository.save(cart);
            return new ResponseDTO(HttpStatus.OK.value(), "장바구니 추가 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 장바구니 삭제 */
    @Transactional
    public ResponseDTO removeCart(int id,UserDetailsImpl userDetails){
        try {
            Cart cart = cartRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다!"));
            if(userDetails.getUser().getId() != cart.getUser().getId()){
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "사용자의 장바구니 항목이 아닙니다.", null);
            }
            cartRepository.delete(cart);
            return new ResponseDTO(HttpStatus.OK.value(), "제품 정보 삭제 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 장바구니 수정 */
    @Transactional
    public ResponseDTO updateCart(CartUpdateRequestDTO cartUpdateRequestDTO,UserDetailsImpl userDetails) {
        try {
            Cart cart = cartRepository.findById(cartUpdateRequestDTO.getId()).orElseThrow(() -> new IllegalArgumentException("제품을 찾을 수 없습니다!"));
            if(userDetails.getUser().getId() != cart.getUser().getId()){
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "사용자의 장바구니 항목이 아닙니다.", null);
            }
            if(cart.getProduct().getCount() < cartUpdateRequestDTO.getCount()){
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "최대 제품 수량입니다.", null);
            }
            cart.update(cartUpdateRequestDTO);
            return new ResponseDTO(HttpStatus.OK.value(), "수량 변경 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }


}
