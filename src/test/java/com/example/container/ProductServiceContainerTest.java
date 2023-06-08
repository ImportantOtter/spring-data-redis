package com.example.container;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceContainerTest {

    static {
        GenericContainer<?> redis =
                new GenericContainer<>(DockerImageName.parse("bitnami/redis"))
                        .withExposedPorts(6379)
                        .withEnv("ALLOW_EMPTY_PASSWORD", "yes");
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }

    @Mock
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    private ProductService productService;

    public ProductServiceContainerTest() {
        MockitoAnnotations.initMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    void testSaveProduct() {
        Product product = new Product(11, "Phone", 1, 1500L);

        when(productRepository.saveProduct(product)).thenReturn(product);

        Product savedProduct = productService.save(product);

        assertEquals(product, savedProduct);

        verify(productRepository, times(1)).saveProduct(productArgumentCaptor.capture());
        assertEquals(product, productArgumentCaptor.getValue());
    }

    @Test
    void testDeleteProduct() {
        int productId = 1;
        String productRemoved = "product removed";
        Product product = new Product(productId, "Phone", 1, 1500L);

        when(productRepository.deleteProduct(productId)).thenReturn(productRemoved);

        String deleteProduct = productService.deleteProduct(product.getId());

        assertEquals(productRemoved, deleteProduct);

        verify(productRepository, times(1)).deleteProduct(productId);
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Phone", 1, 1500L));
        productList.add(new Product(2, "Book", 1, 50L));

        when(productRepository.getAllProducts()).thenReturn(productList);

        List<Product> retrievedProducts = productService.getAllProducts();

        assertEquals(productList, retrievedProducts);

        verify(productRepository, times(1)).getAllProducts();
    }

    @Test
    void testFindProductById() {
        int productId = 12;
        Product product = new Product(productId, "Phone", 1, 1500L);

        when(productRepository.getProductById(productId)).thenReturn(product);

        Product foundProduct = productService.findProductById(productId);

        assertEquals(product, foundProduct);

        verify(productRepository, times(1)).getProductById(productId);
    }
}
