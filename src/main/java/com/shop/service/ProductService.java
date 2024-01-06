package com.shop.service;

import com.shop.model.Product;
import com.shop.request.CreateproductRequest;
import org.example.common.exception.ProductException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateproductRequest req);
    public String deleteProduct(Long id) throws ProductException;
    public Product updateProduct(Long id, Product product) throws ProductException;
    public Product findProductById(Long id) throws ProductException;
    public List<Product> findProductByCategory(String category);
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice , Integer maxPrice,
            Integer minDiscount, String sort ,String stock, Integer pageNumber , Integer pageSize );


}
