package com.example.jirka.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jirka on 17.6.17.
 */

class Poem {
    public String name;
    public String text;
    public String author;
    public Poem(String text) {
        this.text = text;
    }

    public Poem(JSONObject obj) throws JSONException {
        name = obj.getString("name");
        text = obj.getString("text");
        author = obj.getString("author");
    }
}
