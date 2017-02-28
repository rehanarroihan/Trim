package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView tvUser, tvEmail, tvLetter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBuser;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvUser = (TextView) getView().findViewById(R.id.textViewUsernameHome);
        tvEmail = (TextView) getView().findViewById(R.id.textViewEmailHome);
        tvLetter = (TextView) getView().findViewById(R.id.textViewNameLetter);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDBuser = mDB.getReference().child("user_info").child(mAuth.getCurrentUser().getUid().toString());

        DatabaseReference dbUsername = mDBuser.child("username");
        dbUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                tvUser.setText(username);
                tvLetter.setText(username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference dbEmail = mDBuser.child("email");
        dbEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue(String.class);
                tvEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
