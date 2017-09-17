package com.olegshan.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ErrorHandlerTest {

    @Mock
    private ParseController parseController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(parseController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void unexpectedExceptionsAreCaught() throws Exception {

        when(parseController.about()).thenThrow(new RuntimeException("Unexpected exception"));

        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("exception"))
                .andExpect(model().attribute("errorMessage", "Unexpected exception"));
    }
}