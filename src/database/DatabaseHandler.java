package database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "imageManager";
 
    // Contacts table name
    private static final String TABLE_IMAGE = "images";
 
    // Contacts Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_UPLOAD = "upload";
    
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
                + KEY_NAME + " TEXT PRIMARY KEY," + KEY_COMMENT + " TEXT,"
                + KEY_UPLOAD + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
 
        onCreate(db);
    }
    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    public void addImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, image.get_name());
 
        db.insert(TABLE_IMAGE, null, values);
        db.close(); 
    }
     
     
    public List<Image> getAllImages() {
        List<Image> imagetList = new ArrayList<Image>();
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGE;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        if (cursor.moveToFirst()) {
            do {
            	Image image = new Image();
            	image.set_name(cursor.getString(0));
            	image.set_comment(cursor.getString(1));
            	image.set_upload(cursor.getString(2));
            	imagetList.add(image);
            } while (cursor.moveToNext());
        }
 
        return imagetList;
    }
 
    public int updateComment(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_COMMENT, image.get_comment());
 
        return db.update(TABLE_IMAGE, values, KEY_NAME + " = ?",
                new String[] { String.valueOf(image.get_name()) });
    }
    
    public Image getImage(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_IMAGE, new String[] { KEY_NAME,
                KEY_COMMENT, KEY_UPLOAD }, KEY_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        Image image = new Image(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        return image;
        
    }
 
    
}