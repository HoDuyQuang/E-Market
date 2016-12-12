package vn.edu.dut.itf.e_market.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class District extends BaseModel {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("listDistricts")
    private ArrayList<College> listColleges = new ArrayList<>();

    public District(){

    }
    public District(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<College> getListColleges() {
        return listColleges;
    }

    public void setListColleges(ArrayList<College> listColleges) {
        this.listColleges = listColleges;
    }

    public class ProvinceTable {
        public static final String TABLE_NAME = "District";
        public static final String ID = "id";
        public static final String NAME = "name";
    }
}
