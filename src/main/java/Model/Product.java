package Model;

import java.util.List;

public class Product {
	private int productID;
	private String productName;
	private int productQuantity;
	private String description;
	private double price;
	private int brandID;
	private double avgRate; // not available when creating new object, deafault 0
	private int quantitySold; // not available when creating new object, deafault 0
	private List<String> productType; // list này chứa tên type không phải id
	private List<SpecProduct> specProducts;
	private List<String> imageProduct;

	// constructor này dùng để lấy đầy đủ tất cả thông tin liên quan đến sản phẩm
	public Product(int productID, String productName, int productQuantity, String description, double price,
			int brandID, double avgRate, int quantitySold, List<String> productType, List<SpecProduct> specProducts,
			List<String> imageProduct) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.description = description;
		this.price = price;
		this.brandID = brandID;
		this.avgRate = avgRate;
		this.quantitySold = quantitySold;
		this.productType = productType;
		this.specProducts = specProducts;
		this.imageProduct = imageProduct;
	}

	// construct này dùng để tạo sản phẩm không cần
	// id,avgRate,quantitySold,typeProduct
	public Product(String productName, int productQuantity, String description, double price, int brandID,
			List<SpecProduct> specProducts, List<String> imageProduct) {
		super();
		this.productName = productName;
		this.productQuantity = productQuantity;
		this.description = description;
		this.price = price;
		this.brandID = brandID;
		this.avgRate = 0;
		this.quantitySold = 0;
		this.specProducts = specProducts;
		this.imageProduct = imageProduct;
	}

	public int getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public int getBrandID() {
		return brandID;
	}

	public double getAvgRate() {
		return avgRate;
	}

	public int getQuantitySold() {
		return quantitySold;
	}

	public List<String> getProductType() {
		return productType;
	}

	public List<SpecProduct> getSpecProducts() {
		return specProducts;
	}

	public List<String> getImageProduct() {
		return imageProduct;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setBrandID(int brandID) {
		this.brandID = brandID;
	}

	public void setAvgRate(double avgRate) {
		this.avgRate = avgRate;
	}

	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	public void setProductType(List<String> productType) {
		this.productType = productType;
	}

	public void setSpecProducts(List<SpecProduct> specProducts) {
		this.specProducts = specProducts;
	}

	public void setImageProduct(List<String> imageProduct) {
		this.imageProduct = imageProduct;
	}

}
