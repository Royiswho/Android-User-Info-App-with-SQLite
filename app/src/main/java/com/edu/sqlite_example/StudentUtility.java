package com.edu.sqlite_example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

// wrapper class to SQLiteOpenhelper
public class StudentUtility {

    Context mContext;
    MainActivity mainActivity;

    private StudentDatabaseUtility databaseUtility;
    private SQLiteDatabase sqLiteDatabase;
    //1. Let's create the DB

    public static final String DB_NAME = "student.db";

    // 2. Create fields to the DB
    // Table_name students
    // ID      Name         AGE
    // 1       Aka          22

    public static final String TABLE_NAME = "students";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AGE = "_age";
    public static final String COLUMN_NAME = "_name";

    // 2.1 DB Version

    public static final int DB_VERSION = 1;

    // DB ver 2 will be created when the field of table changes
    // e.g. add a field named ADDRESS


    //Care db will drop all existing record and will create new rec


    // 2.2 create the query table

    private final String TABLE_CREATE_QUERY =
            "create table "+TABLE_NAME+"("+COLUMN_ID+" integer primary key autoincrement,"+COLUMN_NAME +" text,"+
                    COLUMN_AGE +  " text);";

    public StudentUtility(MainActivity mainActivity) {
        mContext = mainActivity; // passing this pointer to mcontext
//        this.mainActivity = mainActivity;
        databaseUtility = new StudentDatabaseUtility(mContext);
    }

    public void open() {

        // following line gets a pointer to the CRUD operation
        sqLiteDatabase = databaseUtility.getWritableDatabase();

        // It is not a good idea to start the CRUD operation here
    }

    ///////////////////// CRUD Operation //////////////////////

    public void addStudent(Student student) {

        ContentValues values = new ContentValues(); // like bundle

        values.put(COLUMN_NAME, student.name);
        values.put(COLUMN_AGE, student.age);

        long id = sqLiteDatabase.insert(TABLE_NAME, null, values);

        if(id != -1){
            Toast.makeText(mContext, "Inserted the record..." + id, Toast.LENGTH_SHORT).show();
        }
    }

    public void listStudentsByToast() {
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()){

            do {
                //display the records
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String age = cursor.getString(cursor.getColumnIndex(COLUMN_AGE));

                // Pass it on to POJO class student: ArrayList can be given to recycler view
                Toast.makeText(mContext, "Name: " + name + ", Age: " + age, Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
        }
    }

    public void updateStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_AGE, student.age);

        long numberOfRowsUpdated = sqLiteDatabase.update(TABLE_NAME, values, COLUMN_NAME + "=?", // where COLUMN_NAME = student.name, then update values
                new String[]{student.name});

        Toast.makeText(mContext, "Rows Updated: " + numberOfRowsUpdated, Toast.LENGTH_SHORT).show();
    }

    public void deleteAStudent(Student student) {
        long numberOfRowsDeleted = sqLiteDatabase.delete(TABLE_NAME, COLUMN_NAME + "=?", // where COLUMN_NAME = student.name, then update values
                new String[]{student.name});

        Toast.makeText(mContext, "Rows Deleted: " + numberOfRowsDeleted, Toast.LENGTH_SHORT).show();
    }

    public void deleteAllStudent() {
        int numberOfRecordsDeleted = sqLiteDatabase.delete(TABLE_NAME, null, null);

        Toast.makeText(mContext, "Deleted the complete DB, Rows Deleted: " + numberOfRecordsDeleted, Toast.LENGTH_SHORT).show();
    }

    public Cursor listStudents() {
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);

        return cursor;
    }

    ///////////////////// end of CRUD Operation //////////////////////

    //3.create care database class to handle SQLiteOpenHelper
    private class StudentDatabaseUtility extends SQLiteOpenHelper {
        public StudentDatabaseUtility(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL(TABLE_CREATE_QUERY);
        }




        // 2.1
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            Log.d("tag","Updating data base from old ver "+oldVersion+"to "+newVersion+"This will destroy the old data base");

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
