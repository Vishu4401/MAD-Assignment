package com.example.calling_application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ContactView extends AppCompatActivity {

    private EditText et1;
    private EditText et2;
    ImageButton calling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);

        et1 = findViewById(R.id.editText);
        et2 = findViewById(R.id.ediText2);
        calling = findViewById(R.id.calling);

        calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = et2.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phoneNumber));
                startActivity(intent);
            }
        });
    }


    @SuppressLint("Recycle")
    public void getNameButton(View view){

        try{
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(et2.getText().toString()));
            Cursor cursor;
            cursor = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                    null,null, null);

            String stringContactName = "Name Not found";

            if(cursor != null){
                if(cursor.moveToFirst()){
                    stringContactName = cursor.getString(0);
                }
            }
            et1.setText(stringContactName);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(ContactView.this, "Entered number does not match the contact list", Toast.LENGTH_LONG).show();

        }
    }


    @SuppressLint({"SetTextI18n", "Recycle"})
    public void getNumberButton(View view){

        try {
            Cursor cursor;
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.TYPE},
                    "DISPLAY_NAME = '" + et1.getText().toString() + "'", null, null);

            cursor.moveToFirst();
            et2.setText(cursor.getString(0));
        }
        catch (Exception e){
            e.printStackTrace();
            et2.setText("Does not match");
            Toast.makeText(ContactView.this, "Entered Name is incorrect", Toast.LENGTH_LONG).show();
        }



    }
}