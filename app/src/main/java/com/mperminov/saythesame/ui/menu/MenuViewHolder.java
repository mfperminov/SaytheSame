package com.mperminov.saythesame.ui.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mperminov.saythesame.R;
import com.mperminov.saythesame.data.model.Friend;
import com.squareup.picasso.Picasso;

class MenuViewHolder extends RecyclerView.ViewHolder{
  @BindView(R.id.ivAvatar)
  ImageView avatarImageView;
  @BindView(R.id.tvUser)
  TextView friendNameTextView;
  @BindView(R.id.expanded_menu) View expandedView;
  @BindView(R.id.favourite) ImageButton btnAddToFav;
  @BindView(R.id.play) ImageButton btnPlay;
  View itemView;

  public MenuViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
    this.itemView = itemView;
  }

  public void bind(Friend friend) {
    if (!friend.getPhoto_url().equals("NOT")) {
      Picasso.get().load(friend.getPhoto_url()).into(avatarImageView);
    }
    friendNameTextView.setText(friend.getUsername());
  }

}
