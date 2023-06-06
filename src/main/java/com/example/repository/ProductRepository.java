package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class ProductRepository {
    public static final String HASH_KEY = "products";
    private static final long EXPIRATION_TIMEOUT = 5;

    private RedisTemplate redisTemplate;

    public ProductRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Product save(Product product) {
        redisTemplate.opsForHash().put(HASH_KEY, product.getId(), product);
        redisTemplate.expire(HASH_KEY, EXPIRATION_TIMEOUT, TimeUnit.SECONDS);

        return product;
    }

    public List<Product> findAll() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public Product findProductById(int id) {
        return (Product) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public String deleteProduct(int id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
        return "product removed";
    }
}
