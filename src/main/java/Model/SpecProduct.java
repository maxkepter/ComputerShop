package Model;

public class SpecProduct {
	private int specId;
	private String specName;

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	private String specProduct;
	private Double specValue;

	// constructor này dùng để tạo dữ liêu trong bảng
	public SpecProduct(int specId, String specProduct, Double specValue) {
		super();
		this.specId = specId;
		this.specProduct = specProduct;
		this.specValue = specValue;
	}

//	constructor này dùng để lấy các thông tin cho product
	public SpecProduct(String specName, String specProduct, Double specValue) {
		super();
		this.specName = specName;
		this.specProduct = specProduct;
		this.specValue = specValue;
	}

	public int getSpecId() {
		return specId;
	}

	public String getSpecProduct() {
		return specProduct;
	}

	public Double getSpecValue() {
		return specValue;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public void setSpecProduct(String specProduct) {
		this.specProduct = specProduct;
	}

	public void setSpecValue(Double specValue) {
		this.specValue = specValue;
	}

}
