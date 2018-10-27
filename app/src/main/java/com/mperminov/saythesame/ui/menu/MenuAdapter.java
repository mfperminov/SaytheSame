package com.mperminov.saythesame.ui.menu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.data.model.Friend;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<Friend> friends = new ArrayList<>();
  private MenuActivity activity;

  public MenuAdapter(MenuActivity activity) {
    this.activity = activity;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_friend, parent, false);
    MenuViewHolder vh = new MenuViewHolder(itemView);

    vh.itemView.setOnLongClickListener(view -> {
      int pos = vh.getAdapterPosition();
      if (pos != NO_POSITION) {
        Log.d("friend clicked", friends.get(pos).getUsername());
      }
      return false;
    });
    return vh;
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
