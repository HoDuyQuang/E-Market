package vn.edu.dut.itf.e_market.models;

import com.google.gson.annotations.SerializedName;

public class District extends BaseModel {
	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String name;
	@SerializedName("lat")
	private double lat;
	@SerializedName("long")
	private double lng;

	public District(String id, String name){
		this.id = id;
		this.name = name;
	}

	public District() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public class DistrictTable {

		public static final String TABLE_NAME = "District";
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String LAT = "lat";
		public static final String LONG = "long";
		public static final String PROVINCE_ID = "province_id";
	}
}
