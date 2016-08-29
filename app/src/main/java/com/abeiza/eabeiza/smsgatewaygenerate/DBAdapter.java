package com.abeiza.eabeiza.smsgatewaygenerate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by eabeiza on 7/23/2016.
 */
public class DBAdapter {
    //field from ms_product
    public static final String KEY_ROWID = "_id";
    public static final String KEY_IDPRODUCT = "id_product";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_NAMAPRODUCT = "nama_product";
    public static final String KEY_DESCRIPTIONPRINCIPLE = "description_principle";
    public static final String KEY_CART = "cart";

    //field from ms_ba
    public static final String KEY_ROWIDBA = "_id";
    public static final String KEY_IDBA = "id_ba";
    public static final String KEY_NAMABA = "nama_ba";
    public static final String KEY_IDTL = "id_tl";
    public static final String KEY_NAMATL = "nama_tl";
    public static final String KEY_IDKBA = "id_kba";
    public static final String KEY_NAMAKBA = "nama_kba";
    public static final String KEY_BASTATUS = "ba_status";

    //field from ms_outlet
    public static final String KEY_ROWIDOUTLET = "_id";
    public static final String KEY_IDOUTLET = "id_outlet";
    public static final String KEY_NAMAOUTLET = "nama_outlet";
    public static final String KEY_STATUS = "status";

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "sms";
    private static final String DATABASE_TABLE = "ms_product";
    private static final String DATABASE_TABLE_BA = "ms_ba";
    private static final String DATABASE_TABLE_OUTLET = "ms_outlet";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table ms_product (_id integer primary key autoincrement,"
                    + "id_product text not null, nama_product text not null, volume text,"
                    + "description_principle text not null, cart text not null);";

    private static final String DATABASE_CREATE_BA =
            "create table ms_ba (_id integer primary key autoincrement,"
                    + "id_ba text not null, nama_ba text not null,"
                    + "id_tl text null, nama_tl text null, id_kba text null, nama_kba text null, ba_status text);";

    private static final String DATABASE_CREATE_OUTLET =
            "create table ms_outlet (_id integer primary key autoincrement,"
                    + "id_outlet text not null, nama_outlet text not null, status text not null);";

    private final Context context;

    private DatabaseHelper DBHelper;

    private SQLiteDatabase db;

    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_BA);
            db.execSQL(DATABASE_CREATE_OUTLET);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS ms_product");
            db.execSQL("DROP TABLE IF EXISTS ms_ba");
            db.execSQL("DROP TABLE IF EXISTS ms_outlet");
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        DBHelper.close();
    }

    public long insertProduct(String id_product, String nama_product, String volume, String description_principle){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IDPRODUCT, id_product);
        initialValues.put(KEY_NAMAPRODUCT, nama_product);
        initialValues.put(KEY_VOLUME, volume);
        initialValues.put(KEY_DESCRIPTIONPRINCIPLE, description_principle);
        initialValues.put(KEY_CART, "0");
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    public long insertBA(String id_ba, String nama_ba, String id_tl, String nama_tl, String id_kba, String nama_kba, String ba_status){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IDBA, id_ba);
        initialValues.put(KEY_NAMABA, nama_ba);
        initialValues.put(KEY_IDTL, id_tl);
        initialValues.put(KEY_NAMATL, nama_tl);
        initialValues.put(KEY_IDKBA, id_kba);
        initialValues.put(KEY_NAMAKBA, nama_kba);
        initialValues.put(KEY_BASTATUS, ba_status);
        return db.insert(DATABASE_TABLE_BA, null, initialValues);
    }
    public long insertOUTLET(String id_outlet, String nama_outlet, String status){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IDOUTLET, id_outlet);
        initialValues.put(KEY_NAMAOUTLET, nama_outlet);
        initialValues.put(KEY_STATUS, status);
        return db.insert(DATABASE_TABLE_OUTLET, null, initialValues);
    }

    public boolean deleteProduct(){
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }
    public boolean deleteBA(){
        return db.delete(DATABASE_TABLE_BA, null, null) > 0;
    }
    public boolean deleteOUTLET(){
        return db.delete(DATABASE_TABLE_OUTLET, null, null) > 0;
    }

    public Cursor getAllDataProduct(){
        return db.query(DATABASE_TABLE, new String[]{
                        KEY_ROWID,
                        KEY_IDPRODUCT,
                        KEY_NAMAPRODUCT,
                        KEY_DESCRIPTIONPRINCIPLE
                },
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getFieldDataProduct(){
        return db.query(DATABASE_TABLE, new String[]{
                        KEY_DESCRIPTIONPRINCIPLE
                },
                null,
                null,
                null,
                null,
                null);
    }

    public ArrayList<String> getAllNameProduct()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ms_product where cart != '1'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(KEY_NAMAPRODUCT)) + " " + res.getString(res.getColumnIndex(KEY_VOLUME)) + " - " + res.getString(res.getColumnIndex(KEY_IDPRODUCT)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllListProduct()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ms_product where cart != '1'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(KEY_NAMAPRODUCT)) + "\nVolume :" + res.getString(res.getColumnIndex(KEY_VOLUME)) + "\nCode :" + res.getString(res.getColumnIndex(KEY_IDPRODUCT)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String> getAllNameOutlet()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ms_outlet", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(KEY_IDOUTLET)) + "\n" + res.getString(res.getColumnIndex(KEY_NAMAOUTLET)));
            res.moveToNext();
        }
        return array_list;
    }

    public Cursor getDataProduct(long rowId) throws SQLException{
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]{
                                KEY_ROWID,
                                KEY_IDPRODUCT,
                                KEY_NAMAPRODUCT,
                                KEY_DESCRIPTIONPRINCIPLE
                        },
                        KEY_ROWID + "=" + rowId,
                        null,
                        null,
                        null,
                        null,
                        null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getDataOutlet() throws SQLException{
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_OUTLET, new String[]{
                                KEY_IDOUTLET
                        },
                        KEY_STATUS + "='active'",
                        null,
                        null,
                        null,
                        null,
                        null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getBA(){
        return db.query(DATABASE_TABLE_BA, new String[]{
                        KEY_IDBA
                },
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getAccountBA(){
        return db.query(DATABASE_TABLE_BA, new String[]{
                        KEY_IDBA,
                        KEY_NAMABA,
                        KEY_NAMATL,
                        KEY_NAMAKBA,
                        KEY_BASTATUS
                },
                null,
                null,
                null,
                null,
                null);
    }

    public boolean updateProduct(long rowId, String id_product, String nama_product, String description_principle){
        ContentValues args = new ContentValues();
        args.put(KEY_IDPRODUCT, id_product);
        args.put(KEY_NAMAPRODUCT, nama_product);
        args.put(KEY_DESCRIPTIONPRINCIPLE, description_principle);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateCart(String rowId, String cart){
        ContentValues args = new ContentValues();
        args.put(KEY_CART, cart);
        return db.update(DATABASE_TABLE, args, KEY_IDPRODUCT + "= '" + rowId + "'", null) > 0;
    }

    public boolean notupdateCart(String cart){
        ContentValues args = new ContentValues();
        args.put(KEY_CART, cart);
        return db.update(DATABASE_TABLE, args, KEY_IDPRODUCT + "!=''", null) > 0;
    }

    public boolean updateOutlet(String rowId, String status){
        ContentValues args = new ContentValues();
        args.put(KEY_STATUS, status);
        return db.update(DATABASE_TABLE_OUTLET, args, KEY_IDOUTLET + "= '" + rowId + "'", null) > 0;
    }

    public boolean notupdateOutlet(String rowId, String status){
        ContentValues args = new ContentValues();
        args.put(KEY_STATUS, status);
        return db.update(DATABASE_TABLE_OUTLET, args, KEY_IDOUTLET + "!='" + rowId + "'", null) > 0;
    }
}
