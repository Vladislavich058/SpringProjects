package restaurant.controller;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
	public void homePageTest() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(authenticated())
				.andExpect(xpath("/html/body/div/div[1]/div[1]/div[4]/a/span").string("admin"));
	}

	@Test
	public void accountTest() throws Exception {
		this.mockMvc.perform(get("/admin/account")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("adminAccount"));
	}

	@Test
	public void redactTest() throws Exception {
		this.mockMvc.perform(get("/admin/redact")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("adminAccount"));
	}

	@Test
	public void messengersListTest() throws Exception {
		this.mockMvc.perform(get("/admin/messengers")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("messengersList"));
	}

	@Test
	public void messengersFindListTest() throws Exception {
		this.mockMvc.perform(get("/admin/messengers/find").param("username", "messenger")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("messengersList"));
	}

	@Test
	public void clientsListTest() throws Exception {
		this.mockMvc.perform(get("/admin/clients")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("clientsList"));
	}

	@Test
	public void clientsFindListTest() throws Exception {
		this.mockMvc.perform(get("/admin/clients/find").param("username", "client1")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("clientsList"));
	}

	@Test
	public void managersListTest() throws Exception {
		this.mockMvc.perform(get("/admin/managers")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("managersList"));
	}

	@Test
	public void managersFindListTest() throws Exception {
		this.mockMvc.perform(get("/admin/managers/find").param("username", "manager")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("managersList"));
	}

}
