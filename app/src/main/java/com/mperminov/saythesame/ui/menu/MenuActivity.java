package com.mperminov.saythesame.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public class MenuActivity extends BaseActivity {
    @BindView(R.id.nicknameTv)
    TextView nickText;
    @BindView(R.id.emailTv)
    TextView emailText;
    @BindView(R.id.photoIv)
    ImageView avatarImage;
    @BindView(R.id.start_random_btn)
    Button randBtn;
    @BindView(R.id.testfield_tv)
    TextView testTextView;
    @Inject
    User user;
    @Inject
    MenuPresenter presenter;

    public static final int REQUEST_COMPLETED = 1003;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        nickText.setText(user.getUsername());
        emailText.setText(user.getEmail());
        Picasso.get()
            .load(user.getPhoto_url())
            .centerCrop()
            .resize((int) getResources().getDimension(R.dimen.avatar_width),
                (int) getResources().getDimension(R.dimen.avatar_height))
            .into(avatarImage);
    }

    @Override
    protected void setupActivityComponent() {
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
        ((ResultReceiver) getIntent().getParcelableExtra("finisher")).
            send(MenuActivity.REQUEST_COMPLETED, new Bundle());
    }

    @OnClick(R.id.start_random_btn)
    public void enterRandomMode() {
        presenter.queueUp();
        randBtn.setEnabled(false);
        randBtn.setClickable(false);
        //presenter.waitForStart();
    }

    public void updateTestField(String text) {
        testTextView.setText(text);
    }

    public void updateNickField(String text) {
        nickText.setText(text);
    }
}
