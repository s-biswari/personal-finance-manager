package com.self.finance.controller;

import com.self.finance.model.Category;
import com.self.finance.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // Deprecated but required for test slice mocking in Spring Boot 3.4.x
    private CategoryService categoryService;

    @Test
    void testGetAll() throws Exception {
        Category cat = Category.builder().id(1L).name("TestCat").build();
        Mockito.when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(cat));
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("TestCat"));
    }

    @Test
    void testGetById() throws Exception {
        Category cat = Category.builder().id(1L).name("TestCat").build();
        Mockito.when(categoryService.getCategoryById(1L)).thenReturn(cat);
        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCat"));
    }

    @Test
    void testCreate() throws Exception {
        Category cat = Category.builder().id(1L).name("TestCat").build();
        Mockito.when(categoryService.createCategory(any())).thenReturn(cat);
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"TestCat\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCat"));
    }

    @Test
    void testUpdate() throws Exception {
        Category cat = Category.builder().id(1L).name("UpdatedCat").build();
        Mockito.when(categoryService.updateCategory(any(Long.class), any(Category.class))).thenReturn(cat);
        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"UpdatedCat\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedCat"));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk());
    }
}
