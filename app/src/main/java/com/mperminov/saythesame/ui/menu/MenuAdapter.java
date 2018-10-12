package com.mperminov.saythesame.ui.menu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.data.model.Friend;
import java.util.ArrayList;
import java.util.List;

class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private MenuActivity activity;

  public MenuAdapter(MenuActivity activity) {
    this.activity = activity;
  }

  private final List<Friend> friends = new ArrayList<>();

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_friend, parent, false);
    return new MenuViewHolder(itemView, friends);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
    ((MenuViewHolder) holder).bind(friends.get(position));
  }

  @Override public int getItemCount() {
    return friends.size();
  }

  public void onFriendAdded(Friend friend) {
    friends.add(friend);
    notifyItemChanged(friends.size() - 1);
  }

  public Boolean checkFriendForDouble(String s) {
    for (Friend f : friends) {
      if (f.getUsername().equals(s)) {
        return false;
      }
    }
    return true;
  }

  void clearList() {
    friends.clear();
  }
}
