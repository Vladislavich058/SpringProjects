package com.realty.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin")
public class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void adminAccountPageTest() throws Exception {
		this.mockMvc.perform(get("/admin")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("admin"))
				.andExpect(content().string(containsString("Личный аккаунт")));
	}

	@Test
	public void realtorsTest() throws Exception {
		this.mockMvc.perform(get("/admin/realtors")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("realtors"))
				.andExpect(xpath("//*[@id=\"users\"]/tbody/tr/td[1]").string("realtor"));
	}

	@Test
	public void findRealtorsTest() throws Exception {
		this.mockMvc.perform(get("/admin/realtors").param("username", "realtor")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("realtors"))
				.andExpect(xpath("//*[@id=\"users\"]/tbody/tr/td[1]").string("realtor"));
	}

	@Test
	public void clientsTest() throws Exception {
		this.mockMvc.perform(get("/admin/clients")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("clients"))
				.andExpect(xpath("//*[@id=\"users\"]/tbody/tr/td[1]").string("client"));
	}

	@Test
	public void findClientsTest() throws Exception {
		this.mockMvc.perform(get("/admin/clients").param("username", "client")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("clients"))
				.andExpect(xpath("//*[@id=\"users\"]/tbody/tr/td[1]").string("client"));
	}

	@Test
	public void advertisementsTest() throws Exception {
		this.mockMvc.perform(get("/admin/advertisements")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("admin.advertisements"));
	}

	@Test
	public void getAdvertisementTest() throws Exception {
		this.mockMvc.perform(get("/getAdvertisement").param("name", "1")).andExpect(status().is4xxClientError());
	}

	@Test
	public void filterAdvertisementTest() throws Exception {
		this.mockMvc.perform(get("/admin/filterAdvertisements").param("priceFrom", "5").param("priceTo", "4"))
				.andDo(print()).andExpect(status().isOk()).andExpect(view().name("admin.advertisements"))
				.andExpect(content().string(containsString("Неверный интервал")));
	}

	@Test
	public void sortAdvertisementTest() throws Exception {
		this.mockMvc.perform(get("/admin/sortAdvertisements").param("sort", "1")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("admin.advertisements"));
	}

	@Test
	public void findAdvertisementTest() throws Exception {
		this.mockMvc.perform(get("/admin/findAdvertisements").param("find", "adv")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("admin.advertisements"));
	}

	@Test
	public void statisticsTest() throws Exception {
		this.mockMvc.perform(get("/admin/statistics")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("statistics"));
	}

}
