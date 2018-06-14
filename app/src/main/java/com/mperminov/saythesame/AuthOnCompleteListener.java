package com.mperminov.saythesame;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mperminov.saythesame.ui.menu.MenuActivity;

public class AuthOnCompleteListener implements OnCompleteListener<AuthResult> {
  private FirebaseAuth mAuth;
  private Context mContext;

  public AuthOnCompleteListener(Context c) {
    mContext = c;
  }

  @Override public void onComplete(@NonNull Task<AuthResult> task) {
    if (task.isSuccessful()) {
      // Sign in success, update UI with the signed-in user's information
      Log.d("EnterActivity", "signInWithCredential:success");
      FirebaseUser user = mAuth.getInstance().getCurrentUser();
      mContext.startActivity(new Intent(mContext, MenuActivity.class));
    } else {
      // If sign in fails, display a message to the user.
      Log.d("EnterActivity", "signInWithCredential:failure", task.getException());
      Toast.makeText(mContext, "pizda", Toast.LENGTH_SHORT).show();
      Log.d("toast","show");
    }
  }
}
