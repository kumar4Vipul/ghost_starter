/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    static final String STATE_USER_TURN = "USER_TURN";
    static final String STATE_GHOST_TEXT = "GHOST_TEXT";
    static final String STATE_GAME_STATUS = "GAME_STATUS";

    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

     private TextView GamestatusView;
     private TextView Ghosttextview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        GamestatusView=(TextView)findViewById(R.id.gameStatus);
        Ghosttextview=(TextView)findViewById(R.id.ghostText);

        AssetManager assetManager = getAssets();
        try{
            InputStream inputStream=assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);

        }catch (IOException e){
            Toast toast=Toast.makeText(this,"Could not load Dictionary",Toast.LENGTH_LONG);
            toast.show();

        }
        onStart(null);
        findViewById(R.id.ButtonReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStart(view);

            }
        });
        findViewById(R.id.Buttonchallenge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ghosttext=Ghosttextview.getText().toString();
                if((ghosttext.length() >=4)&&(dictionary.isWord(ghosttext))){
                    GamestatusView.setText("User won");

                }
                else {
                    String k= dictionary.getAnyWordStartingWith(ghosttext);
                    Log.d("Ghost","Word Starting with "+ ghosttext +"is" +k);
                    if(k==null){
                        GamestatusView.setText("User won");
                    }else{
                        GamestatusView.setText("Computer won | possible word"+k);
                    }
                }
            }
        });
        /**
         **  YOUR CODE GOES HERE
         **/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }



    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(userTurn)
        {
            userTurn=false;
            char k=(char)event.getUnicodeChar();
            if(Character.isLetter(k)){
                Ghosttextview.append(k+"");
                userTurn=false;
                computerTurn();
                return true;
            }
        }
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        return super.onKeyUp(keyCode, event);
    }
    private void computerTurn() {
//        TextView label = (TextView) findViewById(R.id.gameStatus);
        String ghostText = Ghosttextview.getText().toString();
        Log.d("Ghost", "Word: " + ghostText);

        if (ghostText.length() >= 4 && dictionary.isWord(ghostText)) {
            GamestatusView.setText("Computer Won");
        } else {
            String k = dictionary.getAnyWordStartingWith(ghostText);
            Log.d("Ghost", "Word starting with " + ghostText + " is " + k);

            if(k == null) {
                GamestatusView.setText("Computer Won");
            } else {
                Ghosttextview.append(k.charAt(ghostText.length()) + "");
                userTurn = true;
                GamestatusView.setText(USER_TURN);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(STATE_USER_TURN, userTurn);
        outState.putString(STATE_GHOST_TEXT,Ghosttextview.getText().toString());
        outState.putString(STATE_GAME_STATUS,GamestatusView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userTurn = savedInstanceState.getBoolean(STATE_USER_TURN);
        Ghosttextview.setText(savedInstanceState.getString(STATE_GHOST_TEXT));
        GamestatusView.setText(savedInstanceState.getString(STATE_GAME_STATUS));
    }
}
