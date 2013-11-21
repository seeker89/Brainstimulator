package controllers;

import models.Session;
import models.Video;
import models.User;
import play.mvc.*;
import views.html.*;

public class Admin extends Controller {
	
	public static boolean isAdmin(boolean renew){
		User user = Session.userConnected(session("brainstimulatorSession"), renew);
		return user != null && user.isAdmin();
	}

	public static Result login(){
		if (isAdmin(true)){
			return redirect(controllers.routes.Admin.index());
		}
		return redirect(controllers.routes.Account.login());
	}
	
	public static Result index(){
		if (isAdmin(true)){
			return ok(adm.render("admin"));
		}
		return redirect(controllers.routes.Account.login());
	}
	
	public static Result getVideos(){
		if (isAdmin(false)){
			return ok(play.libs.Json.toJson(Video.find.all()));
		}
		return forbidden();
	}
	
	public static Result getUsers(){
		if (isAdmin(false)){
			return ok(play.libs.Json.toJson(User.find.all()));
		}
		return forbidden();
	}
	
	public static Result removeVideo(Long id){
		if (isAdmin(false)){
			System.out.println("remove video: "+id);
			
			Video vid = Video.find.ref(id);
			if (vid != null){
				vid.delete();
				return ok();
			}
		}
		return forbidden();
	}
	
	
	public static Result removeUser(Long id){
		if (isAdmin(false)){
			System.out.println("remove user: "+id);
			
			User usr = User.find.ref(id);
			if (usr != null){
				usr.delete();
				return ok();
			}
		}
		return forbidden();
	}
}
