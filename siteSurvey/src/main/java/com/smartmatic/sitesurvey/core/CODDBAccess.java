package com.smartmatic.sitesurvey.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CODDBAccess {
	
	private Context mContext;
	private SQLiteDatabase mDb;
	private CODDBHelper mDbHelper;

	public CODDBAccess(Context c) {
		mContext = c;
	}

	public CODDBAccess open() throws SQLException {
		mDbHelper = new CODDBHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	public void getPlaces(){
		int position;
		int meters;
		int timeInSeconds;
		String statement = "Select * from place order by edited";
		Cursor queryCursor = mDb.rawQuery(statement, null);
		//if (queryCursor == null){
		//	return null;
		//}
		queryCursor.moveToFirst();
		while(queryCursor.isAfterLast() == false) {
			position = queryCursor.getInt(queryCursor.getColumnIndexOrThrow("sposition"));
			meters = queryCursor.getInt(queryCursor.getColumnIndexOrThrow("smeters"));
			timeInSeconds = queryCursor.getInt(queryCursor.getColumnIndexOrThrow("stime"));
			/*switch (position){
			case 1:
				Scores.goldSeconds = timeInSeconds;
				Scores.goldMeters = meters;
				break;
			case 2:
				Scores.silverSeconds = timeInSeconds;
				Scores.silverMeters = meters;
				break;
			case 3:
				Scores.bronzeSeconds = timeInSeconds;
				Scores.bronzeMeters = meters;
				break;
			}*/
			queryCursor.moveToNext();
		}
		queryCursor.close();
	}
	
	public void saveScores(){
		mDb.beginTransaction();
		try{
			
			mDb.execSQL("DELETE FROM myscore");
			ContentValues values = new ContentValues();	
			values.put("sposition", 1);
			/*values.put("smeters", Scores.goldMeters);
			values.put("stime", Scores.goldSeconds);
			mDb.insert("myscore", null, values);
			values.clear();
			values.put("sposition", 2);
			values.put("smeters", Scores.silverMeters);
			values.put("stime", Scores.silverSeconds);
			mDb.insert("myscore", null, values);
			values.clear();
			values.put("sposition", 3);
			values.put("smeters", Scores.bronzeMeters);
			values.put("stime", Scores.bronzeSeconds);
			mDb.insert("myscore", null, values);*/
			mDb.setTransactionSuccessful();
		}finally{
			mDb.endTransaction();
		}
		
	}
	
	/*public Hashtable<String,Challenge> getChallenges(){
		Challenge challenge = null;
		Hashtable<String,Challenge> res = new Hashtable<String,Challenge>();
		String statement = "Select * from challenge order by cchallengername";
		Cursor queryCursor = mDb.rawQuery(statement, null);
		if (queryCursor == null){
			return null;
		}
		queryCursor.moveToFirst();
		while(queryCursor.isAfterLast() == false) {
			challenge = new Challenge();
			challenge.total = queryCursor.getInt(queryCursor.getColumnIndexOrThrow("ctotal"));
			challenge.wins =  queryCursor.getInt(queryCursor.getColumnIndexOrThrow("cwins"));
			challenge.Challenger = queryCursor.getString(queryCursor.getColumnIndexOrThrow("cchallengername"));
			res.put(challenge.Challenger, challenge);
			queryCursor.moveToNext();
		}
		queryCursor.close();
		return res;
	}*/
	
	/*public void saveChallenge(Hashtable<String,Challenge> challenges, String challenger, boolean won){
		Challenge cha = challenges.get(challenger);
		if (won){
			cha.total++;
			cha.wins++;
		} else {
			cha.total++;
		}
		mDb.beginTransaction();
		try{
			mDb.execSQL("DELETE FROM challenge WHERE cchallengername='" + challenger + "'");
			mDb.execSQL("INSERT INTO challenge (cchallengername,ctotal,cwins) VALUES ('" + cha.Challenger + "'," + cha.total + "," + cha.wins + ")");
			mDb.setTransactionSuccessful();
		}finally{
			mDb.endTransaction();
		}
	}*/
	
	
	
}