package com.Aero.Beauty.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SignUpRequest {

	private String firstName;
	private String lastName;
	private String email;
	private String motDePasse;

	private List<AddressDTO> addresses;
	private List<Long> favorites;
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	public List<Long> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<Long> favorites) {
		this.favorites = favorites;
	}
}
