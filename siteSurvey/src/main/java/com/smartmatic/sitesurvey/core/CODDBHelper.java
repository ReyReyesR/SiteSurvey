package com.smartmatic.sitesurvey.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CODDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "COD_db";
	private static final int DB_VERSION = 1;
	private static final String CREATE_PLACE ="CREATE TABLE place (id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ " name TEXT NOT NULL,"
			+ " address TEXT,"
			+ " lon REAL NOT NULL,"
			+ " lat REAL NOT NULL,"
			+ " map INTEGER,"
			+ " lastEdited INTEGER,"
			+ " PRIMARY KEY (id))";
	
	private static final String CREATE_QUESTION ="CREATE TABLE question (id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ " name TEXT NOT NULL,"
			+ " answer TEXT,"
			+ " FOREIGN KEY (nameId) REFERENCES PLACE(id),"
			+ " status INTEGER NOT NULL)";
	
	public CODDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.beginTransaction();
		try{
			//Tables creation
			
			db.execSQL(CREATE_PLACE);
			db.execSQL(CREATE_QUESTION);
			
			//Initial data loading
			
			fillDummyData1(db);
			fillDummyData2(db);
			
			db.setTransactionSuccessful();
			
		} finally {
		     db.endTransaction();
		}
	}
	
	public void fillDummyData1(SQLiteDatabase db){
		ContentValues values = new ContentValues();
		values.put("sposition", 1);
		values.put("smeters", 0);
		values.put("stime", 0);
		db.insert("myscore", null, values);
		values.clear();
		values.put("sposition", 2);
		values.put("smeters", 0);
		values.put("stime", 0);
		db.insert("myscore", null, values);
		values.clear();
		values.put("sposition", 3);
		values.put("smeters", 0);
		values.put("stime", 0);
		db.insert("myscore", null, values);
		
	}
	
	public void fillDummyData2(SQLiteDatabase db){
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino',100,100)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino1',200,100)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino2',300,100)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino3',400,100)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino4',100,25)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino5',100,10)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino6',100,12)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino7',100,10)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino8',100,1)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino9',100,3)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino10',100,33)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino11',100,44)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino12',100,50)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino13',100,0)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino14',100,21)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino15',100,100)");
		db.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('Lino16',100,100)");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		try{
			switch (oldVersion) {
			case 1:	;
		}
			db.setTransactionSuccessful();
		} finally {
		     db.endTransaction();
		}
	}

}

	
