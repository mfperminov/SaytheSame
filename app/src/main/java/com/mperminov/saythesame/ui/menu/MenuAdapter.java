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
import java.util.HashSet;
import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final List<Friend> friends = new ArrayList<>();
  private MenuActivity activity;
  // set contains positions of items in adapter that currently in "expanded" state
  private HashSet<Integer> expandedPositions = new HashSet<>();

  public MenuAdapter(MenuActivity activity) {
    this.activity = activity;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_friend, parent, false);
    MenuViewHolder vh = new MenuViewHolder(itemView);
    vh.btnAddToFav.setOnClickListener(view -> {
      int pos = vh.getAdapterPosition();
      if (pos != NO_POSITION) {
        addToFavourite(friends.get(pos));
      }
    });
    vh.btnPlay.setOnClickListener(view -> {
      int pos = vh.getAdapterPosition();
      if (pos != NO_POSITION) {
        inviteToPlay(friends.get(pos));
      }
    });
    setOnClickListener(vh);
    setOnLongClickListener(vh);
    return vh;
  }

  private void inviteToPlay(Friend friend) {
    Log.d("Invite to play", friend.getUsername());
  }

  private void addToFavourite(Friend friend) {
    Log.d("Adding to Favourite", friend.getUsername());
  }

  private void setOnClickListener(MenuViewHolder vh) {
    vh.itemView.setOnClickListener(view -> {
      int pos = vh.getAdapterPosition();
      if (pos != NO_POSITION && expandedPositions.contains(pos)) {
        // collapse
        vh.expandedView.setVisibility(View.GONE);
        expandedPositions.remove(pos);
      }
    });
  }

  private void setOnLongClickListener(MenuViewHolder vh) {
    vh.itemView.setOnLongClickListener(view -> {
      int pos = vh.getAdapterPosition();
      if (pos != NO_POSITION) {
        // expand
        vh.expandedView.setVisibility(View.VISIBLE);
        expandedPositions.add(pos);
      }
      return true;
    });
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
