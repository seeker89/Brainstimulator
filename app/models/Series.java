package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

import play.db.ebean.*;
import play.data.validation.*;

@Entity 
public class Series extends Model {
	
  public Series(User author, String title, String description, String tags) {
		super();
		this.author = author;
		this.title = title;
		this.description = description;
		this.tags = tags;
	}

private static final long serialVersionUID = 2L;

  @Id
  @Constraints.Min(10)
  public Long id;
  
  @Constraints.Required
  @ManyToOne
  @JsonBackReference
  public User author;
  
  @Constraints.Required
  public String title;

  @Constraints.Required
  public String description;
  
  @Constraints.Required
  public String tags;
  
  @ManyToMany(cascade = CascadeType.PERSIST, mappedBy="serieses")
  @JsonBackReference
  public List<Video> videos = new ArrayList<Video>();

  public int views = 0;

  public int votes = 0;
  
  public static Finder<Long,Series> find = new Finder<Long,Series>(
    Long.class, Series.class
  ); 
}