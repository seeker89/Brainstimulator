package controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import models.*;
import play.mvc.*;

import views.html.*;
import views.html.defaultpages.error;

public class Browser extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
  
    public static Result browse(String cat){
    	
    	Category root = Category.getRoot();
    	List<Video> videos;
        User userConnected = Session.userConnected(session("brainstimulatorSession"), true);

        if ("favourites".equals(cat)) {
        	// if not logged in, redirect to login/register
        	// if logged in, get favourite videos and display
			if (userConnected != null) {
				videos = userConnected.favouriteVideos;
				return ok(browserMain.render(
					"BrainStimulator : My favourites",
					videos,
					root.subCategories,
					root,
					"Your favourite videos",
					cat,
					userConnected.firstName
				)
				);
			} else {
				return redirect(controllers.routes.Account.login());
			}
	    	}
	    	
	    	if ("user".equals(cat)) {
	    		// if not logged in, redirect to login/register
	    		// if logged in, get user's videos and display
			if (userConnected != null) {
				videos = userConnected.videos;
				return ok(browserMain.render(
					"BrainStimulator : My videos",
					videos,
					root.subCategories,
					root,
					"The videos You uploaded",
					cat,
					userConnected.firstName
				)
				);
			} else {
				return redirect(controllers.routes.Account.login());
			}
    	}

    	// try to find the category, default to all videos
    	Category category = Category.find.where().eq("title", cat).findUnique();
    	
    	if (category == null){
    		videos = Video.find.all();
    		category = root;
    	} else {
    		videos = Video.find.where().eq("category", category).findList();
    	}
	return ok(browserMain.render(
		"BrainStimulator : " + cat,
    		videos,
    		root.subCategories,
    		category,
    		"",
    		cat,
		userConnected != null ? userConnected.firstName : "Guest"
    		)
    	);
    }
    
    public static Result showVideo(long id){
    	
    	Video video = Video.find.ref(id);
    	
    	if (video == null) return Results.notFound();
    	
    	video.views++;
    	video.save();

		User userConnected = Session.userConnected(session("brainstimulatorSession"), true);
	    	
		return ok(videoFullscreen.render(
				"BrainStimulator : " + video.title,
				video,
				userConnected,
				(userConnected == null || userConnected.favouriteVideos == null) ? false :
					userConnected.favouriteVideos.contains(video)
			)
	    );
    }   
    public static Result showSeries(long id){
    	return TODO;
    }
    
    public static Result rss(){
    	response().setContentType("application/rss+xml");
    	List<Video> videos = Video.find.all();
    	List<Series> serieses = Series.find.all();
		return ok(
				rss.render(
						videos,
						serieses
    			)
    	);
    }
}
