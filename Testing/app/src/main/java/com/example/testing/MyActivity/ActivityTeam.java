package com.example.testing.MyActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.testing.DBSQLite.WorkDB;
import com.example.testing.MyEntry.MyTeamEntry;
import com.example.testing.R;

import java.util.ArrayList;

public class ActivityTeam extends AppCompatActivity {
    ArrayList<MyTeamEntry> alMyTeam;
    WorkDB workDB;
    ListView listView;
    ArrayList<String> alForListView;
    int myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_team);

        listView = (ListView) findViewById(R.id.listViewTeam);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("positionteam", position);
                bundle.putInt("positionsport", alMyTeam.get(position).getComposerParam());
                Intent intent = new Intent(ActivityTeam.this, HomeActivity.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        alForListView = new ArrayList<String>();

        alMyTeam = new ArrayList<MyTeamEntry>();
        Bundle bundle = getIntent().getExtras();
        myId = bundle.getInt("position");
        workDB = new WorkDB(this);
        alMyTeam = workDB.queryTeam(myId);

        for(int i = 0; i < alMyTeam.size(); i++){
            alForListView.add(alMyTeam.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, alForListView);
        listView.setAdapter(adapter);

    }

}
