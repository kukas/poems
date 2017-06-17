package com.example.jirka.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PoemActivity extends AppCompatActivity {

    public static Poem activePoem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(activePoem.text);
    }
}
