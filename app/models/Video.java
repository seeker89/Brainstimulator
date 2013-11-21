package models;

import java.util.*;

import javax.persistence.*;

import org.codehaus.jackson.annotate.*;

import play.db.ebean.*;
import play.data.validation.*;

@Entity 
public class Video extends Model {
	
  public 	Video(User author, String url, String title, String description, String tags, Category category) {
		super();
		this.author = author;
		this.url = url;
		this.title = title;
		this.description = description;
		this.tags = tags;
		this.category = category;
	}

private static final long serialVersionUID = 2671872337319214628L;

  @Id
  @Constraints.Min(10)
  public Long id;
  
  @Constraints.Required
  @ManyToOne
  @JsonBackReference
  public User author;
  
  @Constraints.Required
  public String url;
  
  @Constraints.Required
  public String title;

  @Constraints.Required
  public String description;
  
  @Constraints.Required
  public String tags;

  @ManyToOne
  @JsonManagedReference
  public Category category;

  @OneToMany(cascade = CascadeType.ALL)
  public Set<Comment> comments =  new HashSet<Comment>();
  
  @ManyToMany(cascade = CascadeType.PERSIST)
  public Set<Series> serieses =  new HashSet<Series>();

  public int views = 0;

  public int votes = 0;
  
  public static Finder<Long,Video> find = new Finder<Long,Video>(
    Long.class, Video.class
  ); 
  
  public List<Comment> getOrderedComments(){
	  return Comment.find.where().eq("video_id", this.id).order().asc("registered").findList();
  }
}