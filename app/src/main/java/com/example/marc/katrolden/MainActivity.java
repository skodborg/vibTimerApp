package com.example.marc.katrolden;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class MainActivity extends ActionBarActivity implements OnClickListener {

    private Queue<CountDownTimer> _timersQueue = new LinkedList<CountDownTimer>();
    private CountDownTimer _currentTimer;
    private ArrayAdapter<String> adapter;

    private Button starttimer_btn;
    private Button cancel_btn;
    private Button addtimer_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        // initialize buttons and add click listeners
        starttimer_btn = (Button) findViewById(R.id.starttimer_btn);
        starttimer_btn.setOnClickListener(this);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(this);
        addtimer_btn = (Button) findViewById(R.id.addTimer_btn);
        addtimer_btn.setOnClickListener(this);

        // init listview and bind it to content of _timersQueue
        adapter = new ArrayAdapter<String>(this,
                                           android.R.layout.simple_list_item_1,
                                           (List) _timersQueue);
        ListView timers_listview = (ListView) findViewById(R.id.timers_listview);
        timers_listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.starttimer_btn:
                // disable starttimer_btn and start first timer
                startTimerClickHandler();
                break;
            case R.id.cancel_btn:
                // cancel timers
                cancelClickHandler();
                break;
            case R.id.addTimer_btn:
                // open dialog for adding timers to _timersQueue
                addTimerClickHandler();
                break;
            default:
                break;
        }
    }

    private void addTimerClickHandler() {
        AddTimeDialogFragment dialog = new AddTimeDialogFragment();
        dialog.show(getFragmentManager(), "tag");
    }

    private void startTimerClickHandler() {
        starttimer_btn.setClickable(false);
        startNextTimer();
    }

    private void cancelClickHandler() {
        if (_currentTimer != null) { _currentTimer.cancel(); }
        for (CountDownTimer cdt : _timersQueue) {
            cdt.cancel();
        }
        _timersQueue.clear();

        // reset TextView reflecting timer countdown
        TextView timer = (TextView) findViewById(R.id.timerTextView);
        timer.setText("00:00");

        // enable starttimer_btn once again
        starttimer_btn.setClickable(true);
    }

    public void addNewTimer(final int millisecCountdown) {
        final TextView timerView = (TextView) findViewById(R.id.timerTextView);
        final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        CountDownTimer timer = new CountDownTimer(millisecCountdown, 500) {

            public void onTick(long millisUntilFinished) {
                if (!(millisUntilFinished < millisecCountdown)) { millisUntilFinished--; }
                String remaining = convertToTimeString((int) millisUntilFinished);
                timerView.setText(remaining);
            }

            public void onFinish() {
                timerView.setText("00:00");
                // Vibrate for 1000 milliseconds
                vib.vibrate(1000);
                startNextTimer();
            }

            @Override
            public String toString() {
                return convertToTimeString(millisecCountdown);
            }
        };

        _timersQueue.add(timer);

        // let ArrayAdapter know to update the ListView reflecting
        // the list of timers
        adapter.notifyDataSetChanged();
    }

    // returns string with format mm:ss
    private String convertToTimeString(int millisecs) {
        int mins = (millisecs / 1000) / 60;
        int secs = (millisecs / 1000) % 60;

        String str_mins = (mins < 10) ? "0"+mins : ""+mins;
        String str_secs = (secs < 10) ? "0"+secs : ""+secs;
        return str_mins + ":" + str_secs;
    }

    private void startNextTimer() {
        _currentTimer = _timersQueue.poll();
        if (_currentTimer != null) {
            _currentTimer.start();
        } else {
            starttimer_btn.setClickable(true);
        }
        adapter.notifyDataSetChanged();
    }
}

