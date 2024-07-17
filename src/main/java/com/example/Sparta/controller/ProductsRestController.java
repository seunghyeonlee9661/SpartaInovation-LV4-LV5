package com.example.Sparta.controller;

import com.example.Sparta.dto.request.*;
import com.example.Sparta.dto.response.ResponseDTO;
import com.example.Sparta.enums.UserAuthority;
import com.example.Sparta.security.UserDetailsImpl;
import com.example.Sparta.service.ProductsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductsRestController {

    private final ProductsService productsService;

    /*_____________________제품_______________________*/

    /* 제품 목록 불러오기 : page(페이지네이션),option(정렬옵션),desc(정렬방향) */
    @GetMapping("/products")
    public ResponseDTO findProducts(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="option", defaultValue="") String option, @RequestParam(value="desc", defaultValue="") boolean desc) {
        return productsService.findProducts(page,option,desc);
    }

    /* 제품 내용 불러오기 */
    @GetMapping("/product")
    public ResponseDTO findProduct(@RequestParam("id") int id) {
        return productsService.findProduct(id);
    }

    /* 제품 추가 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PostMapping("/product")
    public ResponseDTO createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        return productsService.createProduct(productRequestDTO);
    }

    /* 제품 삭제 */
    @Secured(UserAuthority.Authority.ADMIN)
    @DeleteMapping("/product")
    public ResponseDTO removeProduct(@RequestParam("id") int id) {
        return productsService.removeProduct(id);
    }

    /* 제품 이미지 추가 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PostMapping("/product/uploadImage")
    public ResponseDTO uploadProductImage(@RequestParam("filename") String filename, @RequestParam("file") MultipartFile file) {
        return productsService.uploadProductImage(filename,file);
    }



    /*_____________________장바구니_______________________*/

    /* 장바구니 내용 불러오기 */
    @GetMapping("/cart")
    public ResponseDTO findCart(@RequestParam("id") int id) {
        return productsService.findCart(id);
    }

    /* 장바구니 추가 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PostMapping("/cart")
    public ResponseDTO createCart(@RequestBody @Valid CartCreateRequestDTO cartCreateRequestDTO) {
        return productsService.createCart(cartCreateRequestDTO);
    }

    /* 장바구니 삭제 */
    @Secured(UserAuthority.Authority.ADMIN)
    @DeleteMapping("/cart")
    public ResponseDTO removeCart(@RequestParam("id") int id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productsService.removeCart(id,userDetails);
    }

    /* 장바구니 수정 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @PutMapping("/cart")
    public ResponseDTO updateCart(@Valid @RequestBody CartUpdateRequestDTO cartUpdateRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productsService.updateCart(cartUpdateRequestDTO,userDetails);
    }
}
