package utils;

import Model.User;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
	public static User getUser(HttpSession session) {
		User user=(User)session.getAttribute("user");
		return user;
	}
}
