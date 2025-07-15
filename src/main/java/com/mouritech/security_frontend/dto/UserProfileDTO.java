package com.mouritech.security_frontend.dto;

public class UserProfileDTO {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String village;
    private String district;
    private String state;
    private String pincode;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public UserProfileDTO(String fullName, String email, String phone, String address, String village, String district,
			String state, String pincode) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.village = village;
		this.district = district;
		this.state = state;
		this.pincode = pincode;
	}
	public UserProfileDTO() {
		// TODO Auto-generated constructor stub
	}
    
    

}

