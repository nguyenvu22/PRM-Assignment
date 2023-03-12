package com.example.studysqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtClassId, edtClassName, edtClassNum;
    Button btnInsert, btnDelete, btnUpdate, btnQuery;
    ListView lv;
    ArrayList<String> myList;           //To store data

    ArrayAdapter<String> myAdapter;     //To render data
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtClassId = findViewById(R.id.edtClassId);
        edtClassName = findViewById(R.id.edtClassName);
        edtClassNum = findViewById(R.id.edtClassNum);
        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnQuery = findViewById(R.id.btnQuery);

        //Setup ListView to render Data
        lv = findViewById(R.id.lv);
        myList = new ArrayList<>();
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        lv.setAdapter(myAdapter);

        myDB = openOrCreateDatabase("StudentManagement", MODE_PRIVATE, null);
        try {
            String sql = "CREATE TABLE tblClass(id TEXT primary key, name TEXT, num INTEGER)";
            myDB.execSQL(sql);
        }catch(Exception e){
            Log.e("Error", "Table existed");
        }

        /*===================================================================================================
            Sending data (req.body) : ContentValues
                contentValues.put("field", value)

            Sending param (req.param) : String[]{}
                String[]{ value1, value2, ... }
                    each value will replace ? in query


        ===================================================================================================*/

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtClassId.getText().toString();
                String name = edtClassName.getText().toString();
                int num = Integer.parseInt(edtClassNum.getText().toString());
                ContentValues myValue = new ContentValues();
                myValue.put("id", id);
                myValue.put("name", name);
                myValue.put("num", num);
                String msg = "";

                if(myDB.insert("tblClass", null, myValue) == -1){
                    msg = "Fail to insert Record!";
                }else{
                    msg = "Insert a Record!";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtClassId.getText().toString();
                int n = myDB.delete("tblClass", "id = ?", new String[]{id});
                String msg = "";
                if(n == 0){
                    msg = "No Record to Delete!";
                }else{
                    msg = n + "Record is Deleted";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(edtClassNum.getText().toString());
                String name = edtClassName.getText().toString();
                String id = edtClassId.getText().toString();
                ContentValues myValue = new ContentValues();
                myValue.put("name", name);
                myValue.put("num", num);
                int n = myDB.update("tblClass", myValue, "id = ?", new String[]{id});
                String msg = "";
                if(n == 0){
                    msg = "No Record to Update!";
                }else{
                    msg = n + " Record is Updated";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myList.clear();
                Cursor c = myDB.query("tblClass", null, null, null, null, null, null);
                c.moveToNext();
                String data = "";
                while (c.isAfterLast() == false){
                    data = "ID : " + c.getString(0) + "\nClass Name : " + c.getString(1) + "\nTotal Studet : " + c.getString(2);
                    c.moveToNext();
                    myList.add(data);
                }
                c.close();
                myAdapter.notifyDataSetChanged();
            }
        });
    }
}