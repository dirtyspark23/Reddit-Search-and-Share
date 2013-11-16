package com.intravita.android.rss.model;

public class Post {
	public String thumbnail;
	public String title;
	public String author;
	
	
	public Post() {
		super();
	}
	/**
	 * 
	 * @param icon
	 * @param title
	 * @param author
	 */
	public Post(String icon, String title, String author) {
		super();
		this.thumbnail = icon;
		this.title = title;
		this.author = author;
	}	
}
