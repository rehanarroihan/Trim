package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.goncalves.pugnotification.notification.PugNotification;
import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.fragment.HomeFragment;
import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.fragment.NotesFragment;
import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.fragment.TodoFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView tvUsernameDrawer, tvEmailDrawer;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBuser;

    private String page = "home";
    private String allNote, allTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page == "todo") {
                    Intent t = new Intent(MainActivity.this, TodoActivity.class);
                    startActivity(t);
                } else if (page == "notes") {
                    Intent n = new Intent(MainActivity.this, NotesActivity.class);
                    startActivity(n);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Brain code start here
        View hView = navigationView.getHeaderView(0); //Mengambil view dari drawer
        tvUsernameDrawer = (TextView) hView.findViewById(R.id.textViewUsernameDrawer);
        tvEmailDrawer = (TextView) hView.findViewById(R.id.textViewEmailDrawer);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDBuser = mDB.getReference().child("user_info");

        retrieveData();
        changePage(R.id.nav_home);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void retrieveData() {
        //Mengambil data username yang login
        DatabaseReference userName = mDBuser.child(mAuth.getCurrentUser().getUid()).child("username");
        userName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                tvUsernameDrawer.setText(username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Mengambil data email yang login
        DatabaseReference eMail = mDBuser.child(mAuth.getCurrentUser().getUid()).child("email");
        eMail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue(String.class);
                tvEmailDrawer.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Mengambil informasi jumlah notes
        DatabaseReference dbNote = mDB.getReference().child("note").child(mAuth.getCurrentUser().getUid());
        dbNote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long jumlahNote = dataSnapshot.getChildrenCount();
                allNote = Long.toString(jumlahNote);
                Log.d("FirebaseCounter", allNote);

                if (allNote != null) {
                    push();
                }
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

                if (allTodo != null) {
                    push();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void push() {
        PugNotification.with(this)
                .load()
                .title("Trim")
                .message(getResources().getString(R.string.you_have) + " " + allNote + " notes and " + allTodo + " to-do list")
                .smallIcon(R.drawable.notif_logo)
                .largeIcon(R.drawable.trim_logo_round)
                .flags(Notification.DEFAULT_ALL)
                .simple()
                .build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        changePage(id);
        return true;
    }

    private void changePage(int id) {
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            setTitle(getResources().getString(R.string.home));
            fab.setVisibility(View.GONE);

        } else if (id == R.id.nav_todolist) {
            fragment = new TodoFragment();
            setTitle(getResources().getString(R.string.todolist));
            page = "todo";
            fab.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_notes) {
            fragment = new NotesFragment();
            setTitle(getResources().getString(R.string.notes));
            page = "notes";
            fab.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_about) {
            fragment = new HomeFragment();
            setTitle(getResources().getString(R.string.home));
            navigationView.setCheckedItem(R.id.nav_home);
            fab.setVisibility(View.GONE);

            Intent a = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(a);
        } else if (id == R.id.nav_logout) {
            fragment = new HomeFragment();
            setTitle(getResources().getString(R.string.home));
            navigationView.setCheckedItem(R.id.nav_home);
            fab.setVisibility(View.GONE);

            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.logout))
                    .setMessage(getResources().getString(R.string.logout_message))
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.yes_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();

                            Intent b = new Intent(MainActivity.this, SplashActivity.class);
                            startActivity(b);
                            finish();
                        }
                    }).setNegativeButton(getResources().getString(R.string.cancel_option), null).show();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitNow();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}