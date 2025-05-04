package Model;

public class Specification {
	private int specID;
	private String specName;

	public Specification(int specID, String specName) {
		super();
		this.specID = specID;
		this.specName = specName;
	}

	public int getSpecID() {
		return specID;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecID(int specID) {
		this.specID = specID;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

}
