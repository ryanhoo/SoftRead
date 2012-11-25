package com.iedgeco.ryan.softread.models;

import java.util.Date;

import com.iedgeco.ryan.softread.config.Config;
import com.iedgeco.ryan.softread.utils.Utils;

public class SoftNews {

	private int id;
	private int authorId;
	private String news_en;
	private String news_zn;
	private String category;
	private String tags;
	private String picture;
	private String description;
	private Date creationDate;

	public SoftNews() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getNews_en() {
		return news_en;
	}

	public void setNews_en(String news_en) {
		this.news_en = news_en;
	}

	public String getNews_zn() {
		return news_zn;
	}

	public void setNews_zn(String news_zn) {
		this.news_zn = news_zn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer("news:\n");
		sb.append("\t id: " + id + "\n")
			.append("\t authorId: " + authorId + "\n")
			.append("\t news_en: " + news_en + "\n")
			.append("\t news_zn: " + news_zn + "\n")
			.append("\t category: " + category + "\n")
			.append("\t tags: " + tags + "\n")
			.append("\t picture: " + picture + "\n")
			.append("\t description: " + description + "\n");
		try {
			 sb.append("\t creationDate: " + Utils.dateToString(creationDate, Config.LOCALE_DATE_FORMAT) + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
