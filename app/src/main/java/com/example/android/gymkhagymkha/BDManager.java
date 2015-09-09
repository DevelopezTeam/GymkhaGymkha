package com.example.android.gymkhagymkha;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BDManager {

	public static final String TABLE_NAME = "login";

	public static final String CN_ID = "_id";
	public static final String CN_USER = "user";
	public static final String CN_PASS = "password";

	/* */

	public static final String CREATE_TABLE = "create table " + TABLE_NAME
			+ " (" + CN_ID + " integer primary key autoincrement," + CN_USER
			+ " text," + CN_PASS + " text);";

	private BDHelper helper;
	private SQLiteDatabase bd;

	public BDManager(Context context) {

		helper = new BDHelper(context);
		bd = helper.getWritableDatabase();

	}

	public void login(String user, String pass) {

		ContentValues valores = new ContentValues();

		valores.put(CN_USER, user);
		valores.put(CN_PASS, pass);

		bd.insert(TABLE_NAME, null, valores);

	}

	public Cursor cursorLogin() {

		String[] columnas = new String[] { CN_ID, CN_USER, CN_PASS};

		return bd.query(TABLE_NAME, columnas, null, null, null, null, null);
	}


	public void borrarContacto(int id) {

		bd.delete(TABLE_NAME, CN_ID + "=" + id , null);

	}

}
