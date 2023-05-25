package com.realty.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getRegistrationFormTest() throws Exception {
		this.mockMvc.perform(get("/registration")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("registration")).andExpect(content().string(containsString("Регистрация")));
	}

	@Test
	public void badRegistrationEmailTest() throws Exception {
		this.mockMvc
				.perform(multipart("/registration").param("username", "client1").param("password", "client")
						.param("name", "client").param("surname", "client").param("lastname", "client")
						.param("phone", "80293344555").param("email", "client@bsuir.com").with(csrf()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Пользователь с данным email уже существует!")))
				.andExpect(view().name("registration"));
	}
	
	@Test
	public void badRegistrationUsernameTest() throws Exception {
		this.mockMvc
				.perform(multipart("/registration").param("username", "client").param("password", "client")
						.param("name", "client").param("surname", "client").param("lastname", "client")
						.param("phone", "80293344555").param("email", "client1@bsuir.com").with(csrf()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Логин уже существует!")))
				.andExpect(view().name("registration"));
	}

}
