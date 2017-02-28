package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.EditTodoActivity;
import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.R;
import id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.model.Todolist;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {
    private RecyclerView mTodoList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;
    private DatabaseReference mDBtodo, mDBtodoUser, mDatabase;
    private TextView tvNoTodo;

    private ProgressBar pbTodo;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvNoTodo = (TextView) getView().findViewById(R.id.textViewNoTodo);
        pbTodo = (ProgressBar) getView().findViewById(R.id.progressBarTodo);

        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        mDBtodo = mDB.getReference().child("todo");
        mDBtodoUser = mDBtodo.child(mAuth.getCurrentUser().getUid().toString());

        mDatabase = mDB.getReference().child("todo").child(mAuth.getCurrentUser().getUid().toString());

        mTodoList = (RecyclerView) getView().findViewById(R.id.recyclerViewTodo);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mTodoList.setLayoutManager(layoutManager);
        mTodoList.setHasFixedSize(true);

        mDBtodoUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long jumlahChild;
                jumlahChild = dataSnapshot.getChildrenCount();
                String strValue = jumlahChild.toString();
                Log.d("FirebaseCounter", "Jumlah child " + strValue);
                if (jumlahChild == 0) {
                    Log.d("FirebaseCounter", "No item yet !");
                    pbTodo.setVisibility(View.GONE);
                    tvNoTodo.setVisibility(View.VISIBLE);
                } else {
                    pbTodo.setVisibility(View.GONE);
                    tvNoTodo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseCounter", databaseError.getMessage());
                pbTodo.setVisibility(View.GONE);
                tvNoTodo.setVisibility(View.VISIBLE);
                tvNoTodo.setText("Error." + " " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<Todolist, TodolistViewHolder>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Todolist, TodolistViewHolder>(
                Todolist.class,
                R.layout.layout_todo,
                TodoFragment.TodolistViewHolder.class,
                mDBtodoUser
        ) {
            @Override
            protected void populateViewHolder(TodolistViewHolder viewHolder, Todolist model, final int position) {
                final String note_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDetail(model.getDetail());
                viewHolder.setDate(model.getDate());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent r = new Intent(getActivity(), EditTodoActivity.class);
                        r.putExtra("note_key", note_key);
                        startActivity(r);
                    }
                });

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });

                viewHolder.llDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabase.child(note_key).removeValue();
                        Snackbar.make(getView(), getResources().getString(R.string.done), Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        };

        mTodoList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class TodolistViewHolder extends RecyclerView.ViewHolder {
        View mView;
        LinearLayout llDone;

        public TodolistViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            llDone = (LinearLayout) mView.findViewById(R.id.linearLayoutDone);
        }

        public void setTitle(String title) {
            TextView todoTitle = (TextView) mView.findViewById(R.id.todo_title);
            todoTitle.setText(title);
        }

        public void setDetail(String detail) {
            TextView todoContent = (TextView) mView.findViewById(R.id.todo_detail);
            todoContent.setText(detail);
        }

        public void setDate(String date) {
            TextView todoDate = (TextView) mView.findViewById(R.id.todo_date);
            todoDate.setText(date);
        }
    }


}
