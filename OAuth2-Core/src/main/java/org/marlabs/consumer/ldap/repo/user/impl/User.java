package org.marlabs.consumer.ldap.repo.user.impl;

public class User {
	private String commonName;
	private String telephone;

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String toString() {
		return commonName + "-" + telephone;
	}
}
