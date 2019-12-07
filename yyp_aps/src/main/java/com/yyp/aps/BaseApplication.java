package com.yyp.aps;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by generalYan on 2019/10/28.
 */
public class BaseApplication extends Application {

    public static final int MESSAGE_TOKEN_CHANGE = 0x01;
    public static final int MESSAGE_USERNAME_CHANGE = 0x02;
    public static final int MESSAGE_NICKNAME_CHANGE = 0x03;
    public static final int MESSAGE_USERIMAGE_CHANGE = 0x04;

    static final String APP_SHARENAME = BaseApplication.class.getName() + ".APP_SHARENAME";
    static final String KEY_USR_TOKEN = BaseApplication.class.getName() + ".KEY_USR_TOKEN";

    UserToken userToken;

    FrameLayout container;

    Vector<SingleActivity> pagestack;

    AgentActivity currAgent;

    @Override
    public void onCreate() {
        super.onCreate();

        String saveLocal = getSharedString(KEY_USR_TOKEN, null);
        if (saveLocal != null) {
            try {
                JSONObject json = new JSONObject(saveLocal);
                int userid = json.getInt("userid");
                String username = json.getString("username");
                String usertoken = json.getString("userToken");
                String nickname = json.getString("nickname");
                String userimage = json.getString("userImage");

                userToken = new UserToken(userid, username, usertoken, nickname, userimage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean canResume() {
        return container != null && pagestack != null && pagestack.size() > 0;
    }

    void onRestore(AgentActivity pageAgent) {
        this.container = pageAgent.container;
        this.pagestack = pageAgent.pagestack;
    }

    void onResumed(AgentActivity pageAgent) {
        int count = pagestack.size();
        SingleActivity page = pagestack.get(count - 1);

        if (page instanceof GroupActivity) {
            GroupActivity group = (GroupActivity) page;
            pageAgent.pageIndex = group.findMaxIndex();
        } else {
            pageAgent.pageIndex = page.pageIndex;
        }

        for (SingleActivity instance : pagestack) {
            instance.doResume(pageAgent);
        }

        pageAgent.container = container;
        pageAgent.pagestack = pagestack;

        ViewGroup group = (ViewGroup) container.getParent();
        group.removeView(container);
        pageAgent.setContentView(container);

        container = null;
        pagestack = null;
    }

    /* 保存登录令牌到注册表 */
    void saveToken() {
        String saveLocal = userToken == null ? null : userToken.toString();
        setSharedString(KEY_USR_TOKEN, saveLocal);
    }

    /* 设置用户token */
    public void setUserToken(UserToken userToken) {
        if (userToken == null && this.userToken == null) {
            return;
        }

        this.userToken = userToken;

        saveToken();

        if (currAgent != null) {
            /* 通知各个页面token发生变化 */
            currAgent.sendMessage(MESSAGE_TOKEN_CHANGE);
        }
    }

    /* 获取用户token */
    public UserToken getUserToken() {
        return userToken;
    }

    /* 帐号 */
    public void setUserName(String username) {
        if (username == null)
            return;
        if (userToken != null) {
            userToken.setUsername(username);
            saveToken();

            if (currAgent != null) /* 通知各个页面用户昵称发生变化 */
                currAgent.sendMessage(MESSAGE_USERNAME_CHANGE);
        }
    }

    /* 更新用户昵称 */
    public void updateNickName(String nickname) {
        if (userToken != null) {
            userToken.setNickname(nickname);
            saveToken();

            if (currAgent != null) /* 通知各个页面用户昵称发生变化 */
                currAgent.sendMessage(MESSAGE_NICKNAME_CHANGE);
        }
    }

    /* 更新用户头像 */
    public void updateUserImage(String imageUrl) {
        if (userToken != null) {
            userToken.setUserImage(imageUrl);
            saveToken();

            if (currAgent != null) /* 通知各个页面，用户头像发生变化 */
                currAgent.sendMessage(MESSAGE_USERIMAGE_CHANGE);
        }
    }

    public String getSharedString(String key, String defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getString(key, defValue);
    }

    public void setSharedString(String key, String value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public int getSharedInt(String key, int defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getInt(key, defValue);
    }

    public void setSharedInt(String key, int value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public long getSharedLong(String key, long defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getLong(key, defValue);
    }

    public void setSharedLong(String key, long value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public float getSharedFloat(String key, float defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getFloat(key, defValue);
    }

    public void setSharedFloat(String key, float value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public boolean getSharedBoolean(String key, boolean defValue) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        return sf.getBoolean(key, defValue);
    }

    public void setSharedBoolean(String key, boolean value) {
        SharedPreferences sf = getSharedPreferences(APP_SHARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

}

