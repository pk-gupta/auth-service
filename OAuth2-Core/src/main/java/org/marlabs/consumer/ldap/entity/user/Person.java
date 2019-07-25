package org.marlabs.consumer.ldap.entity.user;

public class Person {

	private String uid;

	private String cn;

	private String mail;

	private String givenName;

	private String telephoneNumber;

	private String department;

	private String loc;

	private String manager;

	private String userPrincipalName;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getUserPrincipalName() {
		return userPrincipalName;
	}

	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}

	@Override
	public String toString() {
		return "Person [uid=" + uid + ", cn=" + cn + ", mail=" + mail
				+ ", givenName=" + givenName + ", telephoneNumber="
				+ telephoneNumber + ", department=" + department + ", loc="
				+ loc + ", manager=" + manager + ", userPrincipalName="
				+ userPrincipalName + "]";
	}

}
