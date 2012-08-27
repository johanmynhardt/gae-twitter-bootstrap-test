package za.co.johanmynhardt.gae.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author Johan Mynhardt
 */
@Path("/login")
public class LoginService {

	@GET
	public String isLoggedIn() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			return "<a href=\"" + userService.createLoginURL("/") + "\">Login</a>";
		} else {
			return "Welcome " + user.getNickname();
		}
	}
}
