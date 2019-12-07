package com.yyp.aps;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by generalYan on 2019/10/28.
 */
public class UserToken implements Parcelable {

    public static final Creator<UserToken> CREATOR = new Creator<UserToken>() {
        @Override
        public UserToken createFromParcel(Parcel in) {
            return new UserToken(in);
        }

        @Override
        public UserToken[] newArray(int size) {
            return new UserToken[size];
        }
    };

    private int userid;    // 用户id

    private String username;    // 用户名称

    private String userToken;    // 用户令牌

    private String nickname;    // 昵称

    private String userImage;    // 用户头像

    public UserToken(int userid, String username, String userToken, String nickname, String userImage) {
        super();
        this.userid = userid;
        this.username = username;
        this.userToken = userToken;
        this.nickname = nickname;
        this.userImage = userImage;
    }

    public UserToken(Parcel in) {
        this.userid = in.readInt();
        this.username = in.readString();
        this.userToken = in.readString();
        this.nickname = in.readString();
        this.userImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(userid);
        out.writeString(username);
        out.writeString(userToken);
        out.writeString(nickname);
        out.writeString(userImage);
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @Override
    public String toString() {
        try {
            JSONObject json = new JSONObject();
            json.put("userid", userid);
            json.put("username", username);
            json.put("userToken", userToken);
            json.put("nickname", nickname);
            json.put("userImage", userImage);
            return json.toString();
        } catch (Exception e) {
            return null;
        }
    }
}

