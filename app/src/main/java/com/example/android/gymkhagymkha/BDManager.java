package com.example.android.gymkhagymkha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BDManager {

	// Variables que contienen los campos de las bases de datos
	public static final String TABLE_LOGIN = "login";
	public static final String TABLE_EVENT = "eventos";

	public static final String CN_ID = "_id";
	public static final String CN_USER_ID = "user_id";
	public static final String CN_USER = "username";
	public static final String CN_FIRSTNAME = "firstname";
	public static final String CN_LASTNAME = "lastname";
	public static final String CN_EMAIL = "email";
	public static final String CN_IDADMINISTRADOR = "idAdministrador";
	public static final String CN_IDCENTRO = "idCentro";

	public static final String CN_IDEVENT = "event_id";
	public static final String CN_EVENT_DESCRIPTION = "event_description";
	public static final String CN_EVENT_NAME = "event_name";
	public static final String CN_EVENT_HOUR = "event_hour";

	// Variable para crear las tablas necesarias
	public static final String CREATE_TABLE_LOGIN = "create table " + TABLE_LOGIN
			+ " (" + CN_ID + " integer primary key autoincrement," + CN_USER_ID + " integer," + CN_USER
			+ " text," + CN_FIRSTNAME + " text," + CN_LASTNAME + " text," + CN_EMAIL + " text," + CN_IDADMINISTRADOR + " integer,"+CN_IDCENTRO+" integer)";
	public static final String CREATE_TABLE_EVENTS = "create table " + TABLE_EVENT
			+ " (" + CN_ID + " integer primary key autoincrement," + CN_IDEVENT + " integer," + CN_EVENT_DESCRIPTION
			+ " text,"+ CN_EVENT_NAME +" text," + CN_EVENT_HOUR + " text)";

	private BDHelper helper;
	private SQLiteDatabase bd;
	ContentValues valores;

	public BDManager(Context context) {

		helper = new BDHelper(context);
		bd = helper.getWritableDatabase();
	}

	// Método para insertar el login en la base de datos
	public void login(int id, String username, String firstname, String lastname, String email,int idAdministrador, int idCentro) {

		valores = new ContentValues();

		valores.put(CN_USER_ID, id);
		valores.put(CN_USER, username);
		valores.put(CN_FIRSTNAME, firstname);
		valores.put(CN_LASTNAME, lastname);
		valores.put(CN_EMAIL, email);
		valores.put(CN_IDADMINISTRADOR, idAdministrador);
		valores.put(CN_IDCENTRO, idCentro);

		bd.insert(TABLE_LOGIN, null, valores);
	}

	public void guardarEvento(int id, String event_description, String event_name, String event_hour) {

		valores = new ContentValues();

		valores.put(CN_IDEVENT, id);
		valores.put(CN_EVENT_DESCRIPTION, event_description);
		valores.put(CN_EVENT_NAME, event_name);
		valores.put(CN_EVENT_HOUR, event_hour);

		bd.insert(TABLE_EVENT, null, valores);
	}

	// Método que te devuelve el contenido de la tabla "login"
	public Cursor cursorLogin() {

		String[] columnas = new String[] { CN_ID, CN_USER_ID , CN_FIRSTNAME, CN_LASTNAME, CN_IDADMINISTRADOR,CN_IDCENTRO};
		return bd.query(TABLE_LOGIN, columnas, null, null, null, null, null);

	}

	public Cursor cursorEventos() {

		String[] columnas = new String[] { CN_ID, CN_IDEVENT , CN_EVENT_DESCRIPTION, CN_EVENT_NAME, CN_EVENT_HOUR};
		return bd.query(TABLE_EVENT, columnas, null, null, null, null, null);
	}

	// Método para borrar el campo de la base de datos al cerrar sesión
	// Para que la siguiente vez te pida de nuevo el login
	public void borrarLogin(int id) {
		bd.delete(TABLE_LOGIN, CN_ID + "=" + id , null);
	}
	public void borrarEventos(int id) {
		bd.delete(TABLE_EVENT, null , null);
	}

}
