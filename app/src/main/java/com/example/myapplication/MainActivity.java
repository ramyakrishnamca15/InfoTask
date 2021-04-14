package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText mailEt, number;
    Button submitt;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    ArrayList<User> users = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mailEt = findViewById(R.id.mail_id);
        number = findViewById(R.id.number);
        linearLayout = findViewById(R.id.data_entry_layiut);
        submitt = findViewById(R.id.submit);
        recyclerView = findViewById(R.id.recyclerveiw);
        submitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateMail(mailEt.getText().toString()) && validateNumber(number.getText().toString())) {
                    User user = new User();
                    user.setMail(mailEt.getText().toString());
                    user.setNumber(number.getText().toString());
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("mail", user.getMail());
                    editor.putString("number", user.getNumber());
                    editor.commit();
                    users.add(user);
                    saveAndDisplay(users);
                } else {
                    Toast.makeText(MainActivity.this, "Enter valid credentails", Toast.LENGTH_LONG).show();
                }
            }
        });
        checkAndInflateDta();

    }

    public String MY_PREFS_NAME = "MyPrefsFile";

    public void checkAndInflateDta() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains("mail") && prefs.contains("number")) {
            String mail = prefs.getString("mail", "");
            String number = prefs.getString("number", "");
            User user = new User();
            user.setNumber(number);
            user.setMail(mail);
            users.add(user);
            saveAndDisplay(users);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }
    }

    private void saveAndDisplay(ArrayList<User> users) {
        linearLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        MyAdapter adapter = new MyAdapter(users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    Pattern emailPattern = Pattern.compile("[a-zA-Z0-9[!#$%&'()*+,/\\-_\\.\"]]+@[a-zA-Z0-9[!#$%&'()*+,/\\-_\"]]+\\.[a-zA-Z0-9[!#$%&'()*+,/\\-_\"\\.]]+");

    public boolean validateMail(String email) {
        Matcher m = emailPattern.matcher(email);
        return m.matches();
    }

    private boolean validateNumber(String number) {
        return number != null && !number.isEmpty() && number.length() == 10;
    }

}