package licenta.beatyourmeal.activities.other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import licenta.beatyourmeal.R;

public class OpenAppActivity extends AppCompatActivity {
    ImageView imgAppLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_app);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
        imgAppLogo=findViewById(R.id.open_app_img_app_logo);
        imgAppLogo.setOnClickListener(openAppEventListener());
    }

    private View.OnClickListener openAppEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
              startActivity(intent);
              finish();
            }
        };
    }
}