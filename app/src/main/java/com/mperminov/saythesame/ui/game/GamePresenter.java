package com.mperminov.saythesame.ui.game;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.mperminov.saythesame.base.BasePresenter;
import com.mperminov.saythesame.data.model.Answer;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

public class GamePresenter implements BasePresenter {
  private static final int MAXIMUM_TURNS = 10;
  private static final String GUESS_WRITE_FUNCTION = "writeAGuess";
  private static final String NO_TURNS_LEFT = "The maximum turns value has been reached";
  private static final String GAME_WIN = "You are winners! Congratulation!";
  private static final String TIME_OVER_FOR_RIVAL = "Your rival has no guess, you both lose";
  private static final String TIME_OVER_FOR_PLAYER = "You has no guess, you both lose";
  private GameActivity mActivity;
  private User mUser;
  private Answer curRivalAnswer;
  private Answer myCurAnswer;
  private Rival mRival;
  private CountDownTimer ctTimer;
  private int turnCount = 1;
  private boolean hasGuess;
  private FirebaseDatabase db = FirebaseDatabase.getInstance();

  GamePresenter(GameActivity activity, User user, Rival rival) {
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
            if (!TextUtils.isEmpty(curRivalAnswer.getCurAnswer())) {
              endTurn();
            }
          }
        });
      }
    });
  }

  public void startTurn() {
    hasGuess = false;
    curRivalAnswer = new Answer();
    myCurAnswer = new Answer();
    curRivalAnswer.setListener(new Answer.ChangeListener() {
      @Override public void onChange() {
        if (!TextUtils.isEmpty(myCurAnswer.getCurAnswer())) {
          endTurn();
        }
      }
    });
    if (turnCount >= MAXIMUM_TURNS) {
      breakGame(NO_TURNS_LEFT);
    }
    Toast.makeText(mActivity, "Turn " + turnCount, Toast.LENGTH_SHORT).show();
    setTimer();
    setListenerOnRivalWord();
  }

  private void setTimer() {
    ctTimer = new CountDownTimer(45000, 1000) {
      @Override public void onTick(long l) {
        mActivity.setSecondsOnTimer(l / 1000);
      }

      @Override public void onFinish() {
        if (!hasGuess || TextUtils.isEmpty(curRivalAnswer.getCurAnswer())) {
          mActivity.showTimeOver();
          breakGame(TIME_OVER_FOR_PLAYER);
        }
      }
    };
    ctTimer.start();
  }

  private void setListenerOnRivalWord() {
    DatabaseReference dbRef = db.getReference("/games_data/" + mRival.getGameId() + "/"
        + turnCount);
    dbRef.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChild(mRival.getUid())) {
          String curRivalWord = dataSnapshot.child(mRival.getUid())
              .getValue().toString();
          curRivalAnswer.setCurAnswer(curRivalWord);
        }
      }

      @Override public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }

  private Task<String> writeAGuess(CharSequence guess) {
    FirebaseFunctions mFunctions = FirebaseFunctions.getInstance("us-central1");
    Map<String, Object> data = new HashMap<>();
    data.put("text", guess.toString());
    data.put("playerUid", mUser.getUid());
    data.put("gameId", mRival.getGameId());
    data.put("turnNumber", turnCount);

    return mFunctions
        .getHttpsCallable(GUESS_WRITE_FUNCTION)
        .call(data)
        .continueWith(task -> {
          String result = "null";
          try {
            result = (String) task.getResult().getData();
          } catch (Exception e) {
            Log.e("Functions exception", e.getMessage());
          }
          return result;
        });
  }

  private void endTurn() {
    if (myCurAnswer.getCurAnswer().equals(curRivalAnswer.getCurAnswer())) {
      mActivity.showGameWin(myCurAnswer.getCurAnswer());
      breakGame(GAME_WIN);
    } else {
      turnCount++;
      if (ctTimer != null) ctTimer.cancel();
      mActivity.shiftWords(curRivalAnswer.getCurAnswer());
      startTurn();
    }
  }

  private void breakGame(@ReasonsGameEnds String reason) {
    mActivity.showGameResult(reason);
  }

  @Retention(RetentionPolicy.CLASS)
  @StringDef({
      NO_TURNS_LEFT,
      GAME_WIN,
      TIME_OVER_FOR_RIVAL,
      TIME_OVER_FOR_PLAYER
  })
  private @interface ReasonsGameEnds {
  }
}
