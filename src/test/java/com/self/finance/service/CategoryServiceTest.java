package com.self.finance.service;

import com.self.finance.dto.CategoryDTO;
import com.self.finance.model.Category;
import com.self.finance.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = Category.builder().id(1L).name("Groceries").build();
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        List<Category> result = categoryService.getAllCategories();
        assertEquals(1, result.size());
        assertEquals("Groceries", result.get(0).getName());
    }

    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Category result = categoryService.getCategoryById(1L);
        assertEquals("Groceries", result.getName());
    }

    @Test
    void testCreateCategory() {
        CategoryDTO dto = new CategoryDTO("Utilities");
        Category newCategory = Category.builder().id(2L).name("Utilities").build();
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);
        Category result = categoryService.createCategory(dto);
        assertEquals("Utilities", result.getName());
    }

    @Test
    void testUpdateCategory() {
        Category updated = Category.builder().id(1L).name("Updated").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updated);
        Category result = categoryService.updateCategory(1L, updated);
        assertEquals("Updated", result.getName());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);
        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
