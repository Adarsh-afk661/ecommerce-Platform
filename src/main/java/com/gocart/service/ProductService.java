package com.gocart.service;

import com.gocart.entity.Product;
import com.gocart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByStoreId(String storeId) {
        return productRepository.findByStoreId(storeId);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product createProduct(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }
        if (product.getMrp() == null) {
            product.setMrp(0.0);
        }
        if (product.getImages() == null) {
            product.setImages(List.of());
        }
        if (product.getCategory() == null) {
            product.setCategory("");
        }
        if (product.getInStock() == null) {
            product.setInStock(true);
        }
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            if (productDetails.getName() != null) {
                product.setName(productDetails.getName());
            }
            if (productDetails.getDescription() != null) {
                product.setDescription(productDetails.getDescription());
            }
            if (productDetails.getPrice() != null) {
                product.setPrice(productDetails.getPrice());
            }
            if (productDetails.getMrp() != null) {
                product.setMrp(productDetails.getMrp());
            }
            if (productDetails.getImages() != null) {
                product.setImages(productDetails.getImages());
            }
            if (productDetails.getCategory() != null) {
                product.setCategory(productDetails.getCategory());
            }
            if (productDetails.getInStock() != null) {
                product.setInStock(productDetails.getInStock());
            }
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
