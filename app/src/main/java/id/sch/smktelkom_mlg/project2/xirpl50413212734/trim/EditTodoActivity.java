package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTodoActivity extends AppCompatActivity {
    private EditText etDoTitle, etDoDetail, etDoDate;

    private String mNoteKey;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        setTitle(getResources().getString(R.string.edit_todo));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDatabase = mDB.getReference().child("todo").child(mAuth.getCurrentUser().getUid().toString());

        etDoDate = (EditText) findViewById(R.id.editTextDoDateEdit);
        etDoTitle = (EditText) findViewById(R.id.editTextDoEdit);
        etDoDetail = (EditText) findViewById(R.id.editTextDoDescEdit);

        mNoteKey = getIntent().getExtras().getString("note_key");
        Log.d("FirebaseCounter", mNoteKey);

        fillData();
    }

    private void fillData() {
        mDatabase.child(mNoteKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String todoTitle = (String) dataSnapshot.child("title").getValue();
                String todoDetail = (String) dataSnapshot.child("detail").getValue();
                String todoDate = (String) dataSnapshot.child("date").getValue();

                etDoTitle.setText(todoTitle);
                etDoDetail.setText(todoDetail);
                etDoDate.setText(todoDate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_todo_edit) {
            updateTodo();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_delete_todo) {
            deleteTodo();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTodo() {
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

    private void updateTodo() {
        String title = etDoTitle.getText().toString();
        String detail = etDoDetail.getText().toString();
        String date = etDoDate.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(detail) || TextUtils.isEmpty(date)) {
            return;
        }

        mDatabase.child(mNoteKey).child("title").setValue(title);
        mDatabase.child(mNoteKey).child("detail").setValue(detail);
        mDatabase.child(mNoteKey).child("date").setValue(date);

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (etDoTitle.length() > 0 || etDoDetail.length() > 0 || etDoDate.length() > 0) {
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
