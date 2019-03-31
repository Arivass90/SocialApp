package com.example.gerard.socialapp.PostsAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.gerard.socialapp.GlideApp;
import com.example.gerard.socialapp.R;
import com.example.gerard.socialapp.model.Post;
import com.example.gerard.socialapp.view.activity.MediaActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class PostsAdapter extends FirebaseRecyclerAdapter<Post, PostsAdapter.PostViewHolder> {

    Context context;
    FirebaseUser mUser;
    DatabaseReference mReference;

    public PostsAdapter(@NonNull FirebaseRecyclerOptions<Post> options, Context context, FirebaseUser firebaseUser, DatabaseReference databaseReference) {
        super(options);
        this.context = context;
        this.mUser = firebaseUser;
        this.mReference = databaseReference;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new PostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder(final PostViewHolder viewHolder, int position, final Post post) {
        final String postKey = getRef(position).getKey();

        viewHolder.author.setText(post.author);
        GlideApp.with(context).load(post.authorPhotoUrl).circleCrop().into(viewHolder.photo);

        if (post.likes.containsKey(mUser.getUid())) {
            viewHolder.like.setImageResource(R.drawable.heart_on);
            viewHolder.numLikes.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.like.setImageResource(R.drawable.heart_off);
            viewHolder.numLikes.setTextColor(context.getResources().getColor(R.color.grey));
        }

        viewHolder.content.setText(post.content);

        if (post.mediaUrl != null) {
            viewHolder.image.setVisibility(View.VISIBLE);
            if ("audio".equals(post.mediaType)) {
                viewHolder.image.setImageResource(R.drawable.audio);
            } else {
                GlideApp.with(context).load(post.mediaUrl).centerCrop().into(viewHolder.image);

            }
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MediaActivity.class);
                    intent.putExtra("mediaUrl", post.mediaUrl);
                    intent.putExtra("mediaType", post.mediaType);
                    context.startActivity(intent);
                }
            });

            //mejora de la app
            //viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
             //   @Override
             //   public void onClick(View view) {
             //       Intent intent = new Intent(context, PostComentActivity.class);
             //       intent.putExtra("postKey", postKey);
             //       context.startActivity(intent);
            //    }
           // });
        } else {
            viewHolder.image.setVisibility(View.GONE);
        }

        viewHolder.numLikes.setText(String.valueOf(post.likes.size()));

        viewHolder.likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post.likes.containsKey(mUser.getUid())) {
                    mReference.child("posts/data").child(postKey).child("likes").child(mUser.getUid()).setValue(null);
                    mReference.child("posts/user-likes").child(mUser.getUid()).child(postKey).setValue(null);
                } else {
                    mReference.child("posts/data").child(postKey).child("likes").child(mUser.getUid()).setValue(true);
                    mReference.child("posts/user-likes").child(mUser.getUid()).child(postKey).setValue(true);
                }
            }
        });
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView photo;
        public TextView author;
        public TextView content;
        public ImageView image;
        public ImageView like;
        public TextView numLikes;
        public LinearLayout likeLayout;

        public PostViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);
            like = itemView.findViewById(R.id.like);
            numLikes = itemView.findViewById(R.id.num_likes);
            likeLayout = itemView.findViewById(R.id.like_layout);
        }
    }
}
