package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.app.Activity;
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
import com.jaredrummler.android.device.DeviceName;

import java.text.DateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    public static Activity LogA;
    private TextView tvLoginTitle, tvGoRegister;
    private EditText etEmail, etPassword;
    private Button btLogin;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDBuser = mDB.getReference().child("user_info");

        etEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        etPassword = (EditText) findViewById(R.id.editTextLoginPassword);
        btLogin = (Button) findViewById(R.id.buttonLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        tvLoginTitle = (TextView) findViewById(R.id.textViewLoginTitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/yananeka.ttf");
        tvLoginTitle.setTypeface(custom_font);
        tvLoginTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);

        tvGoRegister = (TextView) findViewById(R.id.textViewGoRegister);
        tvGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(s);
                finish();
            }
        });

    }

    private void doLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getResources().getString(R.string.email_empty));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getResources().getString(R.string.password_empty));
            return;
        }

        btLogin.setText(getResources().getString(R.string.please_wait));
        btLogin.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                DatabaseReference lastLogin = mDBuser.child(mAuth.getCurrentUser().getUid()).child("last_login");
                String deviceName = DeviceName.getDeviceName(); //Mengambil nama device

                Date date = new Date();
                String time = DateFormat.getDateTimeInstance().format(date);

                lastLogin.child("time").setValue(time);
                lastLogin.child("device").setValue(deviceName);

                btLogin.setText(getResources().getString(R.string.success));

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btLogin.setText(getResources().getString(R.string.login_title));
                btLogin.setEnabled(true);

                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(getResources().getString(R.string.failed))
                        .setMessage(getResources().getString(R.string.failed1_message) + ". " + e.getMessage())
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
