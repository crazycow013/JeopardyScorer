package com.davidalin.jeopardyscorer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FinalJeopardyActivity extends Activity {
  public Integer score = 0;
  public Integer highscore = 0;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    
    // get our data bundle
    Bundle extras = intent.getExtras();
    score = extras.getInt("myScore");
    //highscore = extras.getInt("myHighScore");
    Integer correct = extras.getInt("mycorrect");
    Integer wrong = extras.getInt("mywrong");
    
    // get our HS from prefs
    SharedPreferences prefs = this.getSharedPreferences("myHighScore", Context.MODE_PRIVATE);
    highscore = prefs.getInt("myHighScore", highscore);
    
    setContentView(R.layout.final_jeopardy);
    TextView t = new TextView(this);
    t = (TextView) findViewById(R.id.textView3);
    t.setText(score.toString());
    t = (TextView) findViewById(R.id.numcorrect);
    t.setText(correct.toString());
    t = (TextView) findViewById(R.id.numwrong);
    t.setText(wrong.toString());
    t = (TextView) findViewById(R.id.numhighscore);
    t.setText(highscore.toString());
    
  }
    
  public void finalRight(View view) {	
    EditText editText = (EditText) findViewById(R.id.editText1);
    if (editText.getText().toString().equals("")) {
      
    } else {
      score += Integer.valueOf(editText.getText().toString());
      TextView t = new TextView(this);
      t = (TextView) findViewById(R.id.textView5);
      t.setText(score.toString());
      t.setVisibility(0x00000000);
      if (score > highscore) {
        t = (TextView) findViewById(R.id.numhighscore);
        t.setText(score.toString());
        highscore = score;
      }
    }
  }
    
  public void finalWrong(View view) {
    EditText editText = (EditText) findViewById(R.id.editText1);
    if (editText.getText().toString().equals("")) {
      
    } else {
      score -= Integer.valueOf(editText.getText().toString());
      TextView t = new TextView(this);
      t = (TextView) findViewById(R.id.textView5);
      t.setText(score.toString());
      t.setVisibility(0x00000000);
      if (score > highscore) {
        t = (TextView) findViewById(R.id.numhighscore);
        t.setText(score.toString());
        highscore = score;
      }
    }
  }
    
  public void newGame(View view) {
    finish();
    // old code? don't think I need reset anymore TODO
    /*
    Bundle dataBundle = new Bundle();
    dataBundle.putInt("myHighScore", highscore);
    */
    SharedPreferences prefs = this.getSharedPreferences("myHighScore", Context.MODE_PRIVATE);
    Editor editor = prefs.edit();
    editor.putInt("myHighScore", highscore);
    editor.commit();
    Intent intent = new Intent(this, JeopardyScorer.class);
    //intent.putExtras(dataBundle);
    startActivity(intent);
  }    
}
