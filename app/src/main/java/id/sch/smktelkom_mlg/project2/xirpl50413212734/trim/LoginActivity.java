package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private TextView tvLoginTitle, tvGoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvLoginTitle = (TextView) findViewById(R.id.textViewLoginTitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/diamonde.ttf");
        tvLoginTitle.setTypeface(custom_font);
        tvLoginTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);

        tvGoRegister = (TextView) findViewById(R.id.textViewGoRegister);
        tvGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(s);
                finish();
            }
        });

    }
}
