package Model;

import java.sql.Date;
import java.util.List;

public class Order {
	private int orderID;
	private Date orderDate;
	private int status;
	private int userID;
	private List<OrderDetail> orderDetails;	

	public Order(int orderID, Date orderDate, int status, int userID, List<OrderDetail> orderDetails) {
		super();
		this.orderID = orderID;
		this.orderDate = orderDate;
		this.status = status;
		this.userID = userID;
		this.orderDetails = orderDetails;
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

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public int getTotalAmount() {
		int total=0;if(orderDetails!=null) {
			for(OrderDetail orderDetail:orderDetails) {
				total+=orderDetail.getAmount();
			}
		}
		return  total;
	}
}
