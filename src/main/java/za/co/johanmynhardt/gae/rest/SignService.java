package za.co.johanmynhardt.gae.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Johan Mynhardt
 */
@Path("/sign")
public class SignService {

	@POST
	public Response processSign(@FormParam("content") String content) throws URISyntaxException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			logger.info("Name: " + user.getNickname());
			logger.info("Content: " + content);

			String guestBookName = "guestBookName";

			Key guestbookKey = KeyFactory.createKey("Greeting", guestBookName);
			Date date = new Date();
			Entity entity = new Entity("Greeting", guestbookKey);

			entity.setProperty("user", user);
			entity.setProperty("date", date);
			entity.setProperty("content", content);

			DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
			datastoreService.put(entity);
			return Response.ok().build();
		} else {
			String loginReturnUrl = userService.createLoginURL("/");
			logger.warning("The user needs to be logged in. Redirecting to " + loginReturnUrl);
			return Response.temporaryRedirect(new URI(loginReturnUrl)).build();
		}
	}

	@GET
	@Path("/signatures")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSignatures() {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Greeting", KeyFactory.createKey("Greeting", "guestBookName")).addSort("date", Query.SortDirection.DESCENDING);
		List<Entity> entities = datastoreService.prepare(query).asList(FetchOptions.Builder.withLimit(5));

		logger.info("Got the following entities: " + entities);

		return gson.toJson(entities);
	}

    private final Gson gson = new GsonBuilder().serializeNulls().create();
	private final Logger logger = Logger.getLogger(SignService.class.getName());
}
