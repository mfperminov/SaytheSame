package com.mperminov.saythesame.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.mperminov.saythesame.data.utils.NetworkInfoBroadcastReceiver;

public abstract class BaseActivity extends AppCompatActivity {
  private NetworkInfoBroadcastReceiver br;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    setupActivityComponent();
    super.onCreate(savedInstanceState);
  }

  protected abstract void setupActivityComponent();

  public void showNoInternet() {
    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
        "No Internet connection", Snackbar.LENGTH_INDEFINITE);
    View view = snackbar.getView();
    view.setBackgroundColor(Color.RED);
    snackbar.show();
  }

  public void backOnline() {
    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
        "Back online!", Snackbar.LENGTH_SHORT);
    View view = snackbar.getView();
    view.setBackgroundColor(Color.GREEN);
    snackbar.show();
  }
}
