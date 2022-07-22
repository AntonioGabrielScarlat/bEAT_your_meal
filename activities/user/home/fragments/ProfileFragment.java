package licenta.beatyourmeal.activities.user.home.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import licenta.beatyourmeal.R;
import licenta.beatyourmeal.async.Callback;
import licenta.beatyourmeal.database.biometricData.BiometricData;
import licenta.beatyourmeal.database.biometricData.BiometricDataService;
import licenta.beatyourmeal.database.rating.Rating;
import licenta.beatyourmeal.database.rating.RatingService;
import licenta.beatyourmeal.database.user.User;
import licenta.beatyourmeal.database.user.UserService;
import licenta.beatyourmeal.auxiliary.converters.DateConverter;
import licenta.beatyourmeal.activities.other.LoginActivity;
import licenta.beatyourmeal.activities.user.ChangePasswordActivity;
import licenta.beatyourmeal.activities.user.UpdateWeightActivity;

public class ProfileFragment extends Fragment {
    public static final String CHANGE_PASSWORD_KEY = "changePasswordKey";
    public static final String UPDATE_WEIGHT_KEY = "updateWeightKey";
    private TextView tvSex;
    private TextView tvWeight;
    private TextView tvHeight;
    private TextView tvBirthDate;
    private Button btnUpdateWeight;
    private Button btnLogout;
    private Button btnChangePassword;
    private Button btnDeleteAccount;
    private RatingBar rbRateApp;
    private Button btnSubmitRating;
    private AlertDialog.Builder builder;

    private Rating currentRating;
    private ActivityResultLauncher<Intent> updateWeightLauncher;

    private UserService userService;
    private BiometricDataService biometricDataService;
    private RatingService ratingService;
    private long userId;
    private User user;
    private BiometricData biometricData;

    public ProfileFragment(long userId) {
        this.userId = userId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWeightLauncher=getUpdateWeightLauncher();
        userService=new UserService(getContext().getApplicationContext());
        biometricDataService=new BiometricDataService(getContext().getApplicationContext());
        ratingService=new RatingService(getContext().getApplicationContext());
        builder = new AlertDialog.Builder(this.getContext());
        userService.getUserById(userId,getUserByIdCallback());
        biometricDataService.getBiometricDataByUserId(userId,getBiometricDataByUserIdCallback());
        ratingService.getRatingByUserId(userId,getRatingByUserIdCallback());
    }

    private Callback<Rating> getRatingByUserIdCallback() {
        return new Callback<Rating>() {
            @Override
            public void runResultOnUiThread(Rating result) {
                if (result != null) {
                    currentRating=new Rating(result.getId(),result.getRatingValue(),result.getIdUser());
                    rbRateApp.setRating(currentRating.getRatingValue().floatValue());
                }
            }
        };
    }

    private ActivityResultLauncher<Intent> getUpdateWeightLauncher() {
        ActivityResultCallback<ActivityResult> callback = getUpdateWeightActivityResultCallback();
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), callback);
    }

    private ActivityResultCallback<ActivityResult> getUpdateWeightActivityResultCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result != null && result.getResultCode() == -1 && result.getData() != null) {
                    BiometricData biometricData = (BiometricData) result.getData().getSerializableExtra(UpdateWeightActivity.UPDATE_WEIGHT_KEY);
                    biometricDataService.update(biometricData, updateWeightCallback());
                }
            }
        };
    }

    private Callback<BiometricData> updateWeightCallback() {
        return new Callback<BiometricData>() {
            @Override
            public void runResultOnUiThread(BiometricData result) {
                if (result != null) {
                    Double lostKg=Double.parseDouble(tvWeight.getText().toString())-result.getWeight();
                    tvWeight.setText(result.getWeight().toString());
                    if(lostKg>0){
                        String message="Wow! You lost "+lostKg+"kg. Congratulations!";
                        Toast.makeText(getContext().getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }

                }
            }
        };
    }

    private Callback<BiometricData> getBiometricDataByUserIdCallback() {
        return new Callback<BiometricData>() {
            @Override
            public void runResultOnUiThread(BiometricData result) {
                if (result != null) {
                    biometricData=new BiometricData(result.getId(),result.getSex(),result.getHeight(),result.getWeight(),result.getBirthDate(),result.getIdUser());
                    tvSex.setText(result.getSex());
                    tvWeight.setText(result.getWeight().toString());
                    tvHeight.setText(result.getHeight().toString());
                    tvBirthDate.setText(DateConverter.fromDate(result.getBirthDate()));
                }
            }
        };
    }

    private Callback<User> getUserByIdCallback() {
        return new Callback<User>() {
            @Override
            public void runResultOnUiThread(User result) {
                if (result != null) {
                    user=new User(result.getId(),result.getName(),result.getUsername(),result.getPassword(),result.getSecurityAnswer());
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_user_profile,container,false);
        initComponents(view);
        return view;
    }
    private void initComponents(View view) {
        tvSex=view.findViewById(R.id.fragment_user_profile_tv_sex);
        tvWeight=view.findViewById(R.id.fragment_user_profile_tv_weight);
        tvHeight=view.findViewById(R.id.fragment_user_profile_tv_height);
        tvBirthDate=view.findViewById(R.id.fragment_user_profile_tv_birth_date);
        btnUpdateWeight=view.findViewById(R.id.fragment_user_profile_btn_change_weight);
        btnLogout=view.findViewById(R.id.fragment_user_profile_btn_logout);
        btnChangePassword=view.findViewById(R.id.fragment_user_profile_btn_change_password);
        btnDeleteAccount=view.findViewById(R.id.fragment_user_profile_btn_delete_account);
        rbRateApp=view.findViewById(R.id.fragment_user_profile_rb_rate_app);
        btnSubmitRating=view.findViewById(R.id.fragment_user_profile_btn_submit_rating);
        btnUpdateWeight.setOnClickListener(updateWeightEventListener());
        btnLogout.setOnClickListener(logoutEventListener());
        btnChangePassword.setOnClickListener(changePasswordEventListener());
        btnDeleteAccount.setOnClickListener(deleteAccountEventListener());
        btnSubmitRating.setOnClickListener(submitRatingEventListener());
    }

    private View.OnClickListener logoutEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intent=new Intent(getContext().getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener submitRatingEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentRating!=null)
                {
                    currentRating.setRatingValue(Double.parseDouble(String.valueOf(rbRateApp.getRating())));
                    ratingService.update(currentRating,updateRatingCallback());
                }
                else
                {
                    currentRating=new Rating(Double.parseDouble(String.valueOf(rbRateApp.getRating())),userId);
                    ratingService.insert(currentRating,insertRatingCallback());
                }

            }
        };
    }

    private Callback<Rating> insertRatingCallback() {
        return new Callback<Rating>() {
            @Override
            public void runResultOnUiThread(Rating result) {
                if (result != null) {
                    Toast.makeText(getContext().getApplicationContext(),"Rating successfully submitted!",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Callback<Rating> updateRatingCallback() {
        return new Callback<Rating>() {
            @Override
            public void runResultOnUiThread(Rating result) {
                if (result != null) {
                    Toast.makeText(getContext().getApplicationContext(),"Rating successfully submitted!",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private View.OnClickListener updateWeightEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), UpdateWeightActivity.class);
                intent.putExtra("biometric_data_id",biometricData.getId());
                updateWeightLauncher.launch(intent);
            }
        };
    }

    private View.OnClickListener changePasswordEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), ChangePasswordActivity.class);
                intent.putExtra("user_id",userId);
                intent.putExtra("reason","ChangePassword");
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener deleteAccountEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to delete this account?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                userService.delete(user,deleteUserCallback());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete Account");
                alert.show();
            }
        };
    }

    private Callback<Boolean> deleteUserCallback() {
        return new Callback<Boolean>() {
            @Override
            public void runResultOnUiThread(Boolean result) {
                if (result) {
                    Toast.makeText(getContext().getApplicationContext(),"Account successfully deleted!",
                            Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getContext().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

}
