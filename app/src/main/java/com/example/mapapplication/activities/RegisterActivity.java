package com.example.mapapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.mapapplication.R;
import com.example.mapapplication.data.user.UserEntity;
import com.example.mapapplication.data.user.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private Drawable ErrorDrawable;
    private Button singUpButton;

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ErrorDrawable = getResources().getDrawable(R.drawable.icon_error);
        ErrorDrawable.setBounds(0, 0, ErrorDrawable.getIntrinsicWidth(), ErrorDrawable.getIntrinsicHeight());

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        singUpButton = findViewById(R.id.singup_button);
        textInputEmail = findViewById(R.id.text_singup_email);
        textInputUsername = findViewById(R.id.text_singup_username);
        textInputPassword = findViewById(R.id.text_singup_password);

        // Clicking on singUp button when register
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailInput = textInputEmail.getEditText().getText().toString().trim();
                String usernameInput = textInputUsername.getEditText().getText().toString().trim();
                String passwordInput = textInputPassword.getEditText().getText().toString().trim();

                validateEmail();
                validateUsername();
                validatePassword();

                if (validateEmail() && validateUsername() && validatePassword()){
                    RegisterNewUser(emailInput, usernameInput, passwordInput);
                }
            }
        });

        // Process the textInputEmail editText when typing email
        textInputEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputEmail.getEditText().setTextColor(Color.DKGRAY);
                textInputEmail.setError(null);
                textInputEmail.getEditText().setCompoundDrawables(null, null, null, null);

                if (!TextUtils.isEmpty(textInputEmail.getEditText().getText()) && !TextUtils.isEmpty(textInputUsername.getEditText().getText()) && !TextUtils.isEmpty(textInputPassword.getEditText().getText())) {
                    singUpButton.setAlpha(1f);
                } else {
                    singUpButton.setAlpha(0.65f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Process the textInputUsername editText when typing usename
        textInputUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputUsername.getEditText().setTextColor(Color.DKGRAY);
                textInputUsername.setError(null);
                textInputUsername.getEditText().setCompoundDrawables(null, null, null, null);

                if (!TextUtils.isEmpty(textInputEmail.getEditText().getText()) && !TextUtils.isEmpty(textInputUsername.getEditText().getText()) && !TextUtils.isEmpty(textInputPassword.getEditText().getText())) {
                    singUpButton.setAlpha(1f);
                } else {
                    singUpButton.setAlpha(0.65f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Process the textInputPassword editText when typing password
        textInputPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputPassword.getEditText().setTextColor(Color.DKGRAY);
                textInputPassword.setError(null);
                textInputPassword.getEditText().setCompoundDrawables(null, null, null, null);

                if (!TextUtils.isEmpty(textInputEmail.getEditText().getText()) && !TextUtils.isEmpty(textInputUsername.getEditText().getText()) && !TextUtils.isEmpty(textInputPassword.getEditText().getText())) {
                    singUpButton.setAlpha(1f);
                } else {
                    singUpButton.setAlpha(0.65f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Inser new user to Room database
    private void RegisterNewUser(String email, String username, String password) {
        UserEntity userEntity = new UserEntity(email, username, password);

        userViewModel.insert(userEntity);

        UserEntity u = userViewModel.findUserByUsername(userEntity.username);
        Toast.makeText(this, "Added the user", Toast.LENGTH_LONG).show();
        finish();
    }

    // Check the Email in textInputEmail EditText
    private boolean validateEmail() {
        String usernameInput = textInputEmail.getEditText().getText().toString().trim();
        if (usernameInput.isEmpty()) {
            textInputEmail.setError(" ");
            textInputEmail.getEditText().setError("Field can't be empty", ErrorDrawable);
            return false;
        }  else {
            textInputEmail.setError(null);
            return true;
        }
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

    // Close the Keyboard when clicking on screen body
    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}