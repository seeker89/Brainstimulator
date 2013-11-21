package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Session extends Model {
	private static final long serialVersionUID = 381654729L;

	@Id
	@Constraints.Min(10)
	public Long id;
	
	@OneToOne(mappedBy="session")
	public User connectedUser;

	public Date connectedAt;

	public String cookie;

	public Session(User connectedUser) {
		this.connectedUser = connectedUser;
	}

	static public String createSession(User user) {
		Session session = new Session(user);
		session.cookie = Math.random() + user.mail;
		session.connectedAt = new Date();
		user.session = session;
		user.save();
		return session.cookie;
	}

	static public boolean deleteSession(String cookie) {
		Session session = Session.find.where().eq("cookie", cookie).findUnique();
		if (session != null) {
			session.delete();
			return true;
		} else {
			return false;
		}
	}

	public static Finder<Long, Session> find = new Finder<Long, Session>(Long.class, Session.class);

	public static User userConnected(String cookie, boolean renew) {
		Session session = Session.find.where().eq("cookie", cookie).findUnique();

		if (session != null)   {
			if (session.connectedAt.getTime() + 1000 * 60 * 10 > System.currentTimeMillis()) {
				if (renew){
					session.connectedAt = new Date();
					session.save();
					session.connectedUser.save();
				}
				return session.connectedUser;
			} else {
				session.connectedUser.session.delete();
				session.connectedUser.save();
				//session.delete();
			}
		}
		return null;
	}
}
