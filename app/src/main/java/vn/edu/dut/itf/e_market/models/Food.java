package vn.edu.dut.itf.e_market.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Food extends BaseModel implements Parcelable {
	public static final int TYPE_NORMAL=0;
	public static final int TYPE_NEW=1;
	public static final int TYPE_SALE=2;
	protected int id;
	protected String imageUrl;
	protected String name;
	protected float price;
	protected float discount;
	protected float sale;
	protected int type;
	protected int likeStatus;

	public float getRateString() {
		return rateString;
	}

	protected float rateString;
	protected String description;
	protected Category category;

	public String getDescription() {
		return description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


	public void setLikeStatus(int likeStatus) {
		this.likeStatus = likeStatus;
	}

	public int getLikeStatus() {
		return likeStatus;
	}

	public int getId() {
		return id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}

	public float getDiscount() {
		return discount;
	}

	public float getSale() {
		return sale;
	}

	public int getType() {
		return type;
	}


	public Food() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.imageUrl);
		dest.writeString(this.name);
		dest.writeFloat(this.price);
		dest.writeFloat(this.discount);
		dest.writeFloat(this.sale);
		dest.writeInt(this.type);
		dest.writeInt(this.likeStatus);
		dest.writeFloat(this.rateString);
		dest.writeString(this.description);
		dest.writeParcelable(this.category, flags);
	}

	protected Food(Parcel in) {
		this.id = in.readInt();
		this.imageUrl = in.readString();
		this.name = in.readString();
		this.price = in.readFloat();
		this.discount = in.readFloat();
		this.sale = in.readFloat();
		this.type = in.readInt();
		this.likeStatus = in.readInt();
		this.rateString = in.readFloat();
		this.description = in.readString();
		this.category = in.readParcelable(Category.class.getClassLoader());
	}

	public static final Creator<Food> CREATOR = new Creator<Food>() {
		@Override
		public Food createFromParcel(Parcel source) {
			return new Food(source);
		}

		@Override
		public Food[] newArray(int size) {
			return new Food[size];
		}
	};
}
