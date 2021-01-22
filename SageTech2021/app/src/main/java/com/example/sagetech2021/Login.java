package com.example.sagetech2021;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    //Initialize variable
    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assign variable
        btSignIn = findViewById(R.id.bt_sign_in);

        //Initialize sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder
                (
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                ).requestIdToken("973547652702-a12vd3p3b5hsv23rsq9o2mc2la3ln951.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(Login.this
                , googleSignInOptions);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize sign in intent
                Intent intent = googleSignInClient.getSignInIntent();

                //Start activity for result
                startActivityForResult(intent, 100);
            }
        });

        //Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        //initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //check condition
        if(firebaseUser != null){
            //redirect to profile activity (main)
            startActivity(new Intent(Login.this
                           ,MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check condition
        if(requestCode==100){
            //Initialize task
            final Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            //Check condition
            if(signInAccountTask.isSuccessful()){
                //google sign in is successful
                //Initialize string
                String s = "Google sign in successful";
                //Display toast
                displayToast(s);

                try {
                    //Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    //check condition
                    if(googleSignInAccount != null){
                        //initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken(), null);

                        //check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        //check condition
                                        if(task.isSuccessful()){
                                            //redirect to profile activity
                                            startActivity(new Intent(Login.this,
                                                    MainActivity.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                            //display toast
                                            displayToast("Firebase authentication successful");
                                        }
                                        else{
                                            //Display toast
                                            displayToast("Authentication failed : " + task.getException()
                                            .getMessage());
                                        }
                                    }
                                });

                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
