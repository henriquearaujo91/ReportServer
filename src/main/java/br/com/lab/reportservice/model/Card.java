package br.com.lab.reportservice.model;

public class Card {

	private Integer cod;
	private String name;
	private String phone;

	public Card(Integer cod, String name, String phone) {
		super();
		this.cod = cod;
		this.name = name;
		this.phone = phone;
	}

	public Integer getCod() {
		return cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
