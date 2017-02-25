package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView tvUsernameDrawer, tvEmailDrawer;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        changePage(R.id.nav_home);
        navigationView.setCheckedItem(R.id.nav_home);

        //Brain code start here
        View hView = navigationView.getHeaderView(0); //Mengambil view dari drawer
        tvUsernameDrawer = (TextView) hView.findViewById(R.id.textViewUsernameDrawer);
        tvEmailDrawer = (TextView) hView.findViewById(R.id.textViewEmailDrawer);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDBuser = mDB.getReference().child("User");

        //------------START MENGAMBIL DATA USERNAME DARI ACCOUNT----------//
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
        //------------END MENGAMBIL DATA USERNAME DARI ACCOUNT----------//

        //------------START MENGAMBIL DATA EMAIL DARI ACCOUNT----------//
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
        //------------END MENGAMBIL DATA EMAIL DARI ACCOUNT----------//
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            setTitle("Home");
        } else if (id == R.id.nav_todolist) {
            fragment = new TodoFragment();
            setTitle("ToDoList");
        } else if (id == R.id.nav_notes) {
            fragment = new NotesFragment();
            setTitle("Notes");
        } else if (id == R.id.nav_money) {
            fragment = new MoneyFragment();
            setTitle("Money Management");
        } else if (id == R.id.nav_help) {
            fragment = new HomeFragment();
            setTitle("Home");

            Intent h = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(h);
        } else if (id == R.id.nav_about) {
            fragment = new HomeFragment();
            setTitle("Home");

            Intent a = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(a);
        } else if (id == R.id.nav_logout) {
            fragment = new HomeFragment();
            setTitle("Home");

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
