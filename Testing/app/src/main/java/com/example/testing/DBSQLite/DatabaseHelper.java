package com.example.testing.DBSQLite;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Xml;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;

import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.testing.AsynkTasks.AsyncTaskTools;
import com.example.testing.MyEntry.MySportEntry;
import com.example.testing.MyEntry.MyTeamEntry;
import com.example.testing.MyParsers.XMLParserSports;
import com.example.testing.MyParsers.XMLParserTeam;
import com.example.testing.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Дмитрий on 25.09.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    AsyncTaskTools as;
    SQLiteDatabase db;
    ArrayList<MySportEntry> alMySportEntries;
    ArrayList<MyTeamEntry> alMyTeamExtra;

    MyAsyncTaskRequest myAsyncTaskRequest;

    SharedPreferences mSharedPref;

    final String CREATE_T_SPORT = "create table T_SPORT (" +
            "ID integer NOT NULL," +
            "NAME text NOT NULL," +
            "TEAMFEED text NOT NULL," +
            "COMPOSERPARAM integer NOT NULL" +
            ");";
    final String CREATE_T_TEAM = "create table T_TEAM (" +
            "ID integer NOT NULL," +
            "NAME text NOT NULL," +
            "ABBREV text NOT NULL," +
            "PLAYERSFEED text NOT NULL," +
            "COMPOSERPARAM integer NOT NULL" +
            ");";
    public DatabaseHelper(Context context) {
        super(context, "MyDB", null, 1);
        this.context = context;
        alMySportEntries = new ArrayList<MySportEntry>();
        alMyTeamExtra = new ArrayList<MyTeamEntry>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_T_SPORT);
        db.execSQL(CREATE_T_TEAM);

        mSharedPref = context.getSharedPreferences(context.getString(R.string.myfile),Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPref.edit();
        mEditor.putInt(context.getString(R.string.idSport), -1);
        mEditor.putInt(context.getString(R.string.idTeam), -1);
        mEditor.commit();
        myAsyncTaskRequest = new MyAsyncTaskRequest();
        as = new AsyncTaskTools();
        as.execute(myAsyncTaskRequest);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    class MyAsyncTaskRequest extends AsyncTask<Void,Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            myRequest1(context.getResources().getString(R.string.url_sport),false);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (alMySportEntries.size() != 0)
                for (int i = 0; i < alMySportEntries.size(); i++) {
                    myRequest1(alMySportEntries.get(i).getTeamFeed(), true);
                }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    public void myRequest1(String url, final boolean requestFlag){
        RequestQueue mRequestQueue;
        // Instantiate the cache

        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        InputStream in = null;
                        try {
                            in = new ByteArrayInputStream(response.getBytes("UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        try {
                            XmlPullParser parser = Xml.newPullParser();
                            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                            parser.setInput(in, null);
                            parser.nextTag();

                            if(requestFlag){
                                parserTeam(parser);
                            }
                            else parserSport(parser);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

    private void parserSport(XmlPullParser parser) throws IOException, XmlPullParserException {
            XMLParserSports xmlParserSports = new XMLParserSports();
            alMySportEntries = (ArrayList<MySportEntry>) xmlParserSports.readFeed(parser);
            // Добавляем записи в таблицу
            ContentValues values = new ContentValues();

            for (int i = 0; i < alMySportEntries.size(); i++) {
                values.put("ID", alMySportEntries.get(i).getId());
                values.put("NAME", alMySportEntries.get(i).getName());
                values.put("TEAMFEED", alMySportEntries.get(i).getTeamFeed());
                values.put("COMPOSERPARAM", alMySportEntries.get(i).getComposerParam());

                db.insert("T_SPORT", null, values);
            }
    }

    public void parserTeam(XmlPullParser parser) throws IOException, XmlPullParserException {
            XMLParserTeam xmlParserTeam = new XMLParserTeam();

            alMyTeamExtra = (ArrayList<MyTeamEntry>) xmlParserTeam.readFeed(parser);
            // Добавляем записи в таблицу
            ContentValues values = new ContentValues();

            for (int i = 0; i < alMyTeamExtra.size(); i++) {
                values.put("ID", alMyTeamExtra.get(i).getId());
                values.put("NAME", alMyTeamExtra.get(i).getName());
                values.put("ABBREV", alMyTeamExtra.get(i).getAbbrev());
                values.put("PLAYERSFEED", alMyTeamExtra.get(i).getPlayersFeed());
                values.put("COMPOSERPARAM", alMyTeamExtra.get(i).getComposerParam());

                db.insert("T_TEAM", null, values);
            }
    }
}
