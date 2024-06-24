package com.wesleybertipaglia.products.controllers;

import com.wesleybertipaglia.products.dtos.ProductRecordDto;
import com.wesleybertipaglia.products.dtos.ProductResponseDto;
import com.wesleybertipaglia.products.models.ProductModel;
import com.wesleybertipaglia.products.services.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @PostMapping
    public ResponseEntity<ProductResponseDto> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        logger.debug("Request to save product: {}", productRecordDto);
        ProductModel productModel = productService.saveProduct(productRecordDto);
        ProductResponseDto responseDto = convertToDto(productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<ProductModel> productList = productService.getAllProducts(filter, page, size);
        List<ProductResponseDto> responseDtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        ProductResponseDto responseDto = convertToDto(product.get());
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
            @RequestBody @Valid ProductRecordDto productRecordDto) {
        ProductModel updatedProduct = productService.updateProduct(id, productRecordDto);
        if (updatedProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        ProductResponseDto responseDto = convertToDto(updatedProduct);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        return ResponseEntity.ok().body("Product deleted successfully.");
    }

    private ProductResponseDto convertToDto(ProductModel productModel) {
        ProductResponseDto responseDto = new ProductResponseDto();
        BeanUtils.copyProperties(productModel, responseDto);
        responseDto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductController.class)
                .getOneProduct(productModel.getId())).withSelfRel());
        return responseDto;
    }
}
