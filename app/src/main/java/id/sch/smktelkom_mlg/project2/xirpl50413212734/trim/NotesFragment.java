package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.model.Note;

/**
 * A simple {@link Fragment} subclass.
 */

public class NotesFragment extends Fragment {
    private static FrameLayout sckbrFrm;
    private RecyclerView mNoteList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBnote, mDBnoteUser;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sckbrFrm = (FrameLayout) getView().findViewById(R.id.snckbrFrameLayout);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDBnote = mDB.getReference().child("note");
        mDBnoteUser = mDBnote.child(mAuth.getCurrentUser().getUid().toString());

        mNoteList = (RecyclerView) getView().findViewById(R.id.recyclerViewNotes);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mNoteList.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Note, NoteViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(
                Note.class,
                R.layout.layout_notes,
                NoteViewHolder.class,
                mDBnoteUser
        ) {
            @Override
            protected void populateViewHolder(NoteViewHolder viewHolder, Note model, final int position) {
                final String note_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setTime(model.getTime());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent z = new Intent(getActivity(), EditNoteActivity.class);
                        z.putExtra("note_key", note_key);
                        startActivity(z);
                    }
                });

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
            }
        };

        mNoteList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView noteTitle = (TextView) mView.findViewById(R.id.note_title);
            noteTitle.setText(title);
        }

        public void setContent(String content) {
            TextView noteContent = (TextView) mView.findViewById(R.id.note_content);
            noteContent.setText(content);
        }

        public void setTime(String time) {
            TextView noteTime = (TextView) mView.findViewById(R.id.note_time);
            noteTime.setText(time);
        }
    }
}
