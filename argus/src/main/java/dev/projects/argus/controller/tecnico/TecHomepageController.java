package dev.projects.argus.controller.tecnico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import dev.projects.argus.model.Tecnico;
import dev.projects.argus.model.User;
import dev.projects.argus.repository.TecnicoRepository;
import dev.projects.argus.repository.UserRepository;

@Controller
public class TecHomepageController {
	
	@Autowired
	private TecnicoRepository tecnicos;
	
	@Autowired
	private UserRepository users;

	@GetMapping("tecnico/homepage")
	public ModelAndView homepage() {
		ModelAndView mav = new ModelAndView("/tecnico/homepage");
		Tecnico tecnico = getCurrentUser();
		mav.addObject("currentuser", tecnico);
		return mav;
	} 
	
	private Tecnico getCurrentUser() {
		Tecnico tecnico = new Tecnico();
		User user = new User();

		Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (currentUser instanceof UserDetails) {
			username = ((UserDetails) currentUser).getUsername();
			user = users.findByCpf(username);
			tecnico = tecnicos.findByUser(user);
		} else {
			username = currentUser.toString();
			user = users.findByCpf(username);
			tecnico = tecnicos.findByUser(user);
		}
		return tecnico;
	}
	
}
