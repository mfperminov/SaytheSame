package com.mperminov.saythesame.data.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mperminov.saythesame.base.BaseActivity;

public class NetworkInfoBroadcastReceiver extends BroadcastReceiver {

  @Override public void onReceive(Context context, Intent intent) {
    BaseActivity ba = (BaseActivity) context;
    if (isOnline(context)) {
      ba.backOnline();
    } else {
      ba.showNoInternet();
    }
  }

  private boolean isOnline(Context context) {
    try {
      ConnectivityManager cm = (ConnectivityManager) context
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = cm.getActiveNetworkInfo();
      return (netInfo != null && netInfo.isConnected());
    } catch (NullPointerException e) {
      e.printStackTrace();
      return false;
    }
  }

  //public void registerNetworkCallbacks(final Context context) {
  //  ConnectivityManager cm = (ConnectivityManager) context
  //      .getSystemService(Context.CONNECTIVITY_SERVICE);
  //
  //  NetworkRequest.Builder builder = new NetworkRequest.Builder();
  //  cm.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
  //    @Override public void onAvailable(Network network) {
  //      super.onAvailable(network);
  //      BaseActivity ba = (BaseActivity) context;
  //      ba.backOnline();
  //    }
  //
  //    @Override public void onLost(Network network) {
  //      super.onLost(network);
  //      BaseActivity ba = (BaseActivity) context;
  //      ba.showNoInternet();
  //    }
  //  });
  //}
}

