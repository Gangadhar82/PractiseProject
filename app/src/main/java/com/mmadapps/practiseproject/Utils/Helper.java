package com.mmadapps.practiseproject.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mmadapps.practiseproject.Flight.beans.flightdetails;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by gangadhar.g on 1/10/2016.
 */
public class Helper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.mmadapps.practiseproject/databases/";
    private static String DB_NAME = "FlightDetails.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private String TAG = "Helper";
    Cursor cursorGetData;


    /**
     * Helper constructor Called from Helper object getapplicationContext
     **/
    public Helper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * creating database at first time of application Setup
     * @throws IOException
     **/
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                //throw new Error("Error copying database");
            }
        }
    }

    /**
     * checking the database Availability based on Availability copying database
     * to the device data
     * @return true (if Available)
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
                checkDB = SQLiteDatabase.openDatabase(myPath, null,    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error is" + e.toString());
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * copying database from asserts to package location in mobile data
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Opening database for retrieving/inserting information
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * Closing database after operation done
     */
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    /**
     * getting information based on SQL Query
     * @param sql
     * @return Output of Query
     */
    private Cursor getData(String sql) {
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cursorGetData = getReadableDatabase().rawQuery(sql,null);
        return cursorGetData;
    }

    /**
     * Inserting information based on table name and values
     * @param tableName
     * @param values
     * //@param contentValues
     * @return
     */
    private long insertData(String tableName, ContentValues values) throws SQLException {
        openDataBase();
        return myDataBase.insert(tableName,null, values);
    }

    /**
     * Updating information based on table name and Condition
     * @param tableName
     * @param values
     * @return
     */
    private int updateData(String tableName, ContentValues values,String condition) {
        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myDataBase.update(tableName, values, condition, null);
    }


    public void insertflightdetails(ArrayList<flightdetails> flightdetailses)
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            //db.delete(tbl_language,null,null);
            //Log.e(TAG,tbl_language+" Deleted successfully");
            db.close();
            int mTotalInsertedRows = 0;
            for (flightdetails language : flightdetailses){
                ContentValues cv = new ContentValues();
                cv.put("carrier_FsCode",language.getMcarrierFsCode());
                cv.put("flight_Number",language.getMflightNumber());
                cv.put("departureAirport_FsCode",language.getMdepartureAirportFsCode());
                cv.put("arrivalAirport_FsCode",language.getMarrivalAirportFsCode());
                cv.put("departure_Time",language.getMdepartureTime());
                cv.put("arrival_Time",language.getMarrivalTime());

                long rowId = insertData(tbl_flightdetails, cv);
                if(rowId > 0){
                    mTotalInsertedRows++;
                }
            }
            Log.e(TAG,"InsertLanguage TotalRows"+mTotalInsertedRows);
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public ArrayList<flightdetails> getLanguageList(){
        ArrayList<flightdetails> languageDetails = null;
        Cursor cursor = getData("SELECT * FROM "+tbl_flightdetails);
        if(cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            languageDetails = new ArrayList<>();
            int len = cursor.getCount();
            for(int i=0; i<len; i++){
                flightdetails language = new flightdetails();
                language.setMcarrierFsCode(cursor.getString(0));
                language.setMflightNumber(cursor.getString(1));
                language.setMdepartureAirportFsCode(cursor.getString(2));
                language.setMarrivalAirportFsCode(cursor.getString(3));
                language.setMdepartureTime(cursor.getString(4));
                language.setMarrivalTime(cursor.getString(5));
                languageDetails.add(language);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return languageDetails;
    }






    public String tbl_flightdetails="tbl_flight_details";


}
