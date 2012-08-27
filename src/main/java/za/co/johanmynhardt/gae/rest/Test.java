package za.co.johanmynhardt.gae.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.representation.Form;

/**
 * @author Johan Mynhardt
 */
@Path("/test")
public class Test {
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/post")
	public String verbosePost(@Context Request request) {
		MultivaluedMap<String, String> queryParameters = ((HttpRequestContext) request).getQueryParameters();

		StringBuilder stringBuilder = new StringBuilder("Post Query Parameters:\n-------\n");
		for (String key : queryParameters.keySet()) {
			stringBuilder.append(String.format("%30s : %s\n", key, queryParameters.get(key)));
		}
		stringBuilder.append("\n------\n");

		Form form = ((HttpRequestContext) request).getFormParameters();
		stringBuilder.append("Post Form Parameters:\n-------\n");
		for (String key : form.keySet()) {
			stringBuilder.append(String.format("%30s : %s\n", key, form.get(key)));
		}
		stringBuilder.append("\n------\n");

		logger.info(stringBuilder.toString());

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			logger.info("Hello " + user.getNickname() + ", your email addy is " + user.getEmail());
		} else {
			logger.info("User is null.");
			logger.info("Login URL: " + userService.createLoginURL("/"));
		}

		return stringBuilder.toString();
	}

	private final Logger logger = Logger.getLogger(Test.class.getName());
}
