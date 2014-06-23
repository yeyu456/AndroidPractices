package com.yeyu1;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.app.Notification;
import android.os.Bundle;

import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.ToggleButton;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {
    CheckBox ck;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ck = ((CheckBox) findViewById(R.id.checkbox_test));
        ListView lv = (ListView) findViewById(R.id.lv);
        String[] listForView = {"Ò»", "¶þ"};
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listForView);
        lv.setAdapter(ad);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(mMultiChoice);
    }
    
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_1, popup.getMenu());
        popup.show();
    }
    
    private MultiChoiceModeListener mMultiChoice = new MultiChoiceModeListener(){
        
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked){
        
    }
    
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu){
        MenuInflater mf = mode.getMenuInflater();
        mf.inflate(R.menu.menu_1, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
    }
    
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item){
        System.out.println("set true");
        switch(item.getItemId()){
            case R.id.menuItem1 :
                setCheck(true);
                mode.finish();
                return true;
            case R.id.menuItem2 :
                setCheck(false);
                mode.finish();
                return true;
            default:
                return false;
        }
    }
    
    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }
    };
    
    public void onCheckboxClicked(View view){
        boolean isCheck = ((CheckBox) view).isChecked();
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.country);
        String[] countries = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter = 
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        if(isCheck){
            textView.setAdapter(adapter);
        }
        else
            textView.setAdapter(null);
    }
    
    
    public void onRadioButtonClicked(View view){
        switch(view.getId()){
            case R.id.radio1 :
                ck.setChecked(true);
                break;
            case R.id.radio2 :
                ck.setChecked(false);
                this.notif();
                break;
            default :
                return;
        }
    }
    
    public void onToggleButtonClicked(View view){
        boolean on = ((ToggleButton) view).isChecked();
        this.setCheck(on);
    }
    
    private void setCheck(boolean isOrNot){
        if(isOrNot){
            ck.setChecked(true);
            this.onCheckboxClicked(ck);
            //new Thread(new Runnable() {
                //@Override
                //public void run() {
                    Context context = getApplicationContext();
                    CharSequence text = "Hello toast!";
                    Toast toast = Toast.makeText(context, text, 2000);
                    toast.show();
                //}
            //}).start();
        }
        else{
            ck.setChecked(false);
            this.onCheckboxClicked(ck);
        }
    }
    
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
    
    public void spinnerTest(){
        Spinner sp = (Spinner) findViewById(R.id.spinner_test);
        String[] spString = getResources().getStringArray(R.array.spinner_array);
        ArrayAdapter<String> spAdapter = 
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spString);
        sp.setAdapter(spAdapter);
        
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, 
                    int pos, long id) {
                boolean isNo;
                switch(parent.getItemAtPosition(pos).toString()){
                    case "1" :
                        isNo = true;
                        break;
                    case "2" :
                        isNo = false;
                        break;
                    default :
                        isNo = false;
                }
                if(isNo){
                    ck.setChecked(true);
                }
                else{
                    ck.setChecked(false);
                }
            }
            
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
    
    public void notif(){
        Intent resultIntent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, resultIntent, 0);
        
        final Notification.Builder mBuilder =
                new Notification.Builder(this)
                .setContentIntent(pendingIntent)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setSmallIcon(R.drawable.images);
        // Creates an explicit intent for an Activity in your app
        final NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (incr = 0; incr <= 100; incr+=5) {
                                // Sets the progress indicator to a max value, the
                                // current completion percentage, and "determinate"
                                // state
                                mBuilder.setProgress(100, incr, false);
                                // Displays the progress bar for the first time.
                                mNotificationManager.notify(0, mBuilder.build());
                                    // Sleeps the thread, simulating an operation
                                    // that takes time
                                    try {
                                        // Sleep for 5 seconds
                                        Thread.sleep(5*1000);
                                    } catch (InterruptedException e) {
                                        System.out.println("sleep failure");
                                    }
                        }
                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")
                        // Removes the progress bar
                                .setProgress(0,0,false);
                        Notification n  = mBuilder.build();
                        n.flags = Notification.FLAG_AUTO_CANCEL;
                        mNotificationManager.notify(1, n);
                    }
                }
            // Starts the thread by calling the run() method in its Runnable
            ).start();
    }
}
