package com.mperminov.saythesame.ui.game;

import com.mperminov.saythesame.base.BaseActivity;
import com.mperminov.saythesame.base.BaseApplication;

public class GameActivity extends BaseActivity {

    @Override
    protected void setupActivityComponent() {
        BaseApplication.get(this).getRivalComponent()
                    .plus(new GameActivityModule(this))
                    .inject(this);

    }
}
