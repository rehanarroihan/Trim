package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class TodoActivity extends AppCompatActivity {
    Calendar dateTime = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            DateFormat df_date = DateFormat.getDateInstance();
            etDoDate.setText(df_date.format(dateTime.getTime()));
        }
    };
    private EditText etDo, etDoDesc, etDoDate;
    private ImageButton ibDate;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBtodo, mDBtodoUser;
    private String dbCurrentUser;
    private Long jumlahData;
    private Integer currentPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        setTitle(getResources().getString(R.string.add_todo));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();

        dbCurrentUser = mAuth.getCurrentUser().getUid().toString();

        etDo = (EditText) findViewById(R.id.editTextDo);
        etDoDesc = (EditText) findViewById(R.id.editTextDoDesc);
        etDoDate = (EditText) findViewById(R.id.editTextDoDate);
        etDoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        ibDate = (ImageButton) findViewById(R.id.imageButtonDate);
        ibDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        mDBtodo = mDB.getReference().child("todo");
        mDBtodoUser = mDBtodo.child(dbCurrentUser);
        mDBtodoUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jumlahData = dataSnapshot.getChildrenCount() - 1;
                currentPostId = jumlahData.intValue() + 1;
                String strValue = jumlahData.toString();
                Boolean isDataNotExist = false;
                while (isDataNotExist == false) {
                    Boolean cleanCheck = dataSnapshot.child(currentPostId.toString()).exists();
                    if (cleanCheck) {
                        Log.d("FirebaseCounter", "Child with ID " + currentPostId.toString() + " already Exists! +1");
                        currentPostId += 1;
                    } else {
                        Log.d("FirebaseCounter", "Child with ID " + currentPostId.toString() + " Available to Use!");
                        isDataNotExist = true;
                    }
                }
                Log.d("FirebaseCounter", strValue);
                Log.d("FirebaseCounter", "Next Post Should be : " + currentPostId.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseError", databaseError.getMessage());
            }
        });
    }

    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_todo) {
            uploadTodo();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadTodo() {
        String title = etDo.getText().toString();
        String detail = etDoDesc.getText().toString();
        String date = etDoDate.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(detail) || TextUtils.isEmpty(date)) {
            return;
        }

        // Database Reference for note
        DatabaseReference dbnote = mDBtodoUser.child(currentPostId.toString());

        Log.d("FirebaseCounter", mAuth.getCurrentUser().getUid());
        dbnote.child("title").setValue(title);
        dbnote.child("detail").setValue(detail);
        dbnote.child("date").setValue(date);

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.todo_saved), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (etDo.length() > 0 || etDoDesc.length() > 0 || etDoDate.length() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.alert))
                    .setMessage(getResources().getString(R.string.back_alert))
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.yes_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_saved), Toast.LENGTH_SHORT).show();
                            onSuperBackPressed();
                        }
                    }).setNegativeButton(getResources().getString(R.string.cancel_option), null).show();
        } else {
            onSuperBackPressed();
        }
    }

    public void onSuperBackPressed() {
        super.onBackPressed();
    }
}
