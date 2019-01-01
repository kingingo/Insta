package net.insta.GUI;

import javafx.beans.property.SimpleStringProperty;

public class Comment {
	private final SimpleStringProperty comments;
	

	public Comment(String a) {
		this.comments = new SimpleStringProperty(a);
		
	}

	public String getComments() {
		return comments.get();
	}
	
	 public void setComments(String c) {
		 comments.set(c);
	 }
	
	


}
