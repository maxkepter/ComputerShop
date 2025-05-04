package Model;

public class OrderDetail {
	private int productID;
	private int orderQuantity;
	private double amount;

	

	public OrderDetail(int productID, int orderQuantity, double amount) {
		super();
		this.productID = productID;
		this.orderQuantity = orderQuantity;
		this.amount = amount;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
