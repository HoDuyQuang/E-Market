package vn.edu.dut.itf.e_market.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dut.itf.e_market.models.District;
import vn.edu.dut.itf.e_market.models.Province;


public class ProvinceDao {
	DBHelper dbHelper;

	public ProvinceDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public List<Province> getProvinces() {
		SQLiteDatabase db = dbHelper.open();
		List<Province> provinceList = new ArrayList<>();
		// Make query
		String query = " SELECT * " +
				" FROM " +
				Province.ProvinceTable.TABLE_NAME;

		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			Province entity;
			do {
				entity = new Province();
				entity.setId(cursor.getString(cursor.getColumnIndex(Province.ProvinceTable.ID)));
				entity.setName(cursor.getString(cursor.getColumnIndex(Province.ProvinceTable.NAME)));
				entity.setListDistricts(getDistricts(entity.getId()));
				provinceList.add(entity);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return provinceList;
	}

	ArrayList<District> getDistricts(String provinceId) {
		ArrayList<District> districts = new ArrayList<>();
		SQLiteDatabase db = dbHelper.open();
		// Make query
		String query = " SELECT * " + " FROM " + District.DistrictTable.TABLE_NAME + " WHERE " +
				District.DistrictTable.PROVINCE_ID + "='" + provinceId+"'";

		// db.query(District.DistrictTable.TABLE_NAME, null,
		// District.DistrictTable.PROVINCE_ID + "=?",
		// new String[] { provinceId + "" }, null, null, null);
		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			District entity;
			do {
				entity = new District();
				entity.setId(cursor.getString(cursor.getColumnIndex(District.DistrictTable.ID)));
				entity.setName(cursor.getString(cursor.getColumnIndex(District.DistrictTable.NAME)));
				entity.setLat(cursor.getDouble(cursor.getColumnIndex(District.DistrictTable.LAT)));
				entity.setLng(cursor.getDouble(cursor.getColumnIndex(District.DistrictTable.LONG)));
				districts.add(entity);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return districts;
	}

	public long save(List<Province> list) {
		long count = 0;

		SQLiteDatabase db = dbHelper.open();
		db.beginTransaction();
		try {
			for (Province province : list) {

				ContentValues values = new ContentValues();
				values.put(Province.ProvinceTable.ID, province.getId());
				values.put(Province.ProvinceTable.NAME, province.getName());
				count = db.insert(Province.ProvinceTable.TABLE_NAME, null, values);

				// Values to insert district
				for (District district : province.getListDistricts()) {
					ContentValues valueDistrict = new ContentValues();
					valueDistrict.put(District.DistrictTable.ID, district.getId());
					valueDistrict.put(District.DistrictTable.NAME, district.getName());
					valueDistrict.put(District.DistrictTable.LAT, district.getLat());
					valueDistrict.put(District.DistrictTable.LONG, district.getLng());
					valueDistrict.put(District.DistrictTable.PROVINCE_ID, province.getId());
					count = db.insert(District.DistrictTable.TABLE_NAME, null, valueDistrict);
				}
			}

			// Commit
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d("Province", e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

		return count;
	}

	public int deleteAll() {
		SQLiteDatabase db = dbHelper.open();
		db.beginTransaction();
		int count = 0;
		try {
			count = db.delete(Province.ProvinceTable.TABLE_NAME, null, null);
			count += db.delete(District.DistrictTable.TABLE_NAME, null, null);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d("Province", e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}
		return count;
	}
}
