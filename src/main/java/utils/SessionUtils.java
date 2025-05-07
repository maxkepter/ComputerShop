package utils;

import Model.User;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
	public static User getUser(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return user;
	}

	public static boolean isAdmin(HttpSession session) {
		if (session == null) {
			return false;
		}
		return getUser(session).getUserID() == 1;
	}

}
