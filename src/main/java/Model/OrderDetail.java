package Model;

public class OrderDetail {
	private int productID;
	private String productName;
	private int orderQuantity;
	private double amount;
	public OrderDetail(int productID, String productName, int orderQuantity, double amount) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.orderQuantity = orderQuantity;
		this.amount = amount;
	}

	public int getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	

}

	
