package restaurant.controller;

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
public class LoginTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoad() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("La Sofia")))
				.andExpect(content().string(containsString("Main"))).andExpect(content().string(containsString("Menu")))
				.andExpect(content().string(containsString("Delivery")))
				.andExpect(content().string(containsString("Contacts")));
	}

	@Test
	public void loginTest() throws Exception {
		this.mockMvc.perform(get("/order")).andDo(print()).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	public void correctLoginAdminTest() throws Exception {
		this.mockMvc.perform(formLogin().user("admin").password("admin")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/account"));
	}

	@Test
	public void correctLoginClientTest() throws Exception {
		this.mockMvc.perform(formLogin().user("client1").password("client")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/client/account"));
	}

	@Test
	public void correctLoginManagerTest() throws Exception {
		this.mockMvc.perform(formLogin().user("manager").password("manager")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/manager/account"));
	}

	@Test
	public void correctLoginMessengerTest() throws Exception {
		this.mockMvc.perform(formLogin().user("messenger").password("messenger")).andDo(print())
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/messenger/account"));
	}

	@Test
	public void incorrectLoginTest() throws Exception {
		this.mockMvc.perform(post("/login").param("user", "Vvv")).andDo(print()).andExpect(status().isForbidden());
	}
}
