package vn.edu.dut.itf.e_market.models;

public class FoodDetail extends BaseModel {

	private String phone;

	public int bookmarkCount;
	public int reviewCount;
	public int imageCount;
	public Food food;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getBookmarkCount() {
		return bookmarkCount;
	}
	public int getReviewCount() {
		return reviewCount;
	}
	public int getImageCount() {
		return imageCount;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
}
