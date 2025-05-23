package com.self.finance.repository;

import com.self.finance.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindAll() {
        Category category = Category.builder().name("TestCategory").build();
        categoryRepository.save(category);
        List<Category> categories = categoryRepository.findAll();
        assertFalse(categories.isEmpty());
        assertTrue(categories.stream().anyMatch(c -> "TestCategory".equals(c.getName())));
    }
}
