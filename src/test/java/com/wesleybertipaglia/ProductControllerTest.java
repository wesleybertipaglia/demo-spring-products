package com.wesleybertipaglia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.wesleybertipaglia.products.controllers.ProductController;
import com.wesleybertipaglia.products.dtos.ProductRecordDto;
import com.wesleybertipaglia.products.dtos.ProductResponseDto;
import com.wesleybertipaglia.products.models.ProductModel;
import com.wesleybertipaglia.products.services.ProductService;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductModel productModel;
    private ProductRecordDto productRecordDto;
    private ProductResponseDto productResponseDto;
    private Validator validator;

    @BeforeEach
    void setUp() {
        productModel = new ProductModel();
        productModel.setId(UUID.randomUUID());
        productModel.setName("Test Product");
        productModel.setPrice(100.0);

        productRecordDto = new ProductRecordDto("Test Product", 100.0);

        productResponseDto = new ProductResponseDto();
        productResponseDto.setId(productModel.getId());
        productResponseDto.setName("Test Product");
        productResponseDto.setPrice(100.0);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSaveProduct() {
        when(productService.saveProduct(any(ProductRecordDto.class))).thenReturn(productModel);

        ResponseEntity<ProductResponseDto> response = productController.saveProduct(productRecordDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productResponseDto.getName(), response.getBody().getName());
    }
}
