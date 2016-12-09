package vn.edu.dut.itf.e_market.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper {

	/******* if debug is set true then it will show all Logcat message ***/
	public static final boolean DEBUG = true;
	/**
	 * Logcat TAG
	 */
	private static final String LOG_TAG = "DBHelper";
	private static final String DATABASE_NAME = "restaurant.db";
	private static String DATABASE_PATH = "";
	private final Context context;
	/**
	 * Database Version (Increase one if want to also upgrade your database)
	 * started at 1
	 */
	private static final int DATABASE_VERSION = 1;
	/**
	 * Used to open database in synchronized way
	 */
	private static InnerDatabaseHelper InnerDBHelper = null;

	public DBHelper(Context context) {

		if (InnerDBHelper == null)
			InnerDBHelper = new InnerDatabaseHelper(context);
		this.context = context;
		DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).toString();
	}

	/**
	 * Main Database creation INNER class
	 * 
	 */
	private static class InnerDatabaseHelper extends SQLiteOpenHelper {
		public InnerDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(
						"CREATE TABLE `Category` (`id` INTEGER NOT NULL,`name` TEXT,PRIMARY KEY(`id`));");
				db.execSQL(
						"CREATE TABLE `College` (\n" +
								"\t`id`\tTEXT NOT NULL,\n" +
								"\t`name`\tTEXT NOT NULL,\n" +
								"\t`lat`\tREAL NOT NULL DEFAULT 0,\n" +
								"\t`long`\tREAL NOT NULL DEFAULT 0,\n" +
								"\t`district_id`\tINTEGER NOT NULL,\n" +
								"\tPRIMARY KEY(`id`)\n" +
								");");
				db.execSQL(
						"CREATE TABLE `District` (\n" +
								"\t`id`\tTEXT NOT NULL,\n" +
								"\t`name`\tTEXT NOT NULL,\n" +
								"\t`lat`\tREAL NOT NULL DEFAULT 0,\n" +
								"\t`long`\tREAL NOT NULL,\n" +
								"\tPRIMARY KEY(`id`)\n" +
								");");
			} catch (Exception exception) {
				Log.i(LOG_TAG, "Exception onCreate() exception");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			// db.execSQL("DROP TABLE IF EXISTS Cart");

			// recreate database
			onCreate(db);
		}

	} // Inner class closed

	/**
	 * Open database for insert,update,delete in synchronized manner
	 * 
	 * @throws SQLException
	 */
	public synchronized SQLiteDatabase open() throws SQLException {
		return InnerDBHelper.getWritableDatabase();
	}

	// Create a empty database on the system
	public void createDatabase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			Log.v("DB Exists", "db exists");
			// By calling this method here onUpgrade will be called on a
			// writable database, but only if the version number has been
			// bumped
			// onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
		}
		boolean dbExist1 = checkDataBase();
		if (!dbExist1) {
			InnerDBHelper.getReadableDatabase();
			try {
				InnerDBHelper.close();
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	// Check database already exist or not
	private boolean checkDataBase() {
		boolean checkDB = false;
		try {
			String myPath = DATABASE_PATH;
			File dbFile = new File(myPath);
			checkDB = dbFile.exists();
		} catch (SQLiteException e) {
		}
		return checkDB;
	}

	// Copies your database from your local assets-folder to the just created
	// empty database in the system folder
	private void copyDataBase() throws IOException {
		String outFileName = DATABASE_PATH;
		OutputStream myOutput = new FileOutputStream(outFileName);
		InputStream myInput = context.getAssets().open(DATABASE_NAME);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myInput.close();
		myOutput.flush();
		myOutput.close();
	}

	public void db_delete() {
		File file = new File(DATABASE_PATH);
		if (file.exists()) {
			file.delete();
			System.out.println("delete database file.");
		}
	}

	public SQLiteDatabase openDatabase() throws SQLException {
		return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
	}
}
