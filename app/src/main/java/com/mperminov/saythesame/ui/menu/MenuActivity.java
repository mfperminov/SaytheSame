package com.mperminov.saythesame.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.User;
import javax.inject.Inject;

public class MenuActivity extends BaseActivity {
  @BindView(R.id.test)
  TextView testTextView;
  @Inject User user;

  public static final int REQUEST_COMPLETED = 1003;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    ButterKnife.bind(this);
    testTextView.setText(user.getUsername());
  }

  @Override protected void setupActivityComponent() {
    BaseApplication.get(this)
        .getUserComponent()
        .plus(new MenuActivityModule(this))
        .inject(this);
  }

  public static void startWithUser(final BaseActivity activity, final User user) {
    //переход из активити ( возможно логин - возможно сплеш активити )
    Intent intent = new Intent(activity, MenuActivity.class);
    // в интент кладется резльтат рецивер, т.е. когда активити из которого мы уйдем
    // получит резльутат, то оно финиширует
    intent.putExtra("finisher", new ResultReceiver(null) {
      @Override
      protected void onReceiveResult(int resultCode, Bundle resultData) {
        activity.finish();
      }
    });
    BaseApplication.get(activity).createUserComponent(user);
    // стартуем интент (ИЗ СЛПЕШ АКТИВИТИ ИЛИ ЛОГИН АКТИВИТИ)
    activity.startActivityForResult(intent, REQUEST_COMPLETED);
  }

  public void sendMessageToBreakPreviousScreen() {
    // когда получили интент здесь, то берем РЕзалтРесивер и его методом send()
    // шлем реквест код и исходное активити (логин или сплеш) финиширует
    ((ResultReceiver)getIntent().getParcelableExtra("finisher")).
        send(MenuActivity.REQUEST_COMPLETED, new Bundle());
  }
}
