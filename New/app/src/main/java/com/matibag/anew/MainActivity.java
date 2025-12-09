package com.matibag.anew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity: collects new record data and saves to DB
 */
public class MainActivity extends AppCompatActivity {
    public Intent DisplayForm;
    public DBHelper Conn;
    public EditText Fname, Mname, Lname, Address, Email;
    public String FNAME = null, MNAME = null, LNAME = null, ADDRESS = null, EMAIL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fname = findViewById(R.id.Fname);
        Mname = findViewById(R.id.Mname);
        Lname = findViewById(R.id.Lname);
        Address = findViewById(R.id.Addre);
        Email = findViewById(R.id.Gmail);

        Conn = new DBHelper(this);
    }

    public void AddRecord(View view) {
        FNAME = Fname.getText().toString().trim();
        MNAME = Mname.getText().toString().trim();
        LNAME = Lname.getText().toString().trim();
        ADDRESS = Address.getText().toString().trim();
        EMAIL = Email.getText().toString().trim();

        if (FNAME.isEmpty()) {
            Fname.setError("Please Enter Your First Name");
            Fname.requestFocus();
        } else if (MNAME.isEmpty()) {
            Mname.setError("Please Enter Your Middle Name");
            Mname.requestFocus();
        } else if (LNAME.isEmpty()) {
            Lname.setError("Please Enter Your Last Name");
            Lname.requestFocus();
        } else if (ADDRESS.isEmpty()) {
            Address.setError("Please Enter Your Address");
            Address.requestFocus();
        } else if (EMAIL.isEmpty()) {
            Email.setError("Please Enter Your Email");
            Email.requestFocus();
        } else {
            boolean ok = Conn.AddRecords(FNAME, MNAME, LNAME, ADDRESS, EMAIL);
            if (ok) {
                Fname.setText("");
                Mname.setText("");
                Lname.setText("");
                Address.setText("");
                Email.setText("");
                Toast.makeText(getApplicationContext(), "RECORD SAVED!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "SAVING INFORMATION FAILED!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ViewRecords(View view) {
        DisplayForm = new Intent(this, RecordsActivity.class);
        startActivity(DisplayForm);
    }

    public void ClearRecords(View view) {
        try {
            Conn.ClearRecord();
            Toast.makeText(getApplicationContext(), "RECORDS CLEAR", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}