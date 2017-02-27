package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private TextView tvRegTitle, tvGoLogin;
    private EditText etUsername, etEmail, etPassword;
    private Button btRegister;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDBuser = mDB.getReference().child("user_info");

        tvRegTitle = (TextView) findViewById(R.id.textViewRegTitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/diamonde.ttf");
        tvRegTitle.setTypeface(custom_font);
        tvRegTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);

        etUsername = (EditText) findViewById(R.id.editTextRegUsername);
        etEmail = (EditText) findViewById(R.id.editTextRegEmail);
        etPassword = (EditText) findViewById(R.id.editTextRegPassword);
        btRegister = (Button) findViewById(R.id.buttonRegister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });

        tvGoLogin = (TextView) findViewById(R.id.textViewGoLogin);
        tvGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentLogin();
            }
        });
    }

    private void intentLogin() {
        Intent s = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(s);
        finish();
    }

    private void doRegister() {
        final String username = etUsername.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(username)) {
            etUsername.setError(getResources().getString(R.string.username_empty));
            return;
        }
        if (username.length() <= 5) {
            etUsername.setError(getResources().getString(R.string.username6));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getResources().getString(R.string.email_empty));
            return;
        }
        if (!email.matches(emailPattern)) {
            etEmail.setError(getResources().getString(R.string.email_notmatch));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getResources().getString(R.string.password_empty));
            return;
        }
        if (password.length() <= 7) {
            etPassword.setError(getResources().getString(R.string.password8));
            return;
        }

        btRegister.setText(getResources().getString(R.string.please_wait));
        btRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String UID = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser = mDBuser.child(UID);
                        currentUser.child("username").setValue(username);
                        currentUser.child("email").setValue(email);
                        currentUser.child("password").setValue(password);

                        Date date = new Date();
                        String time = DateFormat.getDateTimeInstance().format(date);

                        currentUser.child("date_created").setValue(time);
                        btRegister.setEnabled(true);

                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle(getResources().getString(R.string.success))
                                .setMessage(getResources().getString(R.string.success_message))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        etUsername.setText("");
                                        etEmail.setText("");
                                        etPassword.setText("");

                                        intentLogin();
                                    }
                                }).show();
                        mAuth.signOut();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btRegister.setEnabled(true);

                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle(getResources().getString(R.string.failed))
                        .setMessage(getResources().getString(R.string.failed_message))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }
}
