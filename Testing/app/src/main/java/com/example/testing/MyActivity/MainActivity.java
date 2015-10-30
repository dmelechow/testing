package com.example.testing.MyActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.testing.AsynkTasks.AsyncTaskTools;
import com.example.testing.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    TextView tvFiveSec;
    MyAsyncTask myAsyncTask;
    AsyncTaskTools as;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        tvFiveSec = (TextView) findViewById(R.id.tvFiveSec);

        myAsyncTask = new MyAsyncTask();
        as = new AsyncTaskTools();
        as.execute(myAsyncTask);
    }

    class MyAsyncTask extends AsyncTask<Void,Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 5; i > 0; i--) {
                    publishProgress(i);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tvFiveSec.setText("Осталось " + values[0] + " секунд");
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            startActivity(intent);
        }
    }

}

