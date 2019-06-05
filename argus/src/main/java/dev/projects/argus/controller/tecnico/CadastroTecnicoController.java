package dev.projects.argus.controller.tecnico;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.projects.argus.Utils;
import dev.projects.argus.model.Endereco;
import dev.projects.argus.model.Role;
import dev.projects.argus.model.Tecnico;
import dev.projects.argus.model.User;
import dev.projects.argus.repository.EnderecoRepository;
import dev.projects.argus.repository.TecnicoRepository;
import dev.projects.argus.repository.UserRepository;

@Controller
public class CadastroTecnicoController {
	
	@Autowired
	private TecnicoRepository tecnicos;
	
	@Autowired
	private EnderecoRepository enderecos; 
	
	@Autowired
	private UserRepository users;
	
	private String[] estados = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PB", "PR", "PE",
			"PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};

	@GetMapping("/cadastroTecnico")
	public ModelAndView showpage() {
		ModelAndView mav = new ModelAndView("cadastrotec");
		mav.addObject("estados",estados);
		return mav;
	}
	
	@PostMapping("/cadastroTecnico")
	public String cadastrar(@Valid User user, @Valid Tecnico tecnico, @Valid Endereco endereco, BindingResult res, RedirectAttributes attbs) {
		
		// : : - etapa de verificação - : :
		//erros no formulário
		if(res.hasErrors()) {
			List<FieldError> errors = res.getFieldErrors();
			String message = "";
			for (FieldError e : errors) {
				message = e.getDefaultMessage();
			}
			attbs.addFlashAttribute("erro", message.toString());
			return "redirect:/cadastro";
		}
		
		//verificar cpf
		if(!Utils.isCpfValid(user.getCpf())) {
			attbs.addFlashAttribute("erro", "O CPF digitado é inválido.");
			return "redirect:/cadastrotec";
		}
		
		//verifica se há clonagem de usuario
		if(tecnicos.findByUser(user) != null) {
			attbs.addFlashAttribute("erro", "Já existe um usuário com estes dados em nosso sistema.");
			return "redirect:/cadastrotec";
		}
		
		//criptografando senha
		user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
		
		//configurando permissões
		Role auth = new Role();
		auth.setRolename("ROLE_TECNICO");
		List<Role> auths = new ArrayList<>();
		auths.add(auth);
		user.setRoles(auths);
		
		//salvando e aplicando o usuario a classe Tecnico
		users.save(user);
		tecnico.setUser(user);
		
		//salvando o endereço
		enderecos.save(endereco);
		tecnico.setEndereco(endereco);
		
		//salvando o tecnico
		tecnicos.save(tecnico);
		return "redirect:/";
	}
	
}
