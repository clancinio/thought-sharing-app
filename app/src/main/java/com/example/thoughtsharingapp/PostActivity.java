package com.example.thoughtsharingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.thoughtsharingapp.classes.Feed;
import com.example.thoughtsharingapp.classes.NotificationStarter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = PostActivity.class.getSimpleName();
    private TextInputLayout titleEditext;
    private TextInputLayout textEditext;
    
    private DatabaseReference databaseReference;
    private DatabaseReference myPostsDatabaseRef;
    private FirebaseAuth auth;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);
        titleEditext = findViewById(R.id.title_edit_text);
        textEditext = findViewById(R.id.text_edit_text);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts");
        // reference for just the post posted by current user
        myPostsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("MyPosts");

        auth = FirebaseAuth.getInstance();
        NotificationStarter notification = new NotificationStarter(this);
        notification.checkForNewRequest();
    }

    public void postTitle(View view) {

        if (!titleEditext.getEditText().getText().toString().isEmpty() && !textEditext.getEditText().getText().toString().isEmpty()) {
            startPosting(titleEditext.getEditText().getText().toString(), textEditext.getEditText().getText().toString());
            // Progress bar
            AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
            builder.setCancelable(false); // if you want user to wait for some process to finish,
            builder.setView(R.layout.layout_post_loading);
            dialog = builder.create();
            dialog.show(); // to show this dialog
        }else{
            dialog.dismiss();
            Toast.makeText(this, "Thinking of posting something right?", Toast.LENGTH_SHORT).show();
        }


    }

    // This method posts to the database
    private void startPosting(String title, String text) {
        final DatabaseReference myNewPost = myPostsDatabaseRef.child(auth.getUid()).push();
        final DatabaseReference newPost = databaseReference.push();
        newPost.setValue(new Feed(title, text, auth.getUid(), newPost.getRef().getKey())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {


                    Log.e(TAG, "Posted successfully");
                    Toast.makeText(PostActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //TODO: come up with a better design to notify them that they couldnt post
                    Log.e(TAG, "Posting failed");
                    Toast.makeText(PostActivity.this, "Posting failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myNewPost.setValue(new Feed(title, text, auth.getUid(), newPost.getRef().getKey())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "Posted successfully");
                    Toast.makeText(PostActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //TODO: come up with a better design to notify them that they couldnt post
                    Log.e(TAG, "Posting failed");
                    Toast.makeText(PostActivity.this, "Posting failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}