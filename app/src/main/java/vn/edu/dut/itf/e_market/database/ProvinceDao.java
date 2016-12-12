package vn.edu.dut.itf.e_market.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dut.itf.e_market.models.College;
import vn.edu.dut.itf.e_market.models.District;


public class ProvinceDao {
	DBHelper dbHelper;

	public ProvinceDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public List<District> getDistricts() {
		SQLiteDatabase db = dbHelper.open();
		List<District> districtList = new ArrayList<>();
		// Make query
		String query = " SELECT * " +
				" FROM " +
				District.ProvinceTable.TABLE_NAME;

		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			District entity;
			do {
				entity = new District();
				entity.setId(cursor.getInt(cursor.getColumnIndex(District.ProvinceTable.ID)));
				entity.setName(cursor.getString(cursor.getColumnIndex(District.ProvinceTable.NAME)));
//				entity.setListColleges(getDistricts(entity.getId()));
				districtList.add(entity);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return districtList;
	}
	public List<College> getColleges() {
		SQLiteDatabase db = dbHelper.open();
		List<College> districtList = new ArrayList<>();
		// Make query
		String query = " SELECT * " +
				" FROM " +
				College.DistrictTable.TABLE_NAME;

		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			College entity;
			do {
				entity = new College();
				entity.setId(cursor.getInt(cursor.getColumnIndex(College.DistrictTable.ID)));
				entity.setName(cursor.getString(cursor.getColumnIndex(College.DistrictTable.NAME)));
//				entity.setListColleges(getDistricts(entity.getId()));
				districtList.add(entity);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return districtList;
	}

	ArrayList<College> getDistricts(String provinceId) {
		ArrayList<College> colleges = new ArrayList<>();
		SQLiteDatabase db = dbHelper.open();
		// Make query
		String query = " SELECT * " + " FROM " + College.DistrictTable.TABLE_NAME + " WHERE " +
				College.DistrictTable.PROVINCE_ID + "='" + provinceId+"'";

		// db.query(District.DistrictTable.TABLE_NAME, null,
		// District.DistrictTable.PROVINCE_ID + "=?",
		// new String[] { provinceId + "" }, null, null, null);
		Cursor cursor = db.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			College entity;
			do {
				entity = new College();
				entity.setId(cursor.getInt(cursor.getColumnIndex(College.DistrictTable.ID)));
				entity.setName(cursor.getString(cursor.getColumnIndex(College.DistrictTable.NAME)));
				entity.setLat(cursor.getDouble(cursor.getColumnIndex(College.DistrictTable.LAT)));
				entity.setLng(cursor.getDouble(cursor.getColumnIndex(College.DistrictTable.LONG)));
				colleges.add(entity);
			} while (cursor.moveToNext());
			cursor.close();
		}
		return colleges;
	}

	public long saveDistrict(List<District> list) {
		long count = 0;

		SQLiteDatabase db = dbHelper.open();
		db.beginTransaction();
		try {
			for (District district : list) {

				ContentValues values = new ContentValues();
				values.put(District.ProvinceTable.ID, district.getId());
				values.put(District.ProvinceTable.NAME, district.getName());
				count = db.insert(District.ProvinceTable.TABLE_NAME, null, values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.d("Province", e.getMessage());
		} finally {
			db.endTransaction();
			db.close();
		}

		return count;
	}

	public long saveCollege(List<College> list) {
		long count = 0;

		SQLiteDatabase db = dbHelper.open();
		db.beginTransaction();
		try {

				for (College college : list) {
					ContentValues valueDistrict = new ContentValues();
					valueDistrict.put(College.DistrictTable.ID, college.getId());
					valueDistrict.put(College.DistrictTable.NAME, college.getName());
					valueDistrict.put(College.DistrictTable.LAT, college.getLat());
					valueDistrict.put(College.DistrictTable.LONG, college.getLng());
					valueDistrict.put(College.DistrictTable.PROVINCE_ID, college.getDistrictId());
					count = db.insert(College.DistrictTable.TABLE_NAME, null, valueDistrict);
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
			count = db.delete(District.ProvinceTable.TABLE_NAME, null, null);
			count += db.delete(College.DistrictTable.TABLE_NAME, null, null);
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
