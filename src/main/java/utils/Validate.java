package utils;

public class Validate {
	public static boolean checkPhoneNumber(String phoneNumber) {
		if (phoneNumber == null) {
			return false;
		}
		return phoneNumber.trim().matches("^\\+?1?[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$");
	}

	public static boolean checkInt(String intnumber) {
		try {
			Integer.parseInt(intnumber);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean checkDouble(String doubleNumber) {
		try {
			Double.parseDouble(doubleNumber);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkInt(String[] strings) {
		if(strings==null) {
			return false;
		}
		for (String s : strings) {
			if (!checkInt(s)) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkDouble(String[] doubleNumber) {
		for (String s : doubleNumber) {
			if (!checkDouble(s) && !s.isBlank()) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkString(String input) {
		return input != null && !input.trim().isEmpty();
	}
}