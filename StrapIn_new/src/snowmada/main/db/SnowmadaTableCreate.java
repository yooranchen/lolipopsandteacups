package snowmada.main.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SnowmadaTableCreate {

	private static final String DATABASE_SNOMADA = "CREATE TABLE " + TableConstantName.TABLE_NAME + " (" + TableConstantName.ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " + TableConstantName.SENDER_ID + " TEXT, " + TableConstantName.RECEIVER_ID + " TEXT, " + TableConstantName.NAME + " TEXT, " + TableConstantName.MESSAGE + " TEXT, " + TableConstantName.TIME + "	TEXT);";

	public static void onCreate(SQLiteDatabase database) {
		Log.v("SendQueueTable OnCreate", "Reached Here");
		database.beginTransaction();
		try {
			database.execSQL(DATABASE_SNOMADA);
			database.setTransactionSuccessful();
		} catch (SQLException e) {
			throw e;
		} finally {
			database.endTransaction();
		}
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(SnowmadaTableCreate.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TableConstantName.TABLE_NAME);
		onCreate(database);
	}
}