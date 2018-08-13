package com.mperminov.saythesame.ui.game;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class GamePresenter implements BasePresenter {
    public static final int MAXIMUM_TURNS = 10;
    private GameActivity mActivity;
    private User mUser;
    private Rival mRival;
    int turnCount = 1;
    CountDownTimer ctTimer;
    private boolean hasGuess;

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
        hasGuess = true;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                writeAGuess(text).addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        Log.i("Guess is wrote","presenter");
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
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
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
        if (turnCount >= MAXIMUM_TURNS) {
            breakGame();
        }
            Toast.makeText(mActivity, "Turn" + turnCount, Toast.LENGTH_SHORT).show();
            setTimer();
            turnCount++;

    }

    private void setTimer() {
        ctTimer = new CountDownTimer(45000,1000) {
            @Override public void onTick(long l) {
                mActivity.setSecondsOnTimer(l/1000);
            }

            @Override public void onFinish() {
                if(hasGuess) mActivity.showWatingForRivalGuess();
                else breakGame();
            }
        };
        ctTimer.start();
    }

    private void breakGame() {
        //Show YOU FAILED
        //exitTO MAIN MENU
    }
}
