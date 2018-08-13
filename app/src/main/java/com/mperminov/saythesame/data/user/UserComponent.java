package com.mperminov.saythesame.data.user;

import com.mperminov.saythesame.base.annotation.UserScope;
import com.mperminov.saythesame.data.rival.RivalComponent;
import com.mperminov.saythesame.data.rival.RivalModule;
import com.mperminov.saythesame.ui.menu.MenuActivityComponent;
import com.mperminov.saythesame.ui.menu.MenuActivityModule;
import dagger.Subcomponent;

@UserScope
@Subcomponent(
    modules = {
        UserModule.class
    }
)
public interface UserComponent {
    MenuActivityComponent plus(MenuActivityModule activityModule);

    RivalComponent plus(RivalModule friendModule);
}