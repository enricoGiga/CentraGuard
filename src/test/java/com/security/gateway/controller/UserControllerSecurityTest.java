package com.security.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.gateway.dto.ApplicationUserDTO;
import com.security.gateway.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestPropertySource("classpath:application-test.yml")
public class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void getUserWithId() throws Exception {
        ApplicationUserDTO userDTO = new ApplicationUserDTO();
        userDTO.setLastName("Enrico");
        when(userService.findUserByEmail("marco.gianni@gmail.com")).thenReturn(Optional.of(userDTO));
        mockMvc.perform(get("/api/marco.gianni@gmail.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(new ApplicationUserDTO(){{
                    setLastName("Enrico");
                }})))
                .andDo(print());

    }

    @Test
    public void getUserWithId_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/marco.gianni@gmail.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
