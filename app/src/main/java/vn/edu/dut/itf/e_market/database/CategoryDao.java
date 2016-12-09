package vn.edu.dut.itf.e_market.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.edu.dut.itf.e_market.models.Category;
import vn.edu.dut.itf.e_market.models.Category.CategoryTable;

public class CategoryDao {
    private DBHelper dbHelper;
    private Context mContext;

    public CategoryDao(Context context) {
        dbHelper = new DBHelper(context);
        this.mContext = context;
    }

    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.open();

        String query = " SELECT " +
                " " + CategoryTable.TABLE_NAME + ".* " +
                " FROM " +
                " " + CategoryTable.TABLE_NAME + " ";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            Category entity;
            do {
                entity = new Category(cursor.getInt(cursor.getColumnIndex(CategoryTable.ID)),
                        cursor.getString(cursor.getColumnIndex(CategoryTable.NAME)));
                list.add(entity);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public long save(List<Category> list) {
        long count = 0;
        Category cart;

        SQLiteDatabase db = dbHelper.open();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                cart = list.get(i);
                // Values to insert
                ContentValues values = new ContentValues();
                values.put(CategoryTable.ID, cart.getId());
                values.put(CategoryTable.NAME, cart.getCategory());
                count = db.insert(CategoryTable.TABLE_NAME, null, values);
            }

            // Commit
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
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
            count = db.delete(CategoryTable.TABLE_NAME, null, null);
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
