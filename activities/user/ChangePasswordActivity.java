package licenta.beatyourmeal.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.user.User;
import licenta.beatyourmeal.database.user.UserService;
import licenta.beatyourmeal.activities.other.LoginActivity;

public class ChangePasswordActivity extends AppCompatActivity {
    public static final String CHANGE_PASSWORD_KEY = "changePasswordKey";
    private TextView tvCurrentPassword;
    private TextInputEditText tietNewPassword;
    private TextInputEditText tietConfirmNewPassword;
    private Button btnSubmit;

    private long userId;
    private String reason;
    private Intent intent;
    private UserService userService;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initComponents();
        intent=getIntent();
        userId=intent.getLongExtra("user_id",userId);
        reason=intent.getStringExtra("reason");
        userService=new UserService(getApplicationContext());
        userService.getUserById(userId,getUserByIdCallback());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_asset_logo_wide);
        getSupportActionBar().setTitle("");
    }

    private Callback<User> getUserByIdCallback() {
        return new Callback<User>() {
            @Override
            public void runResultOnUiThread(User result) {
                if (result != null) {
                    user=new User(result.getId(),result.getName(),result.getUsername(),result.getPassword(),result.getSecurityAnswer());
                    tvCurrentPassword.setText(result.getPassword());
                }
            }
        };
    }

    private void initComponents() {
        tvCurrentPassword =findViewById(R.id.change_password_tv_current_password);
        tietNewPassword=findViewById(R.id.change_password_tiet_new_password);
        tietConfirmNewPassword=findViewById(R.id.change_password_tiet_confirm_new_password);
        btnSubmit =findViewById(R.id.change_password_btn_submit);
        btnSubmit.setOnClickListener(saveNewPasswordEventListener());
    }

    private View.OnClickListener saveNewPasswordEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    createFromViews();
                    userService.update(user, changePasswordCallback());
                    finish();
                }
            }
        };
    }

    private Callback<User> changePasswordCallback() {
        return new Callback<User>() {
            @Override
            public void runResultOnUiThread(User result) {
                if (result != null) {
                    if(reason.equals("ForgotPassword"))
                    {
                        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Password successfully changed!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Password successfully changed!",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        };
    }

    private boolean isValid() {
        if (tietNewPassword.getText() == null || tietNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Invalid new password!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (tietConfirmNewPassword.getText() == null || tietConfirmNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Invalid confirmation for new password!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (!tietConfirmNewPassword.getText().toString().equals(tietNewPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(),
                    "The introduced password don't match!",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }
    private void createFromViews() {
        String newPassword = tietNewPassword.getText().toString();
        user.setPassword(newPassword);
    }

    }
