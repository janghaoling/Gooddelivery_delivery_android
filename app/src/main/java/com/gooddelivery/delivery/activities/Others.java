package com.gooddelivery.delivery.activities;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.gooddelivery.delivery.R;

public class Others extends AppCompatActivity {

    Button Chatus;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        fragmentManager = getSupportFragmentManager();
        Chatus = (Button)findViewById(R.id.chat_us);
        /*Chatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new ChatFragment(), "Chat");
                fragmentTransaction.commit();
            }
        });*/
    }
}
