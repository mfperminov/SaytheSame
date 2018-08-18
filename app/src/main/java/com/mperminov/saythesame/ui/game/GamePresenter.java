package com.mperminov.saythesame.ui.game;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.data.utils.Answer;
import java.util.HashMap;
import java.util.Map;

public class GamePresenter implements BasePresenter {
    public static final int MAXIMUM_TURNS = 10;
    private GameActivity mActivity;
    private User mUser;
    private Answer curRivalAnswer;
    private Answer myCurAnswer;
    private Rival mRival;
    private CountDownTimer ctTimer;
    int turnCount = 1;
    private boolean hasGuess;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseFunctions mFunctions;


    public GamePresenter(GameActivity activity, User user, Rival rival) {
        mActivity = activity;
        mUser = user;
        mRival = rival;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    public void sendGuess(final CharSequence text) {
        myCurAnswer.setCurAnswer(text.toString());
        hasGuess = true;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                writeAGuess(text).addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!TextUtils.isEmpty(curRivalAnswer.getCurAnswer()))
                            endTurn();
                    }
                });
            }
        });

    }

    private Task<String> writeAGuess(CharSequence guess) {
        mFunctions = FirebaseFunctions.getInstance("us-central1");
        Map<String, Object> data = new HashMap<>();
        data.put("text",  guess.toString());
        data.put("playerUid", mUser.getUid());
        data.put("gameId",mRival.getGameId());
        data.put("turnNumber",turnCount);

        return mFunctions
            .getHttpsCallable("writeAGuess")
            .call(data)
            .continueWith(new Continuation<HttpsCallableResult, String>() {
                @Override
                public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                    String result = "null";
                    try {
                        result = (String) task.getResult().getData();
                    } catch (Exception e) {
                        Log.e("Functions exception",e.getMessage());
                    }
                    return result;
                }
            });
    }

    public void startTurn() {
        hasGuess = false;
        curRivalAnswer = new Answer();
        myCurAnswer = new Answer();
        curRivalAnswer.setListener(new Answer.ChangeListener() {
            @Override public void onChange() {
                if(!TextUtils.isEmpty(myCurAnswer.getCurAnswer()))
                    endTurn();
            }
        });
        if (turnCount >= MAXIMUM_TURNS) {
            breakGame();
        }
            Toast.makeText(mActivity, "Turn " + turnCount, Toast.LENGTH_SHORT).show();
            setTimer();
            setListenerOnRivalWord();
    }

    private void endTurn() {
        if(myCurAnswer.getCurAnswer().equals(curRivalAnswer.getCurAnswer())) {
            mActivity.showGameWin(myCurAnswer.getCurAnswer());
        } else {
            turnCount++;
            if(ctTimer!=null) ctTimer.cancel();
            mActivity.shiftWords(curRivalAnswer.getCurAnswer());
            startTurn();
        }
    }

    private void setListenerOnRivalWord() {
        DatabaseReference dbRef = db.getReference("/games_data/"+mRival.getGameId()+"/"
            +turnCount);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mRival.getUid())) {
                    String curRivalWord = dataSnapshot.child(mRival.getUid())
                        .getValue().toString();
                    curRivalAnswer.setCurAnswer(curRivalWord);
                    //mActivity.updateLastRivalWord(curRivalWord);
                }

            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setTimer() {
        ctTimer = new CountDownTimer(45000,1000) {
            @Override public void onTick(long l) {
                mActivity.setSecondsOnTimer(l/1000);
            }

            @Override public void onFinish() {
                if(!hasGuess || TextUtils.isEmpty(curRivalAnswer.getCurAnswer())){
                    mActivity.showTimeOver();
                    breakGame();
                }

            }
        };
        ctTimer.start();
    }

    private void breakGame() {
        //Show YOU FAILED
        //exitTO MAIN MENU
    }


}
