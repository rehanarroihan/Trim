package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.fragment;


import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.goncalves.pugnotification.notification.PugNotification;
import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private TextView tvUser, tvEmail, tvLetter, tvTotalNote, tvTotalTodo;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBuser;

    private String allNote;
    private String allTodo;
    private String username;

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
        tvTotalNote = (TextView) getActivity().findViewById(R.id.textViewTotalNote);
        tvTotalTodo = (TextView) getActivity().findViewById(R.id.textViewTotalTodo);

        final LinearLayout llLoading = (LinearLayout) getView().findViewById(R.id.llLoading);
        final LinearLayout llContent = (LinearLayout) getView().findViewById(R.id.llContent);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDBuser = mDB.getReference().child("user_info").child(mAuth.getCurrentUser().getUid());

        //Mengambil informasi jumlah notes
        DatabaseReference dbNote = mDB.getReference().child("note").child(mAuth.getCurrentUser().getUid());
        dbNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long jumlahNote = dataSnapshot.getChildrenCount();
                allNote = Long.toString(jumlahNote);
                Log.d("FirebaseCounter", allNote);

                tvTotalNote.setText(allNote + " " + "notes");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Mengambil informasi jumlah to-do list
        DatabaseReference dbTodo = mDB.getReference().child("todo").child(mAuth.getCurrentUser().getUid());
        dbTodo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long jumlahTodo = dataSnapshot.getChildrenCount();
                allTodo = Long.toString(jumlahTodo);
                Log.d("FirebaseCounter", "Todo List " + allTodo);

                tvTotalTodo.setText(allTodo + " " + "to-do list");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Mengambil data username
        DatabaseReference dbUsername = mDBuser.child("username");
        dbUsername.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
                tvUser.setText(username);
                tvLetter.setText(username);

                if (!TextUtils.isEmpty(username) && allNote != null) {
                    //Menghilangkan tamppilan loading
                    llLoading.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);

                    pushNotification();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getView(), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        //Mengambil informasi email user
        DatabaseReference dbEmail = mDBuser.child("email");
        dbEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue(String.class);
                tvEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getView(), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void pushNotification() {
        PugNotification.with(getActivity())
                .load()
                .title("Trim")
                .message(getResources().getString(R.string.you_have) + " " + allNote + " notes and " + allTodo + " to-do list")
                .smallIcon(R.drawable.trim_logo_round)
                .largeIcon(R.drawable.trim_logo_round)
                .flags(Notification.DEFAULT_ALL)
                .simple()
                .build();
    }
}
