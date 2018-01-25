package could.bluepay.renyumvvm.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import could.bluepay.renyumvvm.Config;
import could.bluepay.renyumvvm.bindingAdapter.command.ReplyCommand;
import could.bluepay.renyumvvm.common.PrefsHelper;
import could.bluepay.renyumvvm.databinding.ActivityUserSettingBinding;
import could.bluepay.renyumvvm.view.activity.LoginActivity;

/**
 * Created by bluepay on 2018/1/24.
 */

public class UserSettingViewModel implements ViewModel {

    private Context context;
    private ActivityUserSettingBinding binding;

    public void init(ActivityUserSettingBinding binding,Context context){
        this.binding = binding;
        this.context = context;
    }
    public void logout(View view){
        PrefsHelper.with(context,Config.PREFS_USER).writeInt(Config.SP_KEY_LAST_LOGIN_TYPE,-1);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//清空activity栈
        context.startActivity(intent);
    }
    public final ReplyCommand logoutCommand = new ReplyCommand(new Runnable() {
        @Override
        public void run() {
            PrefsHelper.with(context,Config.PREFS_USER).writeInt(Config.SP_KEY_LAST_LOGIN_TYPE,-1);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//清空activity栈
            context.startActivity(intent);
        }
    });
}
