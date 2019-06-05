package dev.projects.argus.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Tecnico implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private User user;
	
	@NotEmpty(message="O horário do expediente deve ser declarado.")
	@Size(min = 11, max = 11, message="Modelo inválido, use o modelo 00:00-00:00")
	private String expediente;
	
	@NotEmpty(message="Sua área de atuação deve ser declarada")
	private String area;
	
	@NotEmpty(message="Um número para contato é essencial, não o deixe em branco")
	@Size(min = 10, max = 11, message="O numero de telefone deve conter entre 10 e 11 números, seguindo o modelo - 11925413652 - exemplo.")
	private String telefone;
	
	private String perfiluri;
	private String status;
	
	@OneToOne
	private Endereco endereco;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getExpediente() {
		return expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPerfiluri() {
		if(perfiluri == null) 
		return "https://image.flaticon.com/icons/png/512/69/69114.png";
		else
		return perfiluri;
	}

	public void setPerfiluri(String perfiluri) {
		this.perfiluri = perfiluri;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
