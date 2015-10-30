package com.example.testing.MyEntry;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Дмитрий on 21.09.2015.
 */
public class MyTeamEntry implements Serializable, Parcelable{
    private int id;
    private String name;
    private String abbrev;
    private String playersFeed;
    private int composerParam;


    public MyTeamEntry(int id, String name, String abbrev, String playersFeed, int composerParam) {
        this.id = id;
        this.name = name;
        this.abbrev = abbrev;
        this.playersFeed = playersFeed;
        this.composerParam = composerParam;
    }


    protected MyTeamEntry(Parcel in) {
        id = in.readInt();
        name = in.readString();
        abbrev = in.readString();
        playersFeed = in.readString();
        composerParam = in.readInt();
    }

    public static final Creator<MyTeamEntry> CREATOR = new Creator<MyTeamEntry>() {
        @Override
        public MyTeamEntry createFromParcel(Parcel in) {
            return new MyTeamEntry(in);
        }

        @Override
        public MyTeamEntry[] newArray(int size) {
            return new MyTeamEntry[size];
        }
    };

    public int getComposerParam() {
        return composerParam;
    }

    public void setComposerParam(int composerParam) {
        this.composerParam = composerParam;
    }

    public String getPlayersFeed() {
        return playersFeed;
    }

    public void setPlayersFeed(String playersFeed) {
        this.playersFeed = playersFeed;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(abbrev);
        dest.writeString(playersFeed);
        dest.writeInt(composerParam);
    }
}
