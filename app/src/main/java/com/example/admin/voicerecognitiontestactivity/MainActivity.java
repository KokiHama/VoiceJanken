package com.example.admin.voicerecognitiontestactivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE = 1000;
    private TextView textView;
    private TextView CPUHand;
    private TextView result;
    private Button buttonStart;

    SoundPool sp;
    int sound_id[]=new int[2];


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 認識結果を表示させる
        textView = (TextView) findViewById(R.id.text_view);//自分の手の状態
        CPUHand = (TextView) findViewById(R.id.CPUHand);//相手の手の状態
        result = (TextView) findViewById(R.id.result);//結果表示

        buttonStart = (Button) findViewById(R.id.button_start);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 音声認識を開始
                speech();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        sp       = new SoundPool( 2, AudioManager.STREAM_MUSIC, 0 );
        sound_id[0] = sp.load(this, R.raw.success, 1 );
        sound_id[1] = sp.load(this, R.raw.failed, 1 );

    }


    private void speech() {
        // 音声認識が使えるか確認する
        try {
            // 音声認識の　Intent インスタンス
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を入力");
            // インテント発行
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            textView.setText("No Activity ");
        }

    }

    // 結果を受け取るために onActivityResult を設置
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Speech", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 認識結果を ArrayList で取得
            ArrayList<String> candidates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (candidates.size() > 0) {
                // 認識結果候補で一番有力なものを表示
                int yourState=-1;

                for(int i=0;i<3;i++){
                    if(candidates.get(i).equals("グー")||candidates.get(i).equals("goo")){
                        textView.setText("グー");//gooと判定されることが多いため
                        yourState=0;
                    }
                    else if(candidates.get(i).equals("チョキ")){
                        textView.setText(candidates.get(i));
                        yourState=1;
                    }
                    else if(candidates.get(i).equals("パー")){
                        textView.setText(candidates.get(i));
                        yourState=2;
                    }
                    System.out.println(candidates.get(i));
                }
                GameResult(yourState);//ジャンケンの結果表示
            }
        }
    }

    void GameResult(int yourState) {//ジャンケンの結果表示
        int CPUState=(int)(Math.random()*3);
        if(CPUState==0){//コンピュータの手
            CPUHand.setText("グー");
        }
        else if(CPUState==1){
            CPUHand.setText("チョキ");
        }
        else{
            CPUHand.setText("パー");
        }
        if(yourState==-1){//認識結果でジャンケン以外の用語を話した場合スルー
            CPUHand.setText("CPU Hand");
        }

        if(CPUState==yourState){//勝敗の判定
            result.setTextColor(Color.BLACK);
            result.setText("あいこで…");
        }
        else if((CPUState==1&&yourState==0)||(CPUState==2&&yourState==1)||(CPUState==0&&yourState==2)){
            result.setTextColor(Color.BLUE);
            result.setText("YOU WIN");
            sp.play(sound_id[0], 1.0F, 1.0F, 0, 0, 1.0F);
        }
        else if(yourState==-1){
            textView.setText("Your Hand");
            result.setTextColor(Color.BLACK);
            result.setText("Speak again!");

        }
        else{
            result.setText("YOU LOSE");
            result.setTextColor(Color.RED);
            sp.play(sound_id[1], 1.0F, 1.0F, 0, 0, 1.0F);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.admin.voicerecognitiontestactivity/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.admin.voicerecognitiontestactivity/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}