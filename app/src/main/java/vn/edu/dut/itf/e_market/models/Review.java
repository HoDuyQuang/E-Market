package vn.edu.dut.itf.e_market.models;

import java.util.ArrayList;
import java.util.Date;

public class Review extends BaseModel {
	public String reviewId;
	public String name;
	public Date date;
	public String title;
	public String content;
	public int like;
	public int comment;
	protected int likeStatus;
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

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
}
