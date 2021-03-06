package com.example.usp05.githubtry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usp05.githubtry.inventory_display.InventoryActivity;
import com.example.usp05.githubtry.user_handling.DatabaseHelper;
import com.example.usp05.githubtry.user_handling.ForgotPassword;
import com.example.usp05.githubtry.user_handling.Register;
import com.example.usp05.githubtry.user_handling.UserHandler;

public class MainActivity extends AppCompatActivity {
    private final DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginClick(View view) {
        if((view.getId() == R.id.loginButton)) {
            EditText username = findViewById(R.id.usernameLogin);
            EditText password = findViewById(R.id.passwordLogin);
            String usernameStr = username.getText().toString();
            String passwordStr = password.getText().toString();

            if(usernameStr.isEmpty()) {
                username.setError("Enter username");
            }
            else if(passwordStr.isEmpty()) {
                password.setError("Enter password");
            }
            else if("not found".equals(helper.searchUsernameAndPassword(usernameStr, passwordStr))) {
                Toast message = Toast.makeText(this, "Incorrect Username and Password!", Toast.LENGTH_SHORT);
                message.show();
            }
            else {

                UserHandler UH = UserHandler.getInstance(helper.getUser(usernameStr));

                Intent i = new Intent(this, InventoryActivity.class);
                startActivity(i);
            }
        }
    }

    public void onSignUpClick(View view) {
        if(view.getId() == R.id.signUpButton) {
            Intent i = new Intent(this, Register.class);
            startActivity(i);
        }
    }

    public void onForgotPasswordClick(View view) {
        if(view.getId() == R.id.forgotPassword) {
            Intent i = new Intent(this, ForgotPassword.class);
            startActivity(i);
        }
    }

}
