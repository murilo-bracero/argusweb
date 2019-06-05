package dev.projects.argus.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Endereco {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="O Estado não pode ficar vazio")
	@Size(min = 2, max = 2, message="UF inválida")
	private String estado;
	
	@NotEmpty(message="O CEP deve ser informado.")
	@Size(min=8, max=8, message="O CEP deve conter 8 números, não use hífens ou pontos")
	private String cep;
	
	@NotEmpty(message="A cidade deve ser informada.")
	private String cidade;
	
	@NotEmpty(message="Seu logradouro não pode ficar vazio")
	private String logradouro;
	
	@NotNull(message="O número do logradouro deve ser informado")
	private Integer numero;
	
	private String complemento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
}
