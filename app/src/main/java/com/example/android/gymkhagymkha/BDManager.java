package com.example.android.gymkhagymkha;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BDManager {

	// Variables que contienen los campos de las bases de datos
	public static final String TABLE_LOGIN = "login";

	public static final String CN_ID = "_id";
	public static final String CN_USER_ID = "user_id";
	public static final String CN_USER = "username";
	public static final String CN_FIRSTNAME = "firstname";
	public static final String CN_LASTNAME = "lastname";
	public static final String CN_EMAIL = "email";
	
	// Variable para crear las tablas necesarias
	public static final String CREATE_TABLE = "create table " + TABLE_LOGIN
			+ " (" + CN_ID + " integer primary key autoincrement," + CN_USER_ID + " integer," + CN_USER
			+ " text," + CN_FIRSTNAME + " text," + CN_LASTNAME + " text," + CN_EMAIL + " text)";

	private BDHelper helper;
	private SQLiteDatabase bd;

	public BDManager(Context context) {

		helper = new BDHelper(context);
		bd = helper.getWritableDatabase();
	}

	// Método para insertar el login en la base de datos
	public void login(int id, String username, String firstname, String lastname, String email) {

		ContentValues valores = new ContentValues();

		valores.put(CN_USER_ID, id);
		valores.put(CN_USER, username);
		valores.put(CN_FIRSTNAME, firstname);
		valores.put(CN_LASTNAME, lastname);
		valores.put(CN_EMAIL, email);

		bd.insert(TABLE_LOGIN, null, valores);
	}

	// Método que te devuelve el contenido de la tabla "login"
	public Cursor cursorLogin() {

		String[] columnas = new String[] { CN_ID, CN_USER_ID , CN_FIRSTNAME, CN_LASTNAME };
		return bd.query(TABLE_LOGIN, columnas, null, null, null, null, null);
	}

	// Método para borrar el campo de la base de datos al cerrar sesión
	// Para que la siguiente vez te pida de nuevo el login
	public void borrarLogin(int id) {
		bd.delete(TABLE_LOGIN, CN_ID + "=" + id , null);
	}

}
