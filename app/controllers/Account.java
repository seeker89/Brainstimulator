package controllers;

import models.Category;
import models.Comment;
import models.Series;
import models.Session;
import models.User;
import models.Video;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;

import views.html.*;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.node.ObjectNode;

import static play.data.Form.form;

public class Account extends Controller {

	public static Result login() {
		User userConnected = Session.userConnected(session("brainstimulatorSession"), true);
		if (userConnected != null)
			return redirect(routes.Account.account());
		else
			return ok(login.render());
	}

	public static Result signIn() {
		DynamicForm requestData = form().bindFromRequest();
		String mail = requestData.get("mail");
		String password = requestData.get("password");
		User user = User.authenticate(mail, password);
		if (user != null) {
			session("brainstimulatorSession", Session.createSession(user));
			return redirect(routes.Account.account());
		} else {
			return unauthorized("Oops, you are not connected");
		}
	}

	public static Result signUp() {
		Form<User> form = form(User.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(login.render());
		} else {
			User data = form.get();
			if (User.find.where().eq("mail", data.mail).findUnique() != null)
				return forbidden("You are already a member");
			data.registered = new Date();
			data.save();
			session("brainstimulatorSession", Session.createSession(data));
			return redirect(controllers.routes.Account.account());
		}
	}

	public static Result logout() {
		Session.deleteSession(session("brainstimulatorSession"));
		session().clear();
		return redirect(controllers.routes.Browser.browse("home"));
	}

	public static Result account() {
		User userConnected = Session.userConnected(session("brainstimulatorSession"), true);
		if (userConnected == null){
			return redirect(controllers.routes.Account.login());
		}else{
			return ok(account.render(
					Session.userConnected(session("brainstimulatorSession"), false), 
					Category.getRoot()
					));
		}
	}

	public static Result addVideo() {
		DynamicForm form = form().bindFromRequest();
		User user = Session.userConnected(session("brainstimulatorSession"), true);
		if (user != null) {
			if (form.hasErrors()) {
				return badRequest();
			} else {
				Pattern p = Pattern.compile("[^=]*=(.*)");//https://www.youtube.com/watch\?v=(.*)$");
				Matcher matcher = p.matcher(form.get("url"));
				if (!matcher.matches())
					return TODO;
				Video data = new Video(user, matcher.group(1), form.get("title"),
					form.get("description"),
					form.get("tags"),
					null);
				Series series = Series
					.find
					.where()
					.eq("id", Integer.parseInt(form.get("series")))
					.findUnique();
				if (series != null) {
					data.serieses.add(series);
					series.videos.add(data);
					series.save();
				}
				// add category
				Category cat = Category
						.find
						.where()
						.eq("id", Integer.parseInt(form.get("category")))
						.findUnique();
				if (cat != null) {
					data.category = cat;
				}
				System.out.println(data);
				
				data.save();
				return redirect(controllers.routes.Browser.showVideo(data.id));
			}
		} else {
			return badRequest(login.render());
		}
	}

	public static Result addSeries() {
		DynamicForm form = form().bindFromRequest();
		User user = Session.userConnected(session("brainstimulatorSession"), true);
		if (user != null) {
			if (form.hasErrors()) {
				return badRequest();
			} else {
				Series data = new Series(user, form.get("title"), form.get("description"),
					form.get("tags"));
				data.save();
				return redirect(controllers.routes.Account.account());
			}
		} else {
			return badRequest(login.render());
		}
	}

	public static Result addFavourite(Long id) {
		User user = Session.userConnected(session("brainstimulatorSession"), true);
		if (user == null)
			return redirect(controllers.routes.Account.login());
		Video video = Video.find.ref(id);
		user.favouriteVideos.add(video);
		user.save();
		return redirect(controllers.routes.Browser.showVideo(id));
	}
	

	public static Result addFavouritePost() {
		RequestBody body = request().body();
		Map<String, String[]> things = body.asFormUrlEncoded();
		
		Long id = Long.valueOf(things.get("id")[0]);
		
		User user = Session.userConnected(session("brainstimulatorSession"), false);
		if (user == null){	
			System.out.println("forbidden");
			return forbidden();
		}
		
		Video video = Video.find.ref(id);
		if (!user.favouriteVideos.contains(video)){
			user.favouriteVideos.add(video);
			user.save();
		}
		
		System.out.println("Added ok video id="+id);
		
		ObjectNode result = Json.newObject();
		result.put("success", true);
		return ok(result);
	}
	
	

	public static Result addCommentPost() {
		RequestBody body = request().body();
		Map<String, String[]> things = body.asFormUrlEncoded();
		
		Long id = Long.valueOf(things.get("id")[0]);
		String text = things.get("text")[0];
		
		User user = Session.userConnected(session("brainstimulatorSession"), false);
		if (user == null){	
			System.out.println("forbidden");
			return forbidden();
		}
		
		Video video = Video.find.ref(id);
		video.comments.add(
			new Comment(text, user, new Date())	
		);
		video.save();
		
		System.out.println("Added a comment to video id="+id);
		
		ObjectNode result = Json.newObject();
		result.put("success", true);
		return ok(result);
	}

	public static Result vote(Long id) {
		User user = Session.userConnected(session("brainstimulatorSession"), false);
		if (user == null)
			return redirect(controllers.routes.Account.login());
		Video video = Video.find.ref(id);
		video.votes++;
		video.save();
		return redirect(controllers.routes.Browser.showVideo(id));
	}

	public static Result votePost() {
		User user = Session.userConnected(session("brainstimulatorSession"), false);
		if (user == null)
			return redirect(controllers.routes.Account.login());

		RequestBody body = request().body();
		System.out.println(body);
		Map<String, String[]> things = body.asFormUrlEncoded();

		Long id = Long.valueOf(things.get("id")[0]);

		Video video = Video.find.ref(id);
		video.votes++;
		video.save();

		ObjectNode result = Json.newObject();
		result.put("success", true);
		result.put("number", video.votes);
		return ok(result);

	}

}
