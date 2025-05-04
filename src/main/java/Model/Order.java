package Model;

import java.util.Date;

public class Order {
	private int orderID;
	private Date orderDate;
	private int status;
	private int userID;

	public Order(int orderID, Date orderDate, int status, int userID) {
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.status = status;
		this.userID = userID;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
}
