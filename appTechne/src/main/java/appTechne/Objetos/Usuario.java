package appTechne.Objetos;

import java.util.UUID;

import javax.persistence.Id;

public class Usuario {
	
	public Usuario(){
		
	}
	
	
	@Id
	private java.lang.String guid = UUID.randomUUID().toString().toUpperCase();
	
	private String username;
	private String passWd;
	
	
	public java.lang.String getGuid(){
	    return this.guid;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassWd() {
		return passWd;
	}
	public void setPassWd(String passWd) {
		this.passWd = passWd;
	}
	
	
	@Override
	public String toString() {
		return "Usuario [username=" + username + ", passWd=" + passWd + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((passWd == null) ? 0 : passWd.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (passWd == null) {
			if (other.passWd != null)
				return false;
		} else if (!passWd.equals(other.passWd))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
}
