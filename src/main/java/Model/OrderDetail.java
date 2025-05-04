package Model;

public class OrderDetail {
	private int orderID;
	private int productID;
	private int orderQuantity;

	public OrderDetail(int orderID, int productID, int orderQuantity) {
		this.orderID = orderID;
		this.productID = productID;
		this.orderQuantity = orderQuantity;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
}
