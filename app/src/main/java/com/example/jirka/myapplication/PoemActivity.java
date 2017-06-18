package com.example.jirka.myapplication;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.res.Resources;
import android.view.MenuItem;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jirka.myapplication.R.id.textView;

public class PoemActivity extends AppCompatActivity {

    public static Poem activePoem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(activePoem.name + "\n" + activePoem.author + "\n\n" +  activePoem.text);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if( id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
