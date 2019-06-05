package dev.projects.argus.controller.cliente;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.projects.argus.Utils;
import dev.projects.argus.model.Cliente;
import dev.projects.argus.model.Role;
import dev.projects.argus.model.User;
import dev.projects.argus.repository.ClienteRepository;
import dev.projects.argus.repository.UserRepository;

@Controller
public class CadastroController {

	@Autowired
	private ClienteRepository clientes;
	
	@Autowired
	private UserRepository users;

	@GetMapping(value = "/cadastro")
	public String cadastro(Cliente cliente) {
		return "cadastro";
	}

	@PostMapping(value = "/cadastro")
	public String cadastrar(@Valid User user, BindingResult res, RedirectAttributes attbs) {
		
		//erros no formulário
		if (res.hasErrors()) {
			List<FieldError> errors = res.getFieldErrors();
			String message = "";
			for (FieldError e : errors) {
				message = e.getDefaultMessage();
			}
			attbs.addFlashAttribute("mensagem", message.toString());
			return "redirect:/cadastro";
		}

		//confere se o cpf obedece a regra matematica
		if(!Utils.isCpfValid(user.getCpf())) {
			attbs.addFlashAttribute("mensagem", "O CPF digitado é inválido.");
			return "redirect:/cadastro";
		}
		
		//confere se há um usuário com este CPF ou endereço de email em nosso db
		if (clientes.findByUser(user) != null) {
			attbs.addFlashAttribute("mensagem", "Este usuário já existe no nosso sistema");
			return "redirect:/cadastro";
		}
		
		Cliente cliente = new Cliente();
		
		// configurando permissões
		Role auth = new Role();
		auth.setRolename("ROLE_CLIENTE");
		List<Role> auths = new ArrayList<>();
		auths.add(auth);
		user.setRoles(auths);
		
		// criptografando a senha
		user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
		
		// Salvando e aplicando o usuario a classe
		users.save(user);
		cliente.setUser(user);
		clientes.save(cliente);
		return "redirect:/";
	}

}
