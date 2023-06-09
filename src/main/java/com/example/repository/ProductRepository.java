package com.example.repository;

import com.example.config.RedisExpirationListener;
import com.example.model.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;

@Repository
public class ProductRepository {

    public static final String HASH_KEY = "products";
    private static final long EXPIRATION_TIMEOUT = 5;

    private RedisTemplate redisTemplate;
    private RedisExpirationListener expirationListener;

    public ProductRepository(RedisTemplate redisTemplate, RedisExpirationListener expirationListener) {
        this.redisTemplate = redisTemplate;
        this.expirationListener = expirationListener;
    }

    public Product saveProduct(Product product) {
        Jedis jedis = new Jedis("localhost");
        jedis.hset(HASH_KEY, String.valueOf(product.getId()), product.toString());

        productInfo(product, jedis);

        jedis.expire(HASH_KEY, EXPIRATION_TIMEOUT);

        jedis.close();
        return product;
    }

    public List<Product> getAllProducts() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public Product getProductById(int id) {
        return (Product) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public String deleteProduct(int id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
        return "product removed";
    }

    private static void productInfo(Product product, Jedis jedis) {
        String productInfo = jedis.hget(HASH_KEY, String.valueOf(product.getId()));
        System.out.println("Product info before expire: " + productInfo);
    }
}
