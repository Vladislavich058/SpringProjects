package restaurant.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import restaurant.domain.Admin;
import restaurant.security.AdminRegistrationForm;
import restaurant.security.ClientRegistrationForm;
import restaurant.security.ManagerRegistrationForm;
import restaurant.security.MessengerRegistrationForm;
import restaurant.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@ModelAttribute(name = "adminRegistrationForm")
	public AdminRegistrationForm adminRegistrationForm(@AuthenticationPrincipal Admin admin) {
		return adminService.showAdminRedactForm(admin);
	}

	@GetMapping("/account")
	public String showAccount(Model model, @AuthenticationPrincipal Admin admin) {
		model.addAttribute("admin", admin);
		return "adminAccount";
	}

	@GetMapping("/redact")
	public String showRedactAccount(Model model, @AuthenticationPrincipal Admin admin) {
		model.addAttribute("redactFlag", "redactFlag");
		return "adminAccount";
	}

	@PostMapping("/account")
	public String redactAdmin(Model model, @Valid AdminRegistrationForm form, Errors error,
			@AuthenticationPrincipal Admin admin) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			return "adminAccount";
		}

		adminService.redactAccount(admin, form);

		return "redirect:/admin/account";
	}

	@GetMapping("/messengers")
	public String showMessengers(Model model) {
		model.addAttribute("messengers", adminService.showMessengers());
		return "messengersList";
	}

	@GetMapping("/messenger/delete")
	public String deleteMessenger(@RequestParam Long id) {
		adminService.deleteMessenger(id);
		return "redirect:/admin/messengers";
	}

	@GetMapping("/messengers/find")
	public String findMessenger(Model model, @RequestParam String username) {
		model.addAttribute("messengers", adminService.findMessenger(username));
		return "messengersList";
	}

	@GetMapping("/messenger/status")
	public String changeStatusMessenger(@RequestParam Long id) {
		adminService.changeUserStatus(id);
		return "redirect:/admin/messengers";
	}

	@GetMapping("/messenger/redact")
	public String showMessengerRedact(Model model, @RequestParam Long id) {
		model.addAttribute("messengerRegistrationForm", adminService.showMessengerRedactForm(id));
		model.addAttribute("redactFlag", "redactFlag");
		return "messengersList";
	}

	@PostMapping("/messenger/redact")
	public String redactMessenger(Model model, @Valid MessengerRegistrationForm form, Errors error) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			return "messengersList";
		}

		if (!adminService.checkExistMessenger(form)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "messengersList";
		}

		adminService.redactMessenger(form);

		return "redirect:/admin/messengers";
	}

	@GetMapping("/managers")
	public String showManagers(Model model) {
		model.addAttribute("managers", adminService.showManagers());
		return "managersList";
	}

	@GetMapping("/manager/delete")
	public String deleteManager(@RequestParam Long id) {
		adminService.deleteManager(id);
		return "redirect:/admin/managers";
	}

	@GetMapping("/managers/find")
	public String findManager(Model model, @RequestParam String username) {
		model.addAttribute("managers", adminService.findManager(username));
		return "managersList";
	}

	@GetMapping("/manager/status")
	public String changeStatusManager(@RequestParam Long id) {
		adminService.changeUserStatus(id);
		return "redirect:/admin/managers";
	}

	@GetMapping("/manager/redact")
	public String showManagerRedact(Model model, @RequestParam Long id) {
		model.addAttribute("managerRegistrationForm", adminService.showManagerRedactForm(id));
		model.addAttribute("redactFlag", "redactFlag");
		return "managersList";
	}

	@PostMapping("/manager/redact")
	public String redactManager(Model model, @Valid ManagerRegistrationForm form, Errors error) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			return "managersList";
		}

		if (!adminService.checkExistManager(form)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "managersList";
		}

		adminService.redactManager(form);

		return "redirect:/admin/managers";
	}

	@GetMapping("/clients")
	public String showClients(Model model) {
		model.addAttribute("clients", adminService.showClients());
		return "clientsList";
	}

	@GetMapping("/client/delete")
	public String deleteClient(@RequestParam Long id) {
		adminService.deleteClient(id);
		return "redirect:/admin/clients";
	}

	@GetMapping("/clients/find")
	public String findClient(Model model, @RequestParam String username) {
		model.addAttribute("clients", adminService.findClient(username));
		return "clientsList";
	}

	@GetMapping("/client/status")
	public String changeStatusClient(@RequestParam Long id) {
		adminService.changeUserStatus(id);
		return "redirect:/admin/clients";
	}

	@GetMapping("/client/redact")
	public String showClientRedact(Model model, @RequestParam Long id) {
		model.addAttribute("clientRegistrationForm", adminService.showClientRedactForm(id));
		model.addAttribute("redactFlag", "redactFlag");
		return "clientsList";
	}

	@PostMapping("/client/redact")
	public String redactClient(Model model, @Valid ClientRegistrationForm form, Errors error) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			return "clientsList";
		}

		if (!adminService.checkExistClient(form)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "clientsList";
		}

		adminService.redactClient(form);

		return "redirect:/admin/clients";
	}
	
	@GetMapping("/analytics")
	public String analytics(Model model) {
		model.addAttribute("dishes", adminService.getDishTop());
		model.addAttribute("drinks", adminService.getDrinkTop());
		model.addAttribute("expiredPercent", adminService.getExpiredPercent());
		model.addAttribute("inTimePercent", adminService.getCompletedInTimePercent());
		model.addAttribute("canceledPercent", adminService.getCanceledPercent());
		model.addAttribute("payCheck", adminService.getAveragePaycheck());
		return "analytics";
	}
	
	@GetMapping("/printAnalytics")
	public String printAnalytics() {
		adminService.printAnalytics();
		return "redirect:/admin/analytics";
	}
}
