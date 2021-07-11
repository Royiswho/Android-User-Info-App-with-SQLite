package com.edu.sqlite_example;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Declare all the Buttons and Edit controll List View
    ListView studentLitView;
    EditText userName, age;
    TextView textView;

    StudentUtility studentUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use find view by id
        // add list. to it..

        userName = (EditText) findViewById(R.id.username);
        age = (EditText) findViewById(R.id.age);

        Button insertButton = (Button) findViewById(R.id.insertButton);
        Button readButton = (Button) findViewById(R.id.readButton);
        Button updateButton = (Button) findViewById(R.id.updateButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButtton);

        studentLitView = (ListView) findViewById(R.id.studentListView);


        insertButton.setOnClickListener(this);
        readButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        studentUtility = new StudentUtility(this);
        studentUtility.open();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.insertButton:
                InsertRecords();
                break;

            case R.id.readButton:
//                 ReadRecordsByToast();
                ReadRecords();
                break;

            case R.id.updateButton:
                UpdateRecords();
                break;

            case R.id.deleteButtton:
                //DeleteRecords();
                DeleteAllRecords();//  Delete All records();
                break;
        }
    }

    private void DeleteAllRecords() {
        studentUtility.deleteAllStudent();
    }

    private void ReadRecordsByToast() {
        studentUtility.listStudentsByToast();
    }

    private void DeleteRecords() {
        studentUtility.deleteAStudent(new Student(userName.getText().toString(), age.getText().toString()));
    }

    private void UpdateRecords() {
        studentUtility.updateStudent(new Student(userName.getText().toString(), age.getText().toString()));
    }

    private void ReadRecords() {

        studentUtility.listStudents();
        Cursor cursor = studentUtility.listStudents();

        // replace the following line with recycler view
        //recycler view

        if( cursor != null) {

            String[] from = new String[]{ StudentUtility.COLUMN_NAME,studentUtility.COLUMN_AGE};
            int[] to = new int[]{android.R.id.text1,android.R.id.text2};

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_2,cursor,from,to,0);

            studentLitView.setAdapter(simpleCursorAdapter);

        }
    }

    private void InsertRecords() {

        studentUtility.addStudent(new Student(userName.getText().toString(), age.getText().toString()));

        //        Toast.makeText(this, "InsertRecords Clicked", Toast.LENGTH_SHORT).show();
    }
}