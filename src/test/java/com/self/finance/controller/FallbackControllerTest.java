package com.self.finance.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FallbackController.class)
class FallbackControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFallbackEndpoint() throws Exception {
        mockMvc.perform(get("/some-nonexistent-path"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/index.html"));
    }
}
