package com.example.albert.database;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends AppCompatActivity {

    DBHelper mydb;

    TextView name;
    TextView phone;
    TextView email;

    int id_to_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);
        name = (TextView) findViewById(R.id.editTextName);
        phone = (TextView) findViewById(R.id.editTextPhone);
        email = (TextView) findViewById(R.id.editTextEmail);

        mydb = new DBHelper(this, null, null, 1);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int value = extras.getInt("id");
            if(value > 0){
                Cursor rs = mydb.getData(value);
                id_to_update = value;
                rs.moveToFirst();

                String txtName = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String txtPhone = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String txtEmail = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));

                if(!rs.isClosed()){
                    rs.close();
                }

                Button btn = (Button) findViewById(R.id.button1);
                btn.setVisibility(View.INVISIBLE);

                name.setText(txtName);
                name.setFocusable(false);
                name.setClickable(false);

                phone.setText(txtPhone);
                phone.setFocusable(false);
                phone.setClickable(false);

                email.setText(txtEmail);
                email.setFocusable(false);
                email.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int value = extras.getInt("id");
            if(value>0) {
                getMenuInflater().inflate(R.menu.display_contact, menu);
            }
            else{
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Contact:
                Button btn = (Button) findViewById(R.id.button1);
                btn.setVisibility(View.VISIBLE);

                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);

                return true;

            case R.id.Delete_Contact:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete_contact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mydb.deleteContact(id_to_update);
                                Toast.makeText(getApplicationContext(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //User cancelled the dialog
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void editContact(){
        Button btn = (Button) findViewById(R.id.button1);
        btn.setVisibility(View.VISIBLE);

        name.setEnabled(true);
        name.setFocusableInTouchMode(true);
        name.setClickable(true);

        phone.setEnabled(true);
        phone.setFocusableInTouchMode(true);
        phone.setClickable(true);

        email.setEnabled(true);
        email.setFocusableInTouchMode(true);
        email.setClickable(true);
    }

    public boolean deleteContact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_contact)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id_to_update) {
                        mydb.deleteContact(id_to_update);
                        Toast.makeText(getApplicationContext(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //User cancelled the dialog
                    }
                });

        AlertDialog d = builder.create();
        d.setTitle("Are you sure");
        d.show();
        return true;
    }*/

    public void buttonClicked(View view){
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            int value = extras.getInt("id");
            if(value>0){
                if(mydb.updateContact(id_to_update, name.getText().toString(), phone.getText().toString(), email.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(mydb.insertContact(name.getText().toString(), phone.getText().toString(), email.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
