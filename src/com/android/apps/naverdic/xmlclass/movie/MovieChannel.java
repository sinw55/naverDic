package com.android.apps.naverdic.xmlclass.movie;

import java.util.ArrayList;
import java.util.List;

public class MovieChannel {
	private String title;
	private String link;
	private String description;
	private String lastBuildDate;
	private int total;
	private int start;
	private int display;
	private List<MovieItem> items = new ArrayList<MovieItem>();
	
	public void addItem(MovieItem item) {
		items.add(item);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLastBuildDate() {
		return lastBuildDate;
	}
	public void setLastBuildDate(String lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	public List<MovieItem> getItems() {
		return items;
	}
	public void setItems(List<MovieItem> items) {
		this.items = items;
	}
}
