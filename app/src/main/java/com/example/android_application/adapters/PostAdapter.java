package com.example.android_application.adapters;

import android.media.session.MediaController;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_application.databinding.PostItemBinding;
import com.example.android_application.listeners.PostListener;
import com.example.android_application.models.Post;
import com.example.android_application.ultilities.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<Post> postList;
    private final PostListener postListener;


    public PostAdapter(List<Post> postList, PostListener postListener) {
        this.postList = postList;
        this.postListener = postListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemBinding postItemBinding = PostItemBinding.inflate(
                LayoutInflater.from(parent.getContext())
        );
        return new PostViewHolder(postItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
        holder.SetPostData(postList.get(position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        PostItemBinding binding;
        private FirebaseAuth mAuth;
        private FirebaseUser firebaseUser;


        PostViewHolder(PostItemBinding postItemBinding){
            super(postItemBinding.getRoot());
            binding = postItemBinding;
        }

        void SetPostData(Post post){
            setImage(post.postImg);
            setVideo(post.postVideo);

//            Picasso.get().load(post.postImg).into(binding.postImage);
            binding.profileImage.setImageBitmap(post.imageProfile);
            binding.postTitle.setText(post.title);
            binding.postDescription.setText(post.description);
            binding.profileName.setText(post.name);
            binding.profileDate.setText(post.date);
            binding.feedsCommentCount.setText( String.valueOf(post.userCommentList) + " comments");
            System.out.println(post.title);
            System.out.println(post.userCommentList);
            binding.feedsLikesCount.setText(post.likeCount);
            binding.profileImage.setOnClickListener(v-> postListener.onImageProfileClicked(post));
            FirebaseFirestore database = FirebaseFirestore.getInstance();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();
            database.collection(Constants.COLLECTION_POST).document(post.postId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            List<String> userList = (List<String>) documentSnapshot.get(Constants.POST_USER_LIKE);
                            Integer likeLength = userList.size();
                            binding.feedsLikesCount.setText(likeLength.toString() + " likes");
                            String userId = mAuth.getUid();
                            if (userList.contains(userId)){
                                System.out.println(true);

                                binding.feedsPostLike.setVisibility(View.GONE);
                                binding.feedsPostLiked.setVisibility(View.VISIBLE);
                            }else {
                                System.out.println(false);
                                binding.feedsPostLike.setVisibility(View.VISIBLE);
                                binding.feedsPostLiked.setVisibility(View.GONE);
                            }

                        }
                    });


            binding.feedsPostLiked.setOnClickListener(v -> {
                System.out.println("unlike");
                unliked(post);
                binding.feedsPostLike.setVisibility(View.VISIBLE);
                binding.feedsPostLiked.setVisibility(View.GONE);
            });
            binding.feedsPostLike.setOnClickListener(v -> {
                liked(post);
                binding.feedsPostLike.setVisibility(View.GONE);
                binding.feedsPostLiked.setVisibility(View.VISIBLE);
            });

            binding.feedsLikesCount.setOnClickListener(v-> {
                postListener.onLikedCountClicked(post);
            });

            binding.feedsCommentCount.setOnClickListener(v -> {
                postListener.   onCommnentClicked(post);
            });


        }

        void liked(Post post){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            ArrayList list = new ArrayList();
            list.add(mAuth.getUid());
            database.collection(Constants.COLLECTION_POST)
                    .document(post.postId)
                    .update("userLiked", FieldValue.arrayUnion(list.toArray()));
        }

        void unliked(Post post){

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            ArrayList list = new ArrayList();
            list.add(mAuth.getUid());
            database.collection(Constants.COLLECTION_POST)
                    .document(post.postId)
                    .update("userLiked",FieldValue.arrayRemove(list.toArray()));

        }


    public void setImage(final String imageUrl){

        try {
            if (imageUrl!=null) {
                 Picasso.get().load(imageUrl).into(binding.postImage);
            } else {
                binding.postImage.setVisibility(View.GONE);
            }
        }
        catch (Exception e){

        }

    }

     public void setVideo(final String videoUrl){
        try {
            if (videoUrl!=null) {
                try {
                    Uri videoUri = Uri.parse(videoUrl);
                    binding.postVideo.setVideoURI(videoUri);
                    binding.postVideo.setTag(videoUrl);
                    String hasVideo_string = (String) binding.postVideo.getTag();
                    boolean hasVideo = Boolean.parseBoolean(hasVideo_string);
//                    binding.postVideo.setMediaController(new MediaController(this));
                    binding.postVideo.requestFocus();
                    binding.postVideo.start();

                } catch (Exception e) {
                    System.out.println("Error :" + e);
                }

            } else {
                binding.postVideo.setVisibility(View.GONE);
            }
        }
        catch (Exception e){

        }

    }

    }


}
