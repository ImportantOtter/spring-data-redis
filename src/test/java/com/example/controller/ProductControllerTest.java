package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;



@SpringBootTest
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void saveProduct() throws Exception {
        Product product = new Product(1, "Phone", 1, 1000L);
        String endpoint = "/product";

        when(productService.save(any(Product.class))).thenReturn(product);

        String requestJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(requestJson))
                .andDo(print());

        verify(productService, times(1)).save(any(Product.class));
    }

    @Test
    public void removeTest() throws Exception {
        Product product = new Product(1, "Phone", 1, 1000L);
        String productDeleted = "Product deleted";
        String endpoint = "/product/1";

        when(productService.deleteProduct(product.getId())).thenReturn(productDeleted);

        mockMvc.perform(MockMvcRequestBuilders.delete(endpoint))
                .andExpect(status().isOk())
                .andExpect(content().string(productDeleted))
                .andDo(print());

        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    public void getAllProducts() throws Exception {
        String endpoint = "/product";
        List<Product> products = Arrays.asList(
                new Product(1, "Phone", 1, 1000L),
                new Product(2, "Book", 1, 30L)
        );

        when(productService.getAllProducts()).thenReturn(products);

        String expectedJson = objectMapper.writeValueAsString(products);

        mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(print());

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void findProduct() throws Exception {
        String endpoint = "/product/1";
        Product product = new Product(1, "Phone", 1, 1000L);

        when(productService.findProductById(1)).thenReturn(product);

        String requestJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                .andExpect(status().isOk())
                .andExpect(content().json(requestJson))
                .andDo(print());

        verify(productService, times(1)).findProductById(1);
    }
}
