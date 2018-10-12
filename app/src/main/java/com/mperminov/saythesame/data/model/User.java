package com.mperminov.saythesame.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class User implements Parcelable {
  @NonNull private String uid;
  @Nullable private String username;
  @Nullable private String email;
  @VisibleForTesting
  @Nullable private String password;
  @Nullable private String provider;
  @Nullable String photo_url;
  @Nullable String name;

  public static User newInstance(FirebaseUser firebaseUser, UserInfo provider) {
    User user = new User(firebaseUser.getUid());
    user.setProvider(provider.getProviderId());
    // TODO : refactoring
    if (provider.getProviderId().equals("password")) {
      user.setEmail(firebaseUser.getEmail());
    } else if (provider.getProviderId().equals("facebook.com")) {
      user.setName(provider.getDisplayName());
      user.setPhoto_url(provider.getPhotoUrl().toString());
    } else if (provider.getProviderId().equals("google.com")) {
      user.setEmail(firebaseUser.getEmail());
      user.setName(provider.getDisplayName());
      user.setPhoto_url(provider.getPhotoUrl().toString());
    } else {

    }

    return user;
  }

  public User() {
  }

  public User(String uid) {
    this.uid = uid;
  }

  public User(String uid, String username, String email, String provider, String photo_url,
      String name) {
    this.uid = uid;
    this.username = username;
    this.email = email;
    this.provider = provider;
    this.photo_url = photo_url;
    this.name = name;
  }

  @NonNull
  public String getUid() {
    return uid;
  }

  public void setUid(@NonNull String uid) {
    this.uid = uid;
  }

  @Nullable
  public String getEmail() {
    return email;
  }

  private void setEmail(@Nullable String email) {
    this.email = email;
  }

  @Nullable
  public String getPhoto_url() {
    return photo_url;
  }

  public void setPhoto_url(@Nullable String photo_url) {
    this.photo_url = photo_url;
  }

  @Nullable
  public String getName() {
    return username;
  }

  private void setName(@Nullable String name) {
    this.name = name;
  }

  @Nullable
  public String getProvider() {
    return provider;
  }

  private void setProvider(@Nullable String provider) {
    this.provider = provider;
  }

  @Nullable
  public String getUsername() {
    return username;
  }

  public void setUsername(String nickname) {
    this.username = nickname;
  }

  protected User(Parcel in) {
    uid = in.readString();
    username = in.readString();
    email = in.readString();
    password = in.readString();
    provider = in.readString();
    photo_url = in.readString();
    name = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(uid);
    dest.writeString(username);
    dest.writeString(email);
    dest.writeString(password);
    dest.writeString(provider);
    dest.writeString(photo_url);
    dest.writeString(name);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    @Override
    public User createFromParcel(Parcel in) {
      return new User(in);
    }

    @Override
    public User[] newArray(int size) {
      return new User[size];
    }
  };
}

