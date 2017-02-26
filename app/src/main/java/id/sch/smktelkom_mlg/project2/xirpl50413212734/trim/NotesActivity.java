package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class NotesActivity extends AppCompatActivity {
    FirebaseDatabase mDB;
    DatabaseReference mDBnote;
    FirebaseAuth mAuth;
    Long jumlahData;
    Integer currentPostId;
    private EditText etTitleNote, etContentNote;
    private TextView tvChar;
    //Character counter
    private final TextWatcher etContentNoteWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvChar.setText(String.valueOf(s.length()) + " " + getResources().getString(R.string.charac));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setTitle(getResources().getString(R.string.add_note));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitleNote = (EditText) findViewById(R.id.editTextTitleNote);
        etContentNote = (EditText) findViewById(R.id.editTextContentNotes);
        etContentNote.addTextChangedListener(etContentNoteWatcher);
        tvChar = (TextView) findViewById(R.id.textViewChar);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();

        mDBnote = mDB.getReference().child("Note");
        mDBnote.addValueEventListener(new ValueEventListener() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_note) {
            uploadNote();
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadNote() {
        String title = etTitleNote.getText().toString();
        String content = etContentNote.getText().toString();
        String fUser = mAuth.getCurrentUser().getUid().toString();

        Date date = new Date();
        String time = DateFormat.getDateTimeInstance().format(date);

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return;
        }

        // Database Reference for ToDoList
        DatabaseReference dbnote = mDBnote.child(currentPostId.toString());
        // Database Reference for Users
        DatabaseReference users = mDB.getReference("User").child(fUser).child("note");

        Log.d("FirebaseCounter", mAuth.getCurrentUser().getUid());
        dbnote.child("title").setValue(title);
        dbnote.child("content").setValue(content);
        dbnote.child("time").setValue(time);
        dbnote.child("owner").setValue(fUser);

        users.child(currentPostId.toString()).setValue(title);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (etTitleNote.length() > 0 || etContentNote.length() > 0) {
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
