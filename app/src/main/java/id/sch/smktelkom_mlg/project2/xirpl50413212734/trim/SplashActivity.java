package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private String username = "";
    private FirebaseDatabase mDB;
    private FirebaseAuth mAuth;
    private PreferenceManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();

        if (!konek(this)) {
            new AlertDialog.Builder(SplashActivity.this)
                    .setTitle(getResources().getString(R.string.failed))
                    .setMessage("No internet connection")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }

        prefManager = new PreferenceManager(this);
        if (prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
            finish();
        } else {
            checkLogin();
        }
    }

    private void checkLogin() {
        if (mAuth.getCurrentUser() == null) {
            Intent i = new Intent(SplashActivity.this, LandingActivity.class);
            startActivity(i);
            finish();
        } else {
            handleLoading();
        }
    }

    public boolean konek(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void handleLoading() {
        DatabaseReference mDBuser = mDB.getReference().child("user_info").child(mAuth.getCurrentUser().getUid().toString());

        DatabaseReference dbUsername = mDBuser.child("username");
        dbUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);

                if (!TextUtils.isEmpty(username)) {
                    Intent u = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(u);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do nothing
            }
        });
    }
}