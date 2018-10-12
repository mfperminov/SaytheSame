package com.mperminov.saythesame.ui.menu;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.data.model.Friend;
import com.squareup.picasso.Picasso;
import java.util.List;

class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
  @BindView(R.id.ivAvatar)
  ImageView avatarImageView;
  @BindView(R.id.tvUser)
  TextView friendNameTextView;
  private View itemView;
  private List<Friend> friends;

  public MenuViewHolder(View itemView, List<Friend> friends) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    this.itemView = itemView;
    this.friends = friends;
    itemView.setOnLongClickListener(this);
  }

  public void bind(Friend friend) {
    if (!friend.getPhoto_url().equals("NOT")) {
      Picasso.get().load(friend.getPhoto_url()).into(avatarImageView);
    }
    friendNameTextView.setText(friend.getUsername());
  }

  @Override public boolean onLongClick(View view) {
    Friend friend = friends.get(getAdapterPosition());
    Log.d("Friend long click ", friend.getUsername());
    return true;
  }
}
