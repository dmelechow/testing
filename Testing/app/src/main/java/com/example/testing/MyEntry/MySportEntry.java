package com.example.testing.MyEntry;

import java.io.Serializable;

/**
 * Created by Дмитрий on 20.09.2015.
 */
public class MySportEntry implements Serializable{

    private int id;
    private String name;
    private String teamFeed;
    private int composerParam;

    public MySportEntry(int id, String name, String teamFeed, int composerParam) {

        this.id = id;
        this.name = name;
        this.teamFeed = teamFeed;
        this.composerParam = composerParam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamFeed() {
        return teamFeed;
    }

    public void setTeamFeed(String teamFeed) {
        this.teamFeed = teamFeed;
    }

    public int getComposerParam() {
        return composerParam;
    }

    public void setComposerParam(int composerParam) {
        this.composerParam = composerParam;
    }


}
