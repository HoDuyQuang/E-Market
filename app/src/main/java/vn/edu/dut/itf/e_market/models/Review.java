package vn.edu.dut.itf.e_market.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Review extends BaseModel {
	String id;
	String imageUrl;
	String name;
	@SerializedName("created_at")
	Date date;
	String title;
	String content;
	int categoryId;
	int collegeId;
	String phone;
	String address;

	int like;
	int comment;
	int likeStatus;

	public Review(String id, String name, Date date, String title, String content, int like, int likeStatus) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.title = title;
		this.content = content;
		this.like = like;
		this.likeStatus = likeStatus;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public ArrayList<Review> listComments;

	public ArrayList<Review> getListComments() {
		return listComments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public int isLikeStatus() {
		return likeStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
