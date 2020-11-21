package com.example.mapapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.mapapplication.PermissionRequest;
import com.example.mapapplication.R;
import com.example.mapapplication.data.MyRoomDb;
import com.example.mapapplication.data.user.UserEntity;
import com.example.mapapplication.data.user.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    private ProgressDialog loadingBar;
    Drawable ErrorDrawable;

    private UserViewModel userViewModel;

    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.VIBRATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPermission()) {
            requestPermission();
        }
        setContentView(R.layout.activity_main);
//        MyRoomDb.getInstance(this);

        PermissionRequest.checkNetworkConnected(this);

        // Database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.findUserById((long)0);

        loginButton = findViewById(R.id.login_button);

        textInputUsername = findViewById(R.id.text_input_username);
        textInputPassword = findViewById(R.id.text_input_password);

        ErrorDrawable = getResources().getDrawable(R.drawable.icon_error);
        ErrorDrawable.setBounds(0, 0, ErrorDrawable.getIntrinsicWidth(), ErrorDrawable.getIntrinsicHeight());

        loadingBar = new ProgressDialog(this);

        // Clicking on loginButton
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUsername();
                validatePassword();

                if (validateUsername() && validatePassword()) {
                    SignInUser(textInputUsername.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "Email or password error", Toast.LENGTH_SHORT);
                }
            }
        });

        // Process the InputUserName editText when typing usename
        textInputUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputUsername.getEditText().setTextColor(Color.DKGRAY);
                textInputUsername.setError(null);
                textInputUsername.getEditText().setCompoundDrawables(null, null, null, null);

                if (!TextUtils.isEmpty(textInputUsername.getEditText().getText()) && !TextUtils.isEmpty(textInputPassword.getEditText().getText())) {
                    loginButton.setAlpha(1f);
//                    loginButton.setEnabled(true);
                } else {
                    loginButton.setAlpha(0.65f);
//                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Process the InputUserPassword editText when typing password
        textInputPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputPassword.getEditText().setTextColor(Color.DKGRAY);
                textInputPassword.setError(null);

                if (!TextUtils.isEmpty(textInputUsername.getEditText().getText()) && !TextUtils.isEmpty(textInputPassword.getEditText().getText())) {
                    loginButton.setAlpha(1f);
//                    loginButton.setEnabled(true);
                } else {
                    loginButton.setAlpha(0.65f);
//                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textInputUsername.getEditText().setText("taha");
        textInputPassword.getEditText().setText("123");
    }

    // Check the permission is granted or not
    private boolean checkPermission() {
        return PermissionRequest.hasPermissions(this, permissions);
    }

    // Request the permission if is not granted
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 200);
    }

    // Show the meesage to give permission or close the application
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "İzin Verildi", Toast.LENGTH_SHORT).show();
                // main logic
            } else {
                Toast.makeText(getApplicationContext(), "İzin Reddedildi", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermission();
                                    }
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    System.exit(0);
                                }
                            });
                    }
                }
            }
        }
    }

    // Permission message dialog
    private void showMessageOKCancel(DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                .setMessage("Erişim izinlerine izin vermeniz gerekiyor")
                .setPositiveButton("Evet", okListener)
                .setNegativeButton("Hayır", cancelListener)
                .create();

        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    // Check the Username in textInputUsername EditText
    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            textInputUsername.setError(" ");
            textInputUsername.getEditText().setError("Field can't be empty", ErrorDrawable);
            return false;
        }  else {
            textInputUsername.setError(null);
            return true;
        }
    }

    // Check the Password in textInputPassword EditText
    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    // Login used with "Giris button" to check and loging to next activity "Map Activity"
    private void SignInUser(final String username, final String password) {
        loadingBar.setTitle("Giriyor");
        loadingBar.setMessage("Bekle Lüften...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        UserEntity userEntity = userViewModel.findUserByUsernameAndPassword(username, password);

        if (userEntity != null) {
//            Toast.makeText(this, "Welcom " + userEntity.username, Toast.LENGTH_SHORT).show();

            textInputUsername.getEditText().setTextColor(Color.DKGRAY);
            textInputUsername.setError(null);
            textInputUsername.getEditText().setCompoundDrawables(null, null, null, null);

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("user_id", userEntity.getId());
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Check the UserName or Password", Toast.LENGTH_SHORT).show();

            UserEntity u = userViewModel.findUserByUsername(username);
            if (u != null) {
                textInputPassword.setError("Check the password");
            }
            else {
                textInputUsername.setError(" ");
                textInputUsername.getEditText().setError("No User by this name", ErrorDrawable);
                textInputPassword.setError("Check the password");
            }
        }
        loadingBar.dismiss();
    }

    // Sing Up new user used with "Uye Ol button"
    public void SingUpButton(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    // Close the Keyboard when clicking on screen body
    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}