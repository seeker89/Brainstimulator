import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;
import models.*;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {

    	Category root;
    	Category catMaths;
    	Category catPhysics;
    	Category catWomen;
    	
    	Video v1, v2, v3, v4, v5, v6;
    	
    	User u1, u2;
    	
    	Series s1;
    	
//    	Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
    	
    	if (User.find.findRowCount() == 0) {
    		//Ebean.save(all.get("users"));
    		u1 = new User("Mikolaj", "Pawlikowski", 24, "mikolaj@pawlikowski.pl", "test");
    		u1.save();
    		
    		u2 = new User("Admin", "Admin", 24, "admin@mail.com", "admin");
    		u2.makeAdmin();
    		u2.save();
    		
    		root = new Category("root", "");
    		root.parent = null;
        	catMaths = new Category("Maths", "All we know about maths");
        	catPhysics = new Category("Physics", "All we know about physics");
        	catWomen = new Category("Women", "All we know about women");

    		root.subCategories.add(catWomen);
    		root.subCategories.add(catMaths);
    		root.subCategories.add(catPhysics);
    		root.subCategories.add(new Category("Geography", "All we know about geography"));
    		root.subCategories.add(new Category("Technology", "All we know about technology"));
    		
    		//root.saveManyToManyAssociations("subCategories");
    		root.save();
        		
    		// ADD few VIDEOS
    		v1 = new Video(u1, "8ucz_pm3LX8", "Mathematical curves", "In this lesson, we are going to learn how to deal with nice shapes", "shapes beyonce", catWomen);
    		v1.comments.add(new Comment("I love maths!", u1, new Date()));
    		v1.comments.add(new Comment("So do I !", u1, new Date()));
    		v1.save();
    		
    		v2 = new Video(u1, "RZuJ_OHBN78", "Mathematical curves 2", "In this lesson, we are going to learn how to deal with nice shapes", "shapes beyonce", catWomen);
    		v2.save();
    		
    		v3 = new Video(u1, "FHp2KgyQUFk", "Mathematical curves 3", "In this lesson, we are going to learn how to deal with nice shapes", "shapes beyonce", catWomen);
    		v3.save();
    		
    		v4 = new Video(u1, "vLnQPowbXOo", "Mr Bean", "Classical Mr Bean", "shapes beyonce", catMaths);
    		v4.save();
    		
    		v5 = new Video(u1, "A2P4rXEpIYU", "Applied physics", "In this lesson, we are going to practice some physics..", "shapes beyonce", catPhysics);
    		v5.save();
    		
    		// ADD a SERIES
    		s1 = new Series(u1, "Parabolic shapes", "All you need to know about curves", "maths, curves");
    		s1.videos.add(v1);
    		s1.videos.add(v2);
    		s1.videos.add(v3);

    		v1.serieses.add(s1);
    		v2.serieses.add(s1);
    		v3.serieses.add(s1);
    		
    		s1.save();
    	}
    }
}