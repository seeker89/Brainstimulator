package models;

import java.util.*;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;


@Entity
public class User extends Model {

	public User(String firstName, String lastName, int age, String mail, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.mail = mail;
		this.password = password;
		this.isAdmin = false;
	}
	
	
	public void makeAdmin(){
		this.isAdmin = true;
	}
	
	public boolean isAdmin(){
		return this.isAdmin;
	}
	
	public static User authenticate(String mail, String password) {
        return find
        		.where()
        		.eq("mail", mail)
        		.eq("password", password)
        		.findUnique();
    }

	private static final long serialVersionUID = -4358274568199732557L;

  @Id
  @Constraints.Min(10)
  public Long id;
  
  //@Constraints.Required
  public String firstName;
  
  //@Constraints.Required
  public String lastName;
  
  
  @OneToOne(cascade = CascadeType.PERSIST)
  @JsonIgnore
  public Session session;

  public int age;
  
  public boolean isAdmin = false;

  @Constraints.Required
  @Constraints.Email
  public String mail;
  
  @Constraints.Required
  public String password;

  @ManyToMany(cascade = CascadeType.PERSIST)
  @JsonIgnore
  @JoinTable(       name = "user_fav_videos",
  					joinColumns = @JoinColumn(name = "user_id"), 
  					inverseJoinColumns = @JoinColumn(name = "video_id"))
  public List<Video> favouriteVideos = new ArrayList<Video>();
  
  @ManyToMany(cascade = CascadeType.PERSIST)
  @JsonIgnore
  @JoinTable(       name = "user_fav_series",
  					joinColumns = @JoinColumn(name = "user_id"), 
  					inverseJoinColumns = @JoinColumn(name = "series_id"))
  public List<Series> favouriteSeries =  new ArrayList<Series>();
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy="author")
  @JsonIgnore
  public List<Series> serieses = new ArrayList<Series>();
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy="author")
  @JsonIgnore
  public List<Video> videos = new ArrayList<Video>();
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy="author")
  @JsonManagedReference
  public List<Comment> comments = new ArrayList<Comment>();
  
  @Formats.DateTime(pattern="dd/MM/yyyy")
  public Date registered = new Date();
  
  public static Finder<Long,User> find = new Finder<Long,User>(
    Long.class, User.class
  );

}