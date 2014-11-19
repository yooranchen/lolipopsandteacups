package snowmada.main.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SnowmadaDbHelper extends SQLiteOpenHelper {

	public SnowmadaDbHelper(Context context) {
		super(context, TableConstantName.DATABASE_NAME, null, TableConstantName.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("SQLiteOpenHelper OnCreate", "Reached Here");
		SnowmadaTableCreate.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		SnowmadaTableCreate.onUpgrade(db, oldVersion, newVersion);		
	}
}
