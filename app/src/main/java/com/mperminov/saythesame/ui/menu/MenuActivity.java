package com.mperminov.saythesame.ui.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.base.BaseApplication;
import com.mperminov.saythesame.data.model.Friend;
import com.mperminov.saythesame.data.model.Rival;
import com.mperminov.saythesame.data.model.User;
import com.mperminov.saythesame.ui.Login.LoginActivity;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public class MenuActivity extends BaseActivity {
    @BindView(R.id.nicknameTv)
    TextView nickText;
    @BindView(R.id.emailTv)
    TextView emailText;
    @BindView(R.id.photoIv)
    ImageView avatarImage;
    @BindView(R.id.recycler_view)
    RecyclerView rvUserList;
    @BindView(R.id.start_random_btn)
    Button randBtn;
    @BindView(R.id.testfield_tv)
    TextView testTextView;
    @BindView(R.id.add_friend_btn)
    Button friendAddBtn;
    @BindView(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    @Inject
    User user;
    @Inject
    MenuPresenter presenter;
    @Inject
    MenuAdapter menuAdapter;
    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    AlertDialog.Builder addAlertDialog;

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
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override protected void onStop() {
        super.onStop();
        menuAdapter.clearList();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                showSettings();
                return true;
            case R.id.menu_logout:
                presenter.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettings() {
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
    }

    public void updateTestField(String text) {
        testTextView.setText(text);
    }

    public void updateNickField(String text) {
        nickText.setText(text);
    }

    public void showFriendList() {
        rvUserList.setAdapter(menuAdapter);
        rvUserList.setLayoutManager(linearLayoutManager);
    }

    public void showAddedFriend(Friend friend) {
        menuAdapter.onFriendAdded(friend);
    }

    public void showChangedFriend(Friend friend) {
        //TODO friends changed
    }

    public void showNotExistFriend(String username) {
        Toast.makeText(this, "User " + username + " was not found",
            Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.add_friend_btn)
    public void onClickAdd() {
        addAlertDialog.setTitle("Insert your friend nickname");
        addAlertDialog.setMessage("We will find'em for sure");

        final EditText etLogin = new EditText(this);
        etLogin.setSingleLine();
        addAlertDialog.setView(etLogin);

        addAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Boolean friendUnique =
                    menuAdapter.checkFriendForDouble(etLogin.getText().toString());
                if (friendUnique) {
                    presenter.setFriend(etLogin.getText().toString());
                } else {
                    showFriendExist(etLogin.getText().toString());
                }
            }
        });

        addAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        addAlertDialog.show();
    }

    private void showFriendExist(String s) {
        Toast.makeText(this, s + " is already your friend!", Toast.LENGTH_SHORT).show();
    }

    public void onUserItemClicked(Friend friend) {
        presenter.createRivalFromFriend(friend);
    }

    public void offerGame(Rival mRival) {
        Log.d("Notification",mRival.getUsername());
    }
}
