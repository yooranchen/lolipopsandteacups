package snowmada.main.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SnowmadaDbAdapter {

	private static Context sContext;
	private static SQLiteDatabase sDb;
	private static SnowmadaDbHelper sDbHelper;
	private static SnowmadaDbAdapter sInstance;

	private SnowmadaDbAdapter(final Context context) {
		sContext = context;
	}

	public static SnowmadaDbAdapter databaseHelperInstance(final Context context) {
		if (sInstance == null) {
			sInstance = new SnowmadaDbAdapter(context);
			open();
		}
		return sInstance;
	}

	private static void open() {
		Log.e("Database open", "Database opened");
		sDbHelper = new SnowmadaDbHelper(sContext);
		sDb = sDbHelper.getWritableDatabase();
	}

	public void close() {
		sDbHelper.close();
	}

	public long inserValue(String senderid, String receiverid, String name, String message, String time) {
		ContentValues values = new ContentValues();
		values.put(TableConstantName.SENDER_ID, receiverid);
		values.put(TableConstantName.RECEIVER_ID, receiverid);
		values.put(TableConstantName.MESSAGE, message);
		values.put(TableConstantName.TIME, time);
		try {
			sDb.beginTransaction();
			long state = sDb.insert(TableConstantName.TABLE_NAME, null, values);
			sDb.setTransactionSuccessful();
			return state;
		} catch (SQLException e) {
			throw e;
		} finally {
			sDb.endTransaction();
		}

	}
	
	public ArrayList<String> fetchValue(String id){
		ArrayList<String> arr =  new ArrayList<String>();
			String st;
			Cursor cursor = sDb.rawQuery("SELECT *  FROM  "+ TableConstantName.TABLE_NAME +"   WHERE    ("+TableConstantName.RECEIVER_ID+" = "+id+") OR ("+TableConstantName.SENDER_ID+" = "+id+")",null);		
			if(cursor.getCount()>0){
				cursor.moveToFirst();
				while(!cursor.isAfterLast()){
					st = cursor.getString(1)+"~"+cursor.getString(2)+cursor.getString(3)+cursor.getString(4)+cursor.getString(5);
					arr.add(st);
					cursor.moveToNext();
				}
			}
			cursor.close();
			return arr;
		}


}
