package vn.edu.dut.itf.e_market.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Province extends BaseModel {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("listDistricts")
    private ArrayList<District> listDistricts = new ArrayList<>();

    public Province(){

    }
    public Province(String id, String name) {
        this.id = id;
        this.name = name;
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

    public ArrayList<District> getListDistricts() {
        return listDistricts;
    }

    public void setListDistricts(ArrayList<District> listDistricts) {
        this.listDistricts = listDistricts;
    }

    public class ProvinceTable {
        public static final String TABLE_NAME = "Province";
        public static final String ID = "id";
        public static final String NAME = "name";
    }
}
