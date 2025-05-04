package utils;

import Model.User;
import jakarta.servlet.http.HttpSession;

public class UserFilter {
		public static boolean isAdmin(HttpSession session) {
			User user=(User)session.getAttribute("user");
			if(user.getUserRole()!=1) {
				return false;
			}
			return true;
		}
		
		public static boolean userFilterById(HttpSession session,int userId) {
			User user=(User)session.getAttribute("user");
			if(user.getUserID()==userId) {
				return true;
			}
			return false;
		}
}
