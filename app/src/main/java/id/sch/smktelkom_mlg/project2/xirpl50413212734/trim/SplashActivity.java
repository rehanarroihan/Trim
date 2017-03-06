package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();

        if (mAuth.getCurrentUser() == null) {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            handleLoading();
        }
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
            }
        });
    }
}
