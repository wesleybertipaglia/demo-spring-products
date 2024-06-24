package com.wesleybertipaglia.products.services;

import com.wesleybertipaglia.products.dtos.ProductRecordDto;
import com.wesleybertipaglia.products.models.ProductModel;
import com.wesleybertipaglia.products.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductModel saveProduct(ProductRecordDto productRecordDto) {
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public List<ProductModel> getAllProducts(String filter, int page, int size) {
        if (filter != null && !filter.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(filter);
        } else {
            return productRepository.findAll(PageRequest.of(page, size)).getContent();
        }
    }

    public Optional<ProductModel> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public ProductModel updateProduct(UUID id, ProductRecordDto productRecordDto) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            ProductModel productModel = product.get();
            BeanUtils.copyProperties(productRecordDto, productModel);
            return productRepository.save(productModel);
        }
        return null;
    }

    public boolean deleteProduct(UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        }
        return false;
    }
}
