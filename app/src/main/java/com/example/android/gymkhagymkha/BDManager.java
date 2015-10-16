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
	public static final String CN_USER = "user";
	public static final String CN_PASS = "password";
	
	// Variable para crear las tablas necesarias
	public static final String CREATE_TABLE = "create table " + TABLE_LOGIN
			+ " (" + CN_ID + " integer primary key autoincrement," + CN_USER
			+ " text," + CN_PASS + " text);";

	private BDHelper helper;
	private SQLiteDatabase bd;

	public BDManager(Context context) {

		helper = new BDHelper(context);
		bd = helper.getWritableDatabase();
	}

	// Método para insertar el login en la base de datos
	public void login(String user, String pass) {

		ContentValues valores = new ContentValues();

		valores.put(CN_USER, user);
		valores.put(CN_PASS, pass);

		bd.insert(TABLE_LOGIN, null, valores);
	}

	// Método que te devuelve el contenido de la tabla "login"
	public Cursor cursorLogin() {

		String[] columnas = new String[] { CN_ID, CN_USER, CN_PASS};
		return bd.query(TABLE_LOGIN, columnas, null, null, null, null, null);
	}

	// Método para borrar el campo de la base de datos al cerrar sesión
	// Para que la siguiente vez te pida de nuevo el login
	public void borrarLogin(int id) {
		bd.delete(TABLE_LOGIN, CN_ID + "=" + id , null);
	}

}
