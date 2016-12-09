package com.google.firebase.zerotoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LoginDialog {
    private static final String TAG = "LoginDialog";

    static List<OnSuccessListener<EmailPasswordResult>> callbacks = new ArrayList<>();

    public static void onCredentials(final OnSuccessListener<EmailPasswordResult> callback) {
        callbacks.add(callback);
    }

    public static class EmailPasswordResult {
        public String email;
        public String password;

        public EmailPasswordResult() {
        }

        public EmailPasswordResult(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public static void showLoginPrompt(final Activity activity, final FirebaseApp app) {
        showLoginPrompt(activity, app, null);
    }
    public static void showLoginPrompt(final Activity activity, final FirebaseApp app, final OnSuccessListener<EmailPasswordResult> callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("What's your username");

        LinearLayout parent = new LinearLayout(activity);
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);

        final EditText email = new EditText(activity);
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email.setHint("User name");
        parent.addView(email);

        final EditText password = new EditText(activity);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setHint("Password");
        parent.addView(password);

        builder.setView(parent);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance(app).createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            public void onComplete(Task<AuthResult> task) {
                                EmailPasswordResult result = new EmailPasswordResult(email.getText().toString(), password.getText().toString());
                                if (callback != null) {
                                    callback.onSuccess(result);
                                }
                                else {
                                    for (OnSuccessListener<EmailPasswordResult> callback: callbacks){
                                        callback.onSuccess(result);
                                    }
                                }
                            }
                        });
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

 :  WhiteP
