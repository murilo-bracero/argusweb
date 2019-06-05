package dev.projects.argus.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements GrantedAuthority, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String rolename;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.rolename;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

}
