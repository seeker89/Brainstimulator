package models;

import java.util.*;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.db.ebean.*;
import play.data.validation.*;

@Entity 
public class Category extends Model {
	
  public Category(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}


private static final long serialVersionUID = 2671872337319214628L;

  @Id
  @Constraints.Min(10)
  @ManyToOne
  public Long id;
  
  @Constraints.Required
  public String title;

  public String description;
  
  @ManyToOne
  @JsonIgnore
  public Category parent;

  @OneToMany(cascade=CascadeType.ALL, mappedBy="parent")
  public List<Category> subCategories = new ArrayList<Category>();
  
  public static Finder<Long,Category> find = new Finder<Long,Category>(
    Long.class, Category.class
  ); 
  
  public static Category getRoot(){
	  return Category.find.ref((long) 1);
  }
}