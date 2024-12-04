package ru.otus.java.pro.homeworks.spring.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.pro.homeworks.spring.product_service.dto.ProductDto;
import ru.otus.java.pro.homeworks.spring.product_service.entity.Product;
import ru.otus.java.pro.homeworks.spring.product_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class WindowController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {

        return productService.findAll().stream().map(product -> ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice()).build()).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable long id) {
        return productService.findById(id)
                .map(product -> new ResponseEntity<>(
                        ProductDto.builder()
                                .id(product.getId())
                                .title(product.getTitle())
                                .price(product.getPrice())
                                .build(), HttpStatus.OK)
                ).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.save(
                Product.builder()
                        .title(productDto.getTitle())
                        .price(productDto.getPrice())
                        .build());
        productDto.setId(product.getId());
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        Product updatingProduct = Product.builder()
                .id(productDto.getId())
                .title(productDto.getTitle())
                .price(productDto.getPrice()).build();
        productService.update(updatingProduct);
        return productDto;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteProduct(@RequestParam long id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
