package licenta.beatyourmeal.activities.user.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.activities.user.home.fragments.HomeFragment;
import licenta.beatyourmeal.activities.user.home.fragments.MealPlanFragment;
import licenta.beatyourmeal.activities.user.home.fragments.ProfileFragment;

public class UserHomeActivity extends AppCompatActivity {
    private long userId;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
        intent=getIntent();
        userId=intent.getLongExtra("user_id",userId);
        BottomNavigationView bottomNavigationView=findViewById(R.id.user_home_bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.user_home_fragment_container,
                new HomeFragment(userId)).commit();
    }

    private NavigationBarView.OnItemSelectedListener navigationItemSelectedListener= new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch(item.getItemId()){
                case R.id.nav_user_home:
                    selectedFragment=new HomeFragment(userId);
                    break;
                case R.id.nav_user_meal_plan:
                    selectedFragment=new MealPlanFragment(userId);
                    break;
                case R.id.nav_user_profile:
                    selectedFragment=new ProfileFragment(userId);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.user_home_fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };
}