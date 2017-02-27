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

public class EditNoteActivity extends AppCompatActivity {
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
    private String mNoteKey;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        setTitle(getResources().getString(R.string.edit_note));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDatabase = mDB.getReference().child("note").child(mAuth.getCurrentUser().getUid().toString());

        etTitleNote = (EditText) findViewById(R.id.editTextTitleNote2);
        etContentNote = (EditText) findViewById(R.id.editTextContentNotes2);
        etContentNote.addTextChangedListener(etContentNoteWatcher);
        tvChar = (TextView) findViewById(R.id.textViewChar2);

        mNoteKey = getIntent().getExtras().getString("note_key");
        Log.d("FirebaseCounter", mNoteKey);

        //Toast.makeText(getApplicationContext(), mNoteKey, Toast.LENGTH_SHORT).show();

        fillData();
    }

    private void fillData() {
        mDatabase.child(mNoteKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String noteTitle = (String) dataSnapshot.child("title").getValue();
                String noteContent = (String) dataSnapshot.child("content").getValue();

                etTitleNote.setText(noteTitle);
                etContentNote.setText(noteContent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_note_edit) {
            updateNote();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_delete_note) {
            deleteNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.del))
                .setMessage(getResources().getString(R.string.del_message))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.yes_option), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child(mNoteKey).removeValue();

                        finish();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_saved), Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel_option), null).show();
    }

    private void updateNote() {
        String title = etTitleNote.getText().toString();
        String content = etContentNote.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            return;
        }

        mDatabase.child(mNoteKey).child("title").setValue(title);
        mDatabase.child(mNoteKey).child("content").setValue(content);

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
