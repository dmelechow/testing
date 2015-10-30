package com.example.testing.MyActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.testing.MyEntry.MySportEntry;
import com.example.testing.MyEntry.MyTeamEntry;
import com.example.testing.R;

import java.util.ArrayList;

public class ActivitySport extends AppCompatActivity {
    ListView listView;
    ArrayList<MySportEntry> alMySportEntries;
    ArrayList<String> alForListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activity_sport);

        alForListView = new ArrayList<String>();

        listView = (ListView) findViewById(R.id.listViewSport);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", alMySportEntries.get(position).getId());
                Intent intent = new Intent(ActivitySport.this, ActivityTeam.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        // Принимаем Bundle
        try{
            Bundle bundleObject = getIntent().getExtras();
            alMySportEntries = (ArrayList<MySportEntry>) bundleObject.getSerializable("sports");

        } catch(Exception e){
            e.printStackTrace();
        }
        for(int i = 0; i < alMySportEntries.size(); i++){
            alForListView.add(alMySportEntries.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_1, alForListView);
        listView.setAdapter(adapter);


    }
}
