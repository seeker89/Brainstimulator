package models;

import java.util.*;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonBackReference;

import play.db.ebean.*;
import play.data.format.Formats;
import play.data.validation.*;

@Entity 
public class Comment extends Model {
	
  public Comment(String text, User author, Date registered) {
		super();
		this.text = text;
		this.author = author;
		this.registered = registered;
	}

private static final long serialVersionUID = 2671872337319214628L;

  @Id
  @Constraints.Min(10)
  public Long id;
  
  @Constraints.Required
  public String text;

  @ManyToOne
  @JsonBackReference
  public User author;

  @Formats.DateTime(pattern="dd/MM/yyyy")
  public Date registered = new Date();
  
  public static Finder<Long,Comment> find = new Finder<Long,Comment>(
    Long.class, Comment.class
  ); 
}