package com.example.sagetech2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //initialize variable
    ImageView ivImage;
    TextView tvName;
    Button btLogout;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign variable
        ivImage = findViewById(R.id.iv_image);
        tvName = findViewById(R.id.tv_name);
        btLogout = findViewById(R.id.bt_sign_in);

        //initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //Check condition
        if(firebaseUser != null){
            //set image on image view
            Glide.with(MainActivity.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(ivImage);

            //set name on text view
            tvName.setText(firebaseUser.getDisplayName());
        }

        //initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this,
                GoogleSignInOptions.DEFAULT_SIGN_IN);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sign out from google
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //check condition
                        if(task.isSuccessful()){
                            //sign out from firebase
                            firebaseAuth.signOut();

                            //display toast
                            Toast.makeText(getApplicationContext()
                                    , "logout successful", Toast.LENGTH_SHORT).show();

                            //finish activity
                            finish();
                        }
                    }
                });
            }
        });
    }
}
