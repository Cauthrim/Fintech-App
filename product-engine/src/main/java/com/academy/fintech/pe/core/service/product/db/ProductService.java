package com.academy.fintech.pe.core.service.product.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product findByCode(String code) {
        return productRepository.findByCode(code);
    }
}
