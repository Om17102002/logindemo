package com.example.logindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText e=findViewById(R.id.editTextText);
        EditText p=findViewById(R.id.editTextText2);
        Button bt=findViewById(R.id.button);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em=e.getText().toString();
                String pm=p.getText().toString();
                if(em.equals("jspmrscoe@gmail.com") & pm.equals("jspm@123")){
                    startactivity();
                }
            }
        });

    }

    private void startactivity() {
        Intent intent=new Intent(this, MainActivity2.class);
        startActivity(intent);
        finish();
    }
}