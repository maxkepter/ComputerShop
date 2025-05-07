package utils;

import java.util.ArrayList;
import java.util.List;

import Model.SpecProduct;

public class StringFilter {
	public static List<Integer> toListInt(String[] strings) {
		List<Integer> list = new ArrayList<>();
		for (String string : strings) {
			if (string.isBlank()) {
				list.add(null);
			} else {
				list.add(Integer.parseInt(string));
			}
		}
		return list;
	}

	public static List<String> toListString(String[] strings) {
		List<String> list = new ArrayList<>();
		for (String string : strings) {
			if (string.isBlank()) {
				list.add(null);
			} else {
				list.add(string);
			}
		}
		return list;
	}

	public static List<SpecProduct> toListSpecProduct(String[] specIds, String[] specProducts, String[] specValue) {
		List<SpecProduct> list = new ArrayList<>();
		int length = specIds.length;

		for (int i = 0; i < length; i++) {
			int specId = specIds[i].isBlank() ? 0 : Integer.parseInt(specIds[i]);
			String specProduct = specProducts[i].isBlank() ? null : specProducts[i];
			Double value = specValue[i].isBlank() ? null : Double.parseDouble(specValue[i]);

			SpecProduct sp = new SpecProduct(specId, specProduct, value);
			list.add(sp);
		}

		return list;
	}

	

}
