package vn.edu.dut.itf.e_market.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category extends BaseModel implements Parcelable {
	@SerializedName("id")
	private	int id;
	@SerializedName("category")
	private	String category;

	public static Creator<Category> CREATOR = new Creator<Category>() {
		public Category createFromParcel(Parcel source) {
			return new Category(source);
		}

		public Category[] newArray(int size) {
			return new Category[size];
		}
	};

	public int getId() {
		return id;
	}

	public String getCategory() {
		return category;
	}

	public Category(int id, String category) {
		super();
		this.id = id;
		this.category = category;
	}

	private Category(Parcel in) {
		this.id = in.readInt();
		this.category = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.category);
	}

}
