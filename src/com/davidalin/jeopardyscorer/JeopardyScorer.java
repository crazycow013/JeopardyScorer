package com.davidalin.jeopardyscorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import android.app.AlertDialog;
//import android.content.DialogInterface;



public class JeopardyScorer extends Activity {
  public Integer score = 0;
  public int[] tiles = new int[30];
  public Button[] board = new Button[30];
  public boolean dbl = false;
  public int[] idList = new int[30];
  public int correct = 0;
  public int wrong = 0;
  public Integer highscore = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
          
      // if there is a saved session
      if (savedInstanceState != null) {
        // reinitialize our variables
        score = savedInstanceState.getInt("myscore");
        tiles = savedInstanceState.getIntArray("mytiles");
        dbl = savedInstanceState.getBoolean("mydbl");
        correct = savedInstanceState.getInt("mycorrect");
        wrong = savedInstanceState.getInt("mywrong");
        highscore = savedInstanceState.getInt("myHighScore");
        /*
            if (getIntent() != null) {
	            Intent intent = getIntent();
	            Bundle extras = intent.getExtras();
	            score = extras.getInt("reset");
            }
            */
      }
      // set our content view
      setContentView(R.layout.activity_main);
      
      // keep phone from sleeping
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      
      // set the score to the onscreen textview
      TextView t = (TextView) findViewById(R.id.textView1);
      t.setText(score.toString());
      
      // get the IDs of the buttons
      getIdList();
      
      // set the correct colors and states for the buttons
      for (int i = 0; i < 5; i++) {
        for (int n = 0; n < 6; n++) {
          Button b = (Button) findViewById(idList[i*6+n]);
          if (dbl) {
            if (tiles[i*6+n] == 1) {
              b.setText("+$" + (i + 1)*400);
              b.setBackgroundResource(R.drawable.g_button);
            } else if (tiles[i*6+n] == 2) {
              b.setText("-$" + (i + 1)*400);
              b.setBackgroundResource(R.drawable.r_button);
            } else if (tiles[i*6+n] == 3) {
              b.setText("x$" + (i + 1)*400);
              b.setTextColor(0x00000000);
              //b.setVisibility(0x00000000);
            } else {
              b.setText("$" + (i + 1)*400);
            }
          } else {
            if (tiles[i*6+n] == 1) {
              b.setText("+$" + (i + 1)*200);
              b.setBackgroundResource(R.drawable.g_button);
            } else if (tiles[i*6+n] == 2) {
              b.setText("-$" + (i + 1)*200);
              b.setBackgroundResource(R.drawable.r_button);
            } else if (tiles[i*6+n] == 3) {
              b.setText("x$" + (i + 1)*200);
              b.setTextColor(0x00000000);
            } else {
              
            }
          }
        }
      }
      
      // make the correct double/final jeopardy button
      if (dbl) {
        int test = R.id.dbl;
        Button b = (Button) findViewById(test);
        b.setText("Final Jeopardy");
      }
      
      boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
      if (firstrun){
        // show instructions dialog
        new AlertDialog.Builder(this).setTitle("Instructions").setMessage(R.string.dialog_instructions).setNeutralButton("OK", null).show();
        // Save the state
        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .edit()
            .putBoolean("firstrun", false)
            .commit();
      }
  }

    // creates options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.activity_main, menu);
      return true;
    }
    
    //called when an option is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      // decide which MenuItem was pressed based on its id
      switch(item.getItemId()){
        case R.id.instructions:
          new AlertDialog.Builder(this).setTitle("Instructions").setMessage(R.string.dialog_instructions).setNeutralButton("OK", null).show();
      }
      
      return true;
    }


    
    // prepare to exit application, save our values and button states
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      // get our array of buttons
      getBoard();
      
      // store the int value of the button states (through btoString())
      for (int i = 0; i < 5; i++) {
        for (int n = 0; n < 6; n++) {
          tiles[i*6+n] = btoString(board[i*6+n]);
        }
      }
      
      // save all of our other values
      savedInstanceState.putInt("myscore", score);
      savedInstanceState.putIntArray("mytiles", tiles);
      savedInstanceState.putBoolean("mydbl", dbl);
      savedInstanceState.putInt("mycorrect", correct);
      savedInstanceState.putInt("mywrong", wrong);
      savedInstanceState.putInt("myHighScore", highscore);
      
      super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    protected void onStart() {
      super.onStart();	
    }
    
    @Override
    protected void onStop() {
      super.onStop();
    }
    
    @Override
    protected void onDestroy() {
      super.onDestroy();
    }
    
    public void score(View view) {
      Integer value;
      String theText;
      
      // grab the button that called score
      Button b = new Button(this);
      b = (Button) findViewById(view.getId());
      theText = b.getText().toString();
      
      // toggle through button states and adjust score accordingly
      if (theText.contains("+")) {
        value = Integer.valueOf(b.getText().toString().substring(2));
        b.setText("-$" + value);
        b.setBackgroundResource(R.drawable.r_button);
        score -= 2 * value;
        correct--;
        wrong++;
      } else if (theText.contains("-")) {
        value = Integer.valueOf(b.getText().toString().substring(2));
        b.setText("x$" + value.toString());
        b.setTextColor(0x00000000);
        b.setBackgroundResource(R.drawable.b_button);
        score += value;
        wrong--;
      } else if (theText.startsWith("x")) {
        value = Integer.valueOf(b.getText().toString().substring(2));
        b.setText("$" + value.toString());
        b.setTextColor(0xFFdaa520);
      } else {
        value = Integer.valueOf(b.getText().toString().substring(1));
        b.setText("+$" + value);
        b.setBackgroundResource(R.drawable.g_button);
        score += value;
        correct++;
      }
      
      // update the score on the screen
      TextView t = new TextView(this);
      t = (TextView) findViewById(R.id.textView1);
      t.setText(score.toString());
    }              
    
    public void dbl(View view) {
      getBoard();
      Button b = (Button) findViewById(R.id.dbl);
      
      // double our score values for double jeopardy and reset colors
      if (b.getText().toString().equals("Double Jeopardy")) {
        for (int i = 0; i < 5; i++) {
          for (int n = 0; n < 6; n++) {
            board[i*6+n].setText("$" + (i + 1)*400);
            board[i*6+n].setTextColor(0xFFdaa520);
            board[i*6+n].setBackgroundResource(R.drawable.b_button);
          }
        }
        b.setText("Final Jeopardy");
        dbl = true;
      } else {
        finalJeopardy(view);
      }
    }
    
    // make an array of our buttons. should be a cleaner way to do this
    public void getBoard() {
      board[0] = (Button) findViewById(R.id.r1b1);
      board[1] = (Button) findViewById(R.id.r1b2);
      board[2] = (Button) findViewById(R.id.r1b3);
      board[3] = (Button) findViewById(R.id.r1b4);
      board[4] = (Button) findViewById(R.id.r1b5);
      board[5] = (Button) findViewById(R.id.r1b6);
      board[6] = (Button) findViewById(R.id.r2b1);
      board[7] = (Button) findViewById(R.id.r2b2);
      board[8] = (Button) findViewById(R.id.r2b3);
      board[9] = (Button) findViewById(R.id.r2b4);
      board[10] = (Button) findViewById(R.id.r2b5);
      board[11] = (Button) findViewById(R.id.r2b6);
      board[12] = (Button) findViewById(R.id.r3b1);
      board[13] = (Button) findViewById(R.id.r3b2);
      board[14] = (Button) findViewById(R.id.r3b3);
      board[15] = (Button) findViewById(R.id.r3b4);
      board[16] = (Button) findViewById(R.id.r3b5);
      board[17] = (Button) findViewById(R.id.r3b6);
      board[18] = (Button) findViewById(R.id.r4b1);
      board[19] = (Button) findViewById(R.id.r4b2);
      board[20] = (Button) findViewById(R.id.r4b3);
      board[21] = (Button) findViewById(R.id.r4b4);
      board[22] = (Button) findViewById(R.id.r4b5);
      board[23] = (Button) findViewById(R.id.r4b6);
      board[24] = (Button) findViewById(R.id.r5b1);
      board[25] = (Button) findViewById(R.id.r5b2);
      board[26] = (Button) findViewById(R.id.r5b3);
      board[27] = (Button) findViewById(R.id.r5b4);
      board[28] = (Button) findViewById(R.id.r5b5);
      board[29] = (Button) findViewById(R.id.r5b6);
    }
    
    // make an array of our button ids. should be a cleaner way to do this
    public void getIdList() {
      idList[0] = R.id.r1b1;
      idList[1] = R.id.r1b2;
      idList[2] = R.id.r1b3;
      idList[3] = R.id.r1b4;
      idList[4] = R.id.r1b5;
      idList[5] = R.id.r1b6;
      idList[6] = R.id.r2b1;
      idList[7] = R.id.r2b2;
      idList[8] = R.id.r2b3;
      idList[9] = R.id.r2b4;
      idList[10] = R.id.r2b5;
      idList[11] = R.id.r2b6;
      idList[12] = R.id.r3b1;
      idList[13] = R.id.r3b2;
      idList[14] = R.id.r3b3;
      idList[15] = R.id.r3b4;
      idList[16] = R.id.r3b5;
      idList[17] = R.id.r3b6;
      idList[18] = R.id.r4b1;
      idList[19] = R.id.r4b2;
      idList[20] = R.id.r4b3;
      idList[21] = R.id.r4b4;
      idList[22] = R.id.r4b5;
      idList[23] = R.id.r4b6;
      idList[24] = R.id.r5b1;
      idList[25] = R.id.r5b2;
      idList[26] = R.id.r5b3;
      idList[27] = R.id.r5b4;
      idList[28] = R.id.r5b5;
      idList[29] = R.id.r5b6;
    }
    
    // make an int value from the button state
    public int btoString (Button b) {
      if (b.getText().toString().startsWith("$")) {
        return 0;
      } else if (b.getText().toString().startsWith("+")) {
        return 1;
      } else if (b.getText().toString().startsWith("-")) {
        return 2;
      } else if (b.getText().toString().startsWith("x")) {
        return 3;
      } else {
        return -1;
      }
    }
    
    public void finalJeopardy(View view) {
      // package our necessary info
      Bundle dataBundle = new Bundle();
      dataBundle.putInt("myScore", score);
      dataBundle.putInt("mycorrect", correct);
      dataBundle.putInt("mywrong", wrong);
      dataBundle.putInt("myHighScore", highscore);
      Intent intent = new Intent(this, FinalJeopardyActivity.class);
      intent.putExtras(dataBundle);
      startActivity(intent);
    }
 
    public void dailyDoubleRight(View view) {	
      EditText editText = (EditText) findViewById(R.id.editText1);
      String temp = editText.getText().toString();
      
      if (! temp.equals("") && 
          temp.length() <= 9) {
        score += Integer.valueOf(editText.getText().toString());
        TextView t = new TextView(this);
        t = (TextView) findViewById(R.id.textView1);
        t.setText(score.toString());
        editText.setText("");
      } else {
        editText.setText("");
      }
    }
    
    public void dailyDoubleWrong(View view) {
      EditText editText = (EditText) findViewById(R.id.editText1);
      String temp = editText.getText().toString();
      
      if (! temp.equals("") && 
          temp.length() <= 9) {
        score -= Integer.valueOf(editText.getText().toString());
        TextView t = new TextView(this);
        t = (TextView) findViewById(R.id.textView1);
        t.setText(score.toString());
        editText.setText("");
      } else {
        editText.setText("");
      }
    }              
}
