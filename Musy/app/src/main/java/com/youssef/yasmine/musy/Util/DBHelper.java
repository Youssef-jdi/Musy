package com.youssef.yasmine.musy.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.youssef.yasmine.musy.Model.Track;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MusyBD";
    private static final String TABLE_TRACKS = "tracks";
    private  static final String KEY_ID = "id";
    public DBHelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("create table Track(id TEXT );");

        String CREATE_TABLE = "CREATE TABLE " + TABLE_TRACKS + "("
                + KEY_ID + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TABLE_TRACKS");
        onCreate(db);
    }

  /*  public void insert(String name) {
        long rowId = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", name);


            rowId = db.insert("Track", null, contentValues);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   public void insert(String name)   {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ID, name);
        long newRowId = db.insert(TABLE_TRACKS,null, cValues);
        db.close();
    }


    public ArrayList<Track> getTracks(){
        ArrayList<Track> tracks= new ArrayList<>();
        SQLiteDatabase db=getWritableDatabase();
        String query= "SELECT * FROM " + TABLE_TRACKS ;

        Cursor c= db.rawQuery(query,null);
        c.moveToFirst();
        while (c.moveToNext()){
            if(c.getString(c.getColumnIndex("id"))!=null){
                Track track= new Track();
                track.setId(c.getString(c.getColumnIndex("id")));
                tracks.add(track);
            }
        }
        // c.moveToNext();
        db.close();
        return tracks;
    }

    public void deleteall(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_TRACKS);
        db.close();
    }

    public void delete(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TRACKS,"id = ?",new String[]{id});
    }


}
