package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        setTitle(getResources().getString(R.string.add_todo));
    }
}
