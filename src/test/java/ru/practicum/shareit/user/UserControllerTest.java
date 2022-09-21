package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.InputUserDto;
import ru.practicum.shareit.user.dto.OutputUserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    UserService userService;
    @MockBean
    UserMapper userMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    InputUserDto inputUser;
    User user;
    OutputUserDto outputUserDto;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "user", "user@mail.ru");
        inputUser = new InputUserDto("user", "user@mail.ru");
        outputUserDto = new OutputUserDto(1L, "user", "user@mail.ru");
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService, times(1))
                .getAllUsers();
    }

    @Test
    void addUser() throws Exception {
        when(userMapper.toUser(any(InputUserDto.class)))
                .thenReturn(user);
        when(userService.addUser(any(User.class)))
                .thenReturn(user);
        when(userMapper.toOutputUserDto(any(User.class)))
                .thenReturn(outputUserDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(inputUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputUserDto.getId()))
                .andExpect(jsonPath("$.name").value(outputUserDto.getName()))
                .andExpect(jsonPath("$.email").value(outputUserDto.getEmail()));

        verify(userService, times(1))
                .addUser(user);
    }


    @Test
    void updateUser() throws Exception {
        when(userMapper.toUser(any(InputUserDto.class)))
                .thenReturn(user);
        when(userService.updateUser(anyLong(), any(User.class)))
                .thenReturn(user);
        when(userMapper.toOutputUserDto(any(User.class)))
                .thenReturn(outputUserDto);

        mockMvc.perform(patch("/users/{id}", "1")
                        .content(mapper.writeValueAsString(inputUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputUserDto.getId()))
                .andExpect(jsonPath("$.name").value(outputUserDto.getName()))
                .andExpect(jsonPath("$.email").value(outputUserDto.getEmail()));

        verify(userService, times(1))
                .updateUser(1L, user);
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(anyLong()))
                .thenReturn(user);
        when(userMapper.toOutputUserDto(any(User.class)))
                .thenReturn(outputUserDto);
        mockMvc.perform(get("/users/{id}", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputUserDto.getId()))
                .andExpect(jsonPath("$.name").value(outputUserDto.getName()))
                .andExpect(jsonPath("$.email").value(outputUserDto.getEmail()));

        verify(userService, times(1))
                .getUser(anyLong());
    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/{id}", "1"))
                .andExpect(status().isOk());

        verify(userService, times(1))
                .deleteUser(anyLong());
    }
}