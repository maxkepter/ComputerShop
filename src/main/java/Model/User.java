package Model;

public class User {
	private int userID;
	private int userRole;
	private String userName;
	private String email;
	private String phoneNumber;
	private String address;
	private String firstName;
	private String lastName;

	public User() {
	}

	public User(int userID, int userRole, String userName, String email, String phoneNumber, String address,
			String firstName, String lastName) {
		this.userID = userID;
		this.userRole = userRole;
		this.userName = userName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public User(int userRole, String userName, String email, String phoneNumber, String address, String firstName,
			String lastName) {
		super();
		this.userRole = userRole;
		this.userName = userName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// Getter và Setter cho tất cả các thuộc tính
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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

	@Override
	public String toString() {
		return "User [userID=" + userID + ", userRole=" + userRole + ", userName=" + userName + ", email=" + email
				+ ", phoneNumber=" + phoneNumber + ", address=" + address + ", firstName=" + firstName + ", lastName="
				+ lastName + "]";
	}
}
