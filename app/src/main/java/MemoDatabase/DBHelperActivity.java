package MemoDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelperActivity extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String MEMO_COLUMN_ID = "id";
    public static final String MEMO_COLUMN_TAG = "tag";
    public static final String MEMO_COLUMN_CONTENT = "content";

    public DBHelperActivity(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
        android.util.Log.d("ASHU","DBHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table memo " +
                        "(id integer primary key, tag text,content text)"
        );
        android.util.Log.d("ASHU","onCreate DBHelper");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS memo");
        onCreate(db);
        android.util.Log.d("ASHU","onUpgrade DBHelper");
    }

    public boolean insertMemo (String tag, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tag",tag);
        contentValues.put("content", content);
        db.insert("memo", null, contentValues);
        db.close();
        android.util.Log.d("ASHU","insertMemo DBHelper");
        android.util.Log.d("ASHU","insertMemo DBHelper tag : "+tag+" and content : "+content);
        return true;
    }

    /*public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, MEMO_COLUMN_TAG);
        db.close();
        android.util.Log.d("ASHU","numberOfRows DBHelper");
        return numRows;
    }
    */

    public Cursor getData (String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        res = db.rawQuery("select * from memo where tag=\""+tag+"\"",null);
        android.util.Log.d("ASHU","getData DBHelper");
        return res;
    }

    public ArrayList<String> getAllMemo() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from memo",null);
        res.moveToFirst();
        while(res.isAfterLast()==false) {
            array_list.add(res.getString(res.getColumnIndex(MEMO_COLUMN_TAG)));
            android.util.Log.d("ASHU","getAllMemo DBHelper Tag : "+res.getString(res.getColumnIndex(MEMO_COLUMN_TAG)));
            android.util.Log.d("ASHU","getAllMemo DBHelper Content : "+res.getString(res.getColumnIndex(MEMO_COLUMN_CONTENT)));
            res.moveToNext();
        }

        db.close();
        android.util.Log.d("ASHU","getAllMemo DBHelper");
        return array_list;
    }
}

