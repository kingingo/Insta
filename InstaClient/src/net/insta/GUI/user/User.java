package net.insta.GUI.user;

import javafx.beans.property.*;
import javafx.scene.control.Button;

public class User {

	private final StringProperty instaUser;
	private final StringProperty done;

	public User(String a, String x) {
		this.instaUser = new SimpleStringProperty(a);
		this.done = new SimpleStringProperty(x);

	}

	public String getInstaUser() {
		return instaUser.get();
	}
	
	 public void setInstaUser(String uName) {
		 instaUser.set(uName);
	 }
	
	 public String getDone() {
		 return done.get();
	 }
	
	 public void setDone(String sDone) {
		 done.set(sDone);
	 }

}
