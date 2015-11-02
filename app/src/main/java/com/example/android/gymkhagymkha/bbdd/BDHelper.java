package com.example.android.gymkhagymkha.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// No haría falta tocar esta clase.
// Para modificaciones de la base de datos en BDManager.java
public class BDHelper extends SQLiteOpenHelper {

	private static final String BD_NAME = "gymkhagymkha.sqlite";
	private static final int BD_VERSION = 1;
	
	public BDHelper(Context context) {
		super(context, BD_NAME, null, BD_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BDManager.CREATE_TABLE_LOGIN);
		db.execSQL(BDManager.CREATE_TABLE_EVENTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS agenda"); 
		this.onCreate(db);
	}

}
