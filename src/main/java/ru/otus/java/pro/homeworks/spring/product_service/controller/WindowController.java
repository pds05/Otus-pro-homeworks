package ru.otus.java.pro.homeworks.spring.product_service.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.pro.homeworks.spring.product_service.dto.ProductDto;
import ru.otus.java.pro.homeworks.spring.product_service.entity.Product;
import ru.otus.java.pro.homeworks.spring.product_service.entity.ProductDetails;
import ru.otus.java.pro.homeworks.spring.product_service.exception.ResourceNotFoundException;
import ru.otus.java.pro.homeworks.spring.product_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class WindowController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public WindowController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getProducts().stream().map(this::convertToDto).toList();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable long id) {
        return productService.getProduct(id).map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        Product product = productService.save(convertToEntity(productDto));
        return convertToDto(product);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        Product product = productService.update(convertToEntity(productDto));
        return productDto.getId() != null ?
                ResponseEntity.ok(convertToDto(product)) :
                new ResponseEntity<>(convertToDto(product), HttpStatus.CREATED);

    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete")
    public void deleteProduct(@RequestParam long id) {
        productService.deleteProduct(id);
    }

    public Product convertToEntity(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        if (product.getDetails() != null && productDto.getDeliveryDate() != null) {
            product.getDetails().setDeliveryDate(productDto.getDeliveryDate());
        } else if (productDto.getId() == null) {
            ProductDetails details = new ProductDetails();
            details.setDeliveryDate(productDto.getDeliveryDate());
            product.setDetails(details);
        }
        if (productDto.getCategoryTitle() != null) {
            product.setCategoryId(productService.getProductCategory(productDto.getCategoryTitle()).orElseThrow(() -> new ResourceNotFoundException("Category " + productDto.getCategoryTitle() + " not found")).getId());
        }
        return product;
    }

    public ProductDto convertToDto(Product product) {
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        dto.setDeliveryDate(product.getDetails().getDeliveryDate());
        return dto;
    }
}
