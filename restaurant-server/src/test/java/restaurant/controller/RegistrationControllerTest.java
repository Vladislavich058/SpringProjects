package restaurant.controller;

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
	public void testMessengerRegistrationForm() throws Exception {
		this.mockMvc.perform(get("/register/messenger")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("registrationMessenger"));
	}

	@Test
	public void testManagerRegistrationForm() throws Exception {
		this.mockMvc.perform(get("/register/manager")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("registrationManager"));
	}

	@Test
	public void testClientRegistrationForm() throws Exception {
		this.mockMvc.perform(get("/register/client")).andDo(print()).andExpect(status().isOk())
				.andExpect(view().name("registrationClient"));
	}

	@Test
	public void testProcessClientRegistration() throws Exception {
		this.mockMvc
				.perform(multipart("/register/client").param("username", "volodya").param("password", "volodya")
						.param("name", "volodya").param("surname", "kirov").param("lastname", "ivanovich")
						.param("phone", "80293456789").param("mail", "volodya@mail.ru").with(csrf()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("User with this username already exists!")));
	}

	@Test
	public void testProcessManagerRegistration() throws Exception {
		this.mockMvc
				.perform(multipart("/register/manager").param("username", "manager").param("password", "volodya")
						.param("name", "volodya").param("surname", "kirov").param("lastname", "ivanovich")
						.param("phone", "80293456789").param("workExperience", "2").param("passportNumber", "ММ4567888")
						.param("workPhone", "80173456788").with(csrf()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("User with this username already exists!")));
	}

	@Test
	public void testProcessMessengerRegistration() throws Exception {
		this.mockMvc
				.perform(multipart("/register/messenger").param("username", "manager").param("password", "volodya")
						.param("name", "volodya").param("surname", "kirov").param("lastname", "ivanovich")
						.param("phone", "80293456789").param("workExperience", "2").param("passportNumber", "ММ4567888")
						.param("carNumber", "4444МР6").with(csrf()))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("User with this username already exists!")));
	}

}
