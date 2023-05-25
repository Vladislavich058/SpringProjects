package com.realty.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class LoginControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void loginPageLoadTest() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Вход")))
				.andExpect(content().string(containsString("Регистрация")));
	}
	
	@Test
	public void securityFilterChainTest() throws Exception {
		this.mockMvc.perform(get("/admin")).andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void loginSuccessHandlerAdminTest() throws Exception {
		this.mockMvc.perform(formLogin().user("admin").password("admin")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin"));
	}
	
	@Test
	public void loginSuccessHandlerClientTest() throws Exception {
		this.mockMvc.perform(formLogin().user("client").password("client")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/client"));
	}

	@Test
	public void loginSuccessHandlerRealtorTest() throws Exception {
		this.mockMvc.perform(formLogin().user("realtor").password("realtor")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/realtor"));
	}


	@Test
	public void badCreditalsTest() throws Exception {
		this.mockMvc.perform(post("/login").param("user", "gdjhfdf")).andDo(print()).andExpect(status().isForbidden());
	}
}
