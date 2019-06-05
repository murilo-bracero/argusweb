package dev.projects.argus.controller.cliente;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.projects.argus.model.Cliente;
import dev.projects.argus.model.Tecnico;
import dev.projects.argus.model.User;
import dev.projects.argus.repository.ClienteRepository;
import dev.projects.argus.repository.TecnicoRepository;
import dev.projects.argus.repository.UserRepository;

@Controller
public class CliHomepageController {

	@Autowired
	private ClienteRepository clientes;

	@Autowired
	private TecnicoRepository tecs;
	
	@Autowired
	private UserRepository users;
	
	//************** PAGINA PRINCIPAL ***********************

	@GetMapping("cliente/homepage")
	public ModelAndView homepage() {
		Cliente c = getCurrentUser();
		ModelAndView mav = new ModelAndView("cliente/homepage");
		mav.addObject("currentuser", c);
		return mav;
	}

	//****************** TECNICOS ***********************
	
	@GetMapping("cliente/tecnicos")
	public ModelAndView listTec() {
		ModelAndView mav = new ModelAndView("cliente/tecnicos");
		List<Tecnico> tecnicos = tecs.findAllTecnicos();
		mav.addObject("tecs", tecnicos);
		return mav;
	}

	@GetMapping("cliente/tecnico/{id}")
	public ModelAndView viewtec(@PathVariable("id") Long id) {
		Cliente c = getCurrentUser();
		ModelAndView mav = new ModelAndView("cliente/tecnico");
		Tecnico t = tecs.findById(id).orElse(null);

		List<Tecnico> tecs = c.getFavoritos();
		
		if(tecs.contains(t)) {
			mav.addObject("fav_icon", "favorite");
		}else {
			mav.addObject("fav_icon", "favorite_border");
		}

		mav.addObject("tecnico", t);
		return mav;
	}
	
	//*****************FAVORITOS*******************

	@GetMapping("cliente/favoritos")
	public ModelAndView favoritos() {
		Cliente c = getCurrentUser();
		ModelAndView mav = new ModelAndView("cliente/favoritos");
		List<Tecnico> tecs = c.getFavoritos();
		mav.addObject("tecs", tecs);
		return mav;
	}

	@GetMapping("cliente/tecnico/add/{id}")
	public String addfav(@PathVariable("id") Long id) {
		Cliente c = getCurrentUser();
		Tecnico t = tecs.findById(id).orElse(null);

		List<Tecnico> tecs = new ArrayList<>();

		tecs = c.getFavoritos();
		
		if(tecs.contains(t)) {
			removefav(id);
		}else {
			tecs.add(t);
			c.setFavoritos(tecs);
			clientes.save(c);
		}
		return "redirect:/cliente/tecnico/" + id;
	}

	public String removefav(Long id) {
		Cliente c = getCurrentUser();
		List<Tecnico> tecs = c.getFavoritos();

		for (int i = 0; i < tecs.size(); i++) {
			if (tecs.get(i).getId().equals(id)) {
				Tecnico t = this.tecs.findById(id).orElse(null);
				tecs.remove(t);
			}
		}

		c.setFavoritos(tecs);
		clientes.save(c);

		return "redirect:/cliente/favoritos";
	}
	
	//****** OPÇÕES ***********
	
	@GetMapping("cliente/options")
	public String options() {
		return "cliente/options";
	}
	
	//***************** EDITAR *****************
	//editar o perfil
	@GetMapping("cliente/edit")
	public ModelAndView showedit() {
		ModelAndView mav = new ModelAndView("cliente/edit/profile");
		Cliente c = getCurrentUser();
		mav.addObject("currentuser", c);
		return mav;
	}
	
	//editar perfil
	@PostMapping("cliente/edit/profile")
	public String edituser(User user, RedirectAttributes attbs) {
		User u = getCurrentUser().getUser();
		
		//confere se o nome não está vazio
		if(user.getNome().isEmpty() || user.getNome() == " ") {
			attbs.addFlashAttribute("erro", "O campo Nome não pode ficar vazio");
			return "redirect:/cliente/edit";
		}
		
		//confere se o email obedece a um regex simples
		if(!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			attbs.addFlashAttribute("erro", "O email digitado é inválido.");
			return "redirect:/cliente/edit";
		}
		u.setEmail(user.getEmail());
		u.setNome(user.getNome());
		users.save(u);
		return "redirect:/cliente/edit";
	}
	
	//trocar a foto de perfil
	@PostMapping("cliente/changeProfilePic")
	public String changeProfilePic(@PathVariable("pic") MultipartFile pic, RedirectAttributes attbs) {
		User user = getCurrentUser().getUser();
		String name = pic.getOriginalFilename();
		
		if(!name.endsWith(".jpg") && !name.endsWith(".jpeg") && !name.endsWith(".png")) {
			attbs.addFlashAttribute("erro", "Tipo de imagem não suportado. Tente enviar imagens JPG, JPEG ou PNG.");
			return "redirect:/cliente/edit";
		}
		
		String extension = name.substring(name.lastIndexOf("."), name.length());
		
		try {
			saveImage(pic, user, extension);
			
		} catch (Exception e) {
			attbs.addFlashAttribute("erro", "Ocorreu um erro ao processar a imagem");
			e.printStackTrace();
			return "redirect:/cliente/edit";
		}
		return "redirect:/cliente/homepage";
	}
	
	//mudar a senha
	@GetMapping("cliente/changepsswrd")
	public String showchange() {
		return "cliente/edit/senha";
	}
	
	@PostMapping("cliente/changepsswrd")
	public String changepsswrd(String old_psswrd, String new_psswrd, String new_psswrd2, RedirectAttributes attbs) {
		
		//verifica se nenhum campo está vazio
		if(old_psswrd.isEmpty() || new_psswrd.isEmpty() || new_psswrd2.isEmpty()) {
			attbs.addFlashAttribute("erro", "Não deixe campos vazios");
			return "redirect:/cliente/changepsswrd";
		}
		
		//verifica se a senha tem no minimo 6 caracteres
		if(new_psswrd.length() < 6) {
			attbs.addFlashAttribute("erro", "A senha deve conter no mínimo 6 dígitos");
			return "redirect:/cliente/changepsswrd";
		}
		
		//verifica se a senha atual é igual a nova
		if(old_psswrd.equals(new_psswrd)) {
			attbs.addFlashAttribute("erro", "Sua senha nova não pode ser igual a antiga");
			return "redirect:/cliente/changepsswrd";
		}
		
		//verifica se a repetição de segurança é valida
		if(!new_psswrd.equals(new_psswrd2)) {
			attbs.addFlashAttribute("erro", "As senhas não coincidem");
			return "redirect:/cliente/changepsswrd";
		}
		
		//visualiza se as senhas atual de segurança coincide com a natural coincidem
		if(!PsswrdCompare(old_psswrd)) {
			attbs.addFlashAttribute("erro", "Senha antiga incorreta");
			return "redirect:/cliente/changepsswrd";
		}
		
		User user = getCurrentUser().getUser();
		user.setSenha(new BCryptPasswordEncoder().encode(new_psswrd));
		users.save(user);
		return "redirect:/logout";
	}
	
	//deletar a conta
	@GetMapping("cliente/deleteaccount")
	public String showdelaccount() {
		return "cliente/edit/deletar";
	}
	
	@PostMapping("cliente/deleteaccount")
	public String deleteaccount(String cpf, String cpf2, RedirectAttributes attbs) {
		
		Cliente c  = getCurrentUser();
		User user =  c.getUser();
		
		if(cpf == "" || cpf2 == "") {
			attbs.addFlashAttribute("erro", "Não deixe campos vazios.");
			return "redirect:/cliente/deleteaccount";
		}
		if(!cpf.equals(cpf2)) {
			attbs.addFlashAttribute("erro", "Os CPF não coincidem.");
			return "redirect:/cliente/deleteaccount";
		}
		if(!user.getCpf().equals(cpf)) {
			attbs.addFlashAttribute("erro", "Este não é o seu CPF.");
			return "redirect:/cliente/deleteaccount";
		}
		clientes.delete(c);
		users.delete(user);
		return "redirect:/logout";
	}
	
	//salva imagem no servidor e armazena o caminho no banco de dados
	private void saveImage(MultipartFile pic, User user, String extension) throws Exception{
		String folder = "/Users/muril/Downloads/argus/argus/src/main/webapp/WEB-INF/profile/clientes/";
		byte[] bytes = pic.getBytes();
		Path path = Paths.get(folder + user.getCpf() + extension);
		Files.write(path, bytes);
		user.setPerfiluri("/profile/clientes/"+user.getCpf()+extension);
		users.save(user);
	}

	//************************ UTILS *********************************
	
	// retorna o usuário logado
	private Cliente getCurrentUser() {
		Cliente c = new Cliente();
		User user = new User();

		Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (currentUser instanceof UserDetails) {
			username = ((UserDetails) currentUser).getUsername();
			user = users.findByCpf(username);
			c = clientes.findByUser(user);
		} else {
			username = currentUser.toString();
			user = users.findByCpf(username);
			c = clientes.findByUser(user);
		}
		return c;
	}
	
	//compara as senhas para que se possa trocar a mesma
	private Boolean PsswrdCompare(String senha) {
		Cliente c = getCurrentUser();
		c.setUser(users.findByCpf(c.getUser().getCpf()));
		Boolean res = new BCryptPasswordEncoder().matches(senha, c.getUser().getSenha());
		return res;
	}

}
