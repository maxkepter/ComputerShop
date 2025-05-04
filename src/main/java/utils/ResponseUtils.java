package utils;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class ResponseUtils {
	public static void evict(HttpServletResponse response) throws IOException {
		response.sendRedirect("Home");
	}
}
