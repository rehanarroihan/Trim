package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private TextView tvRegTitle, tvGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvRegTitle = (TextView) findViewById(R.id.textViewRegTitle);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/diamonde.ttf");
        tvRegTitle.setTypeface(custom_font);
        tvRegTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);

        tvGoLogin = (TextView) findViewById(R.id.textViewGoLogin);
        tvGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(s);
                finish();
            }
        });
    }
}
