package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void saveProduct() throws Exception {
        Product product = new Product(1, "Phone", 1, 1000L);

        when(productService.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"Phone\", \"qty\": 1, \"price\": 1000}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"name\": \"Phone\", \"qty\": 1, \"price\": 1000}"))
                .andDo(print());

        verify(productService, times(1)).save(any(Product.class));
    }

    @Test
    public void removeTest() throws Exception {
        Product product = new Product(1, "Phone", 1, 1000L);
        String productDeleted = "Product deleted";

        when(productService.deleteProduct(product.getId())).thenReturn(productDeleted);

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(productDeleted))
                .andDo(print());

        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    public void getAllProducts() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1, "Phone", 1, 1000L),
                new Product(2, "Book", 1, 30L)
        );

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"id\": 1, \"name\": \"Phone\", \"qty\": 1, \"price\": 1000}," +
                        "{\"id\": 2, \"name\": \"Book\", \"qty\": 1, \"price\": 30}" +
                        "]"))
                .andDo(print());

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void findProduct() throws Exception {
        Product product = new Product(1, "Phone", 1, 1000L);

        when(productService.findProductById(1)).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"name\": \"Phone\", \"qty\": 1, \"price\": 1000}"))
                .andDo(print());

        verify(productService, times(1)).findProductById(1);
    }
}
