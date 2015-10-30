package com.example.testing.DBSQLite;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testing.MyEntry.MySportEntry;
import com.example.testing.MyEntry.MyTeamEntry;

import java.util.ArrayList;

public class WorkDB {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Context context;
    public WorkDB(Context context) {
        this.context = context;
    }
    public ArrayList<MySportEntry> queryMySportEntry(){
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        ArrayList<MySportEntry> alMySportEntries = new ArrayList<MySportEntry>();
        Cursor c;
        c = db.query("T_SPORT", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int idColumnIndex = c.getColumnIndex("ID");
            int nameColumnIndex = c.getColumnIndex("NAME");
            int teamfeedColumnIndex = c.getColumnIndex("TEAMFEED");
            int composerparamColumnIndex = c.getColumnIndex("COMPOSERPARAM");
            do {
                alMySportEntries.add(new MySportEntry(c.getInt(idColumnIndex),
                        c.getString(nameColumnIndex), c.getString(teamfeedColumnIndex), c.getInt(composerparamColumnIndex)));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        dbHelper.close();

        return alMySportEntries;
    }
    public ArrayList<ArrayList<MyTeamEntry>> queryMyTeamEntry(ArrayList<MySportEntry> alMySportEntries){
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        ArrayList<ArrayList<MyTeamEntry>> alMyTeamEntries = new ArrayList<ArrayList<MyTeamEntry>>();
        ArrayList<MyTeamEntry> alMyTeamExtra;
        Cursor c = null;
        for (int i = 0; i < alMySportEntries.size(); i++){
            c = db.query("T_TEAM", null, "COMPOSERPARAM = ?", new String[]{Integer.toString(alMySportEntries.get(i).getId())}, null, null, null);
            if (c.moveToFirst()) {
                int idColumnIndex = c.getColumnIndex("ID");
                int nameColumnIndex = c.getColumnIndex("NAME");
                int abbrevColumnIndex = c.getColumnIndex("ABBREV");
                int playersfeedColumnIndex = c.getColumnIndex("PLAYERSFEED");
                int composerparamColumnIndex = c.getColumnIndex("COMPOSERPARAM");
                alMyTeamExtra = new ArrayList<MyTeamEntry>();
                do {
                    alMyTeamExtra.add(new MyTeamEntry(c.getInt(idColumnIndex),
                            c.getString(nameColumnIndex), c.getString(abbrevColumnIndex),
                            c.getString(playersfeedColumnIndex), c.getInt(composerparamColumnIndex)));
                } while (c.moveToNext());
                alMyTeamEntries.add(alMyTeamExtra);
            }
        }

        c.close();
        db.close();
        dbHelper.close();

        return  alMyTeamEntries;
    }
    public ArrayList<MyTeamEntry> queryTeam(int myId){
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        ArrayList<MyTeamEntry> alMyTeam = null;

        Cursor c;
        c = db.query("T_TEAM", null, "COMPOSERPARAM = ?", new String[]{Integer.toString(myId)}, null, null, null);
        if (c.moveToFirst()) {
            int idColumnIndex = c.getColumnIndex("ID");
            int nameColumnIndex = c.getColumnIndex("NAME");
            int abbrevColumnIndex = c.getColumnIndex("ABBREV");
            int playersfeedColumnIndex = c.getColumnIndex("PLAYERSFEED");
            int composerparamColumnIndex = c.getColumnIndex("COMPOSERPARAM");

            alMyTeam = new ArrayList<MyTeamEntry>();
            do {
                alMyTeam.add(new MyTeamEntry(c.getInt(idColumnIndex),
                        c.getString(nameColumnIndex), c.getString(abbrevColumnIndex),
                        c.getString(playersfeedColumnIndex), c.getInt(composerparamColumnIndex)));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        dbHelper.close();

        return alMyTeam;
    }
}
