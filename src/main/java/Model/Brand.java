package Model;

public class Brand {
	private int BrandID;
	private String BrandName;

	public Brand(int brandID, String brandName) {
		super();
		BrandID = brandID;
		BrandName = brandName;
	}

	public int getBrandID() {
		return BrandID;
	}

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandID(int brandID) {
		BrandID = brandID;
	}

	public void setBrandName(String brandName) {
		BrandName = brandName;
	}

}
