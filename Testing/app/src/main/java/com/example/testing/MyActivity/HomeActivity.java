package com.example.testing.MyActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testing.AsynkTasks.AsyncTaskTools;
import com.example.testing.DBSQLite.WorkDB;
import com.example.testing.Fragments.ImageViewer;
import com.example.testing.MyEntry.MySportEntry;
import com.example.testing.MyEntry.MyTeamEntry;
import com.example.testing.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageViewer imageViewer;
    private ImageView ivAliveStar;
    private TextView tvSportTeam;
    private TextView tvSport;
    private TextView tvTeam;
    private WebView mWebView;
    private static  int idSport;
    private static int sizeSport;
    private static ArrayList<Integer> sizeTeam;
    private static int idTeam;
    private AsyncTaskTools as;
    private Animation animAliveStarGreater;
    private Animation animAliveStarLess;
    private ArrayList<ArrayList<MyTeamEntry>> alMyTeamEntries;
    private ArrayList<MyTeamEntry> alMyTeamExtra;
    private SharedPreferences mSharedPref;
    private ArrayList<MySportEntry> alMySportEntries;
    private MyAsyncTaskChangeTeam myAsyncTaskChangeTeam;
    private MyAsyncTaskAliveStar myAsyncTaskAliveStar;
    private WorkDB workDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imageViewer = new ImageViewer(this);
        imageViewer.loadImage(R.drawable.splashscreen);

        FrameLayout relativeLayout = (FrameLayout) findViewById(R.id.frgmCont);
        relativeLayout.addView(imageViewer);

        workDB = new WorkDB(this);

        alMySportEntries = workDB.queryMySportEntry();
        sizeTeam = new ArrayList<Integer>();
        alMyTeamEntries = workDB.queryMyTeamEntry(alMySportEntries);
        for(int i = 0; i < alMyTeamEntries.size(); i++){
            sizeTeam.add(alMyTeamEntries.get(i).size());
        }

        ivAliveStar = (ImageView) findViewById(R.id.ivAliveStar);
        ivAliveStar.setOnClickListener(this);

        tvSportTeam = (TextView) findViewById(R.id.tvSportTeam);
        tvSportTeam.setOnClickListener(this);

        tvSport = (TextView) findViewById(R.id.tvSport);
        tvSport.setOnClickListener(this);
        tvTeam = (TextView) findViewById(R.id.tvTeam);

        animAliveStarGreater = AnimationUtils.loadAnimation(this, R.anim.anim_star_greater);
        animAliveStarLess = AnimationUtils.loadAnimation(this, R.anim.anim_star_less);


        mSharedPref = getSharedPreferences(getResources().getString(R.string.myfile), MODE_PRIVATE);
        sizeSport = alMyTeamEntries.size();
        myAsyncTaskChangeTeam = new MyAsyncTaskChangeTeam();
        myAsyncTaskAliveStar = new MyAsyncTaskAliveStar();

        idTeam = mSharedPref.getInt(getResources().getString(R.string.idTeam),0);
        idSport = mSharedPref.getInt(getResources().getString(R.string.idSport),0);
        if(idTeam >= 0 && idSport >= 0) {
            tvTeam.setText(alMyTeamEntries.get(idSport).get(idTeam).getName());
            tvSport.setText(alMySportEntries.get(idSport).getName());
            tvSportTeam.setText(alMySportEntries.get(idSport).getName() + " / " + alMyTeamEntries.get(idSport).get(idTeam).getName());
        }

        as = new AsyncTaskTools();
        as.execute(myAsyncTaskAliveStar);
        as.execute(myAsyncTaskChangeTeam);

    }

    @Override
    public void onDestroy(){
        cancelTask(myAsyncTaskChangeTeam);
        super.onDestroy();
    }

    private void cancelTask(AsyncTask asyncTask) {
        if (asyncTask == null)
            return;
        else
            asyncTask.cancel(false);
    }

    class MyAsyncTaskChangeTeam extends  AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try {
                while(true){
                    if (isCancelled())
                        return null;
                    else {
                        TimeUnit.SECONDS.sleep(4);
                        publishProgress();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
            if(idTeam >= 0 && idSport >= 0){
                if(idTeam >= sizeTeam.get(idSport)){
                    if((idSport + 1) >= sizeSport)
                        idSport = 0;
                    else
                        idSport++;
                    idTeam = 0;
                    tvTeam.setText(alMyTeamEntries.get(idSport).get(idTeam).getName());


                    for(int i = 0; i < alMySportEntries.size(); i ++){
                        if(alMyTeamEntries.get(idSport).get(idTeam).getComposerParam() ==
                                alMySportEntries.get(i).getComposerParam()) {
                            tvSport.setText(alMySportEntries.get(i).getName());
                            tvSportTeam.setText(alMySportEntries.get(i).getName() + " / " +
                                    alMyTeamEntries.get(idSport).get(idTeam).getName());
                        }

                    }
                } else if(idSport >= sizeSport){
                    idSport = 0;
                    idTeam = 0;
                    tvTeam.setText(alMyTeamEntries.get(idSport).get(idTeam).getName());
                    tvSport.setText(alMySportEntries.get(idSport).getName());

                    tvSportTeam.setText(alMySportEntries.get(idSport).getName() + " / " +
                            alMyTeamEntries.get(idSport).get(idTeam).getName());
                } else {
                    tvTeam.setText(alMyTeamEntries.get(idSport).get(idTeam).getName());

                    for (int i = 0; i < alMySportEntries.size(); i++) {
                        if (alMyTeamEntries.get(idSport).get(idTeam).getComposerParam() ==
                                alMySportEntries.get(i).getComposerParam()) {

                            tvSportTeam.setText(alMySportEntries.get(i).getName() + " / " +
                                    alMyTeamEntries.get(idSport).get(idTeam).getName());
                        }

                    }
                    idTeam++;
                }
            }
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            SharedPreferences.Editor mEditor = mSharedPref.edit();
            mEditor.putInt(getString(R.string.idSport), idSport);
            mEditor.putInt(getString(R.string.idTeam), idTeam);
            mEditor.commit();
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            int idSportLocal = bundle.getInt("positionsport");
            idTeam = bundle.getInt("positionteam");

            for(int i = 0; i < alMyTeamEntries.size(); i++){
                if(idSportLocal == alMyTeamEntries.get(i).get(0).getComposerParam()) {
                    idSport = i;
                    tvTeam.setText(alMyTeamEntries.get(i).get(idTeam).getName());
                    break;
                }
            }
            for(int i = 0; i < alMySportEntries.size(); i++){
                if(idSportLocal == alMySportEntries.get(i).getId()) {
                    tvSport.setText(alMySportEntries.get(i).getName());

                    tvSportTeam.setText(alMySportEntries.get(i).getName() + " / " +
                            alMyTeamEntries.get(idSport).get(idTeam).getName());
                    break;
                }
            }

        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ivAliveStar:{
                String url = "http://www.google.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }break;

            case R.id.tvSportTeam:{
                Bundle b = new Bundle();
                b.putSerializable("sports", alMySportEntries);
                Intent activ = new Intent(HomeActivity.this, ActivitySport.class);
                activ.putExtras(b);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                startActivity(activ);
            }break;
            case R.id.tvSport:{
                mWebView = (WebView) findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadUrl("http://yandex.ru");
            }break;
        }
    }

    class MyAsyncTaskAliveStar extends AsyncTask<Void,Boolean, Void> {
        boolean flagGreaterOrLess;
        @Override
        protected Void doInBackground(Void... params) {
            flagGreaterOrLess = true;
            try {
                while(true){
                    publishProgress(flagGreaterOrLess);
                    TimeUnit.SECONDS.sleep(4);
                    flagGreaterOrLess = !flagGreaterOrLess;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            if(values[0])
                ivAliveStar.startAnimation(animAliveStarGreater);
            else
                ivAliveStar.startAnimation(animAliveStarLess);
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
