package com.himanshu.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmailLogIn , edtPasswordLogIn;
    private Button btnLogInLogIn , btnSignUpLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edtEmailLogIn = findViewById(R.id.edtEmailLogIn);
        edtPasswordLogIn = findViewById(R.id.edtPasswordLogIn);

        btnLogInLogIn = findViewById(R.id.btnLogInLogIn);
        btnSignUpLogIn = findViewById(R.id.btnSignUpLogIn);

        btnLogInLogIn.setOnClickListener(this);
        btnSignUpLogIn.setOnClickListener(this);

        edtPasswordLogIn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnLogInLogIn);
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser() != null)
        {
            Intent intent = new Intent(LogIn.this ,WhatsAppUser.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnLogInLogIn :

                if(edtEmailLogIn.getText().toString().equals("") ||
                        edtPasswordLogIn.getText().toString().equals(""))
                {
                    FancyToast.makeText(LogIn.this , "Email , Password is required!" ,
                            FancyToast.LENGTH_SHORT , FancyToast.INFO , true).show();
                }
                else {
                    ParseUser.logInInBackground(edtEmailLogIn.getText().toString(), edtPasswordLogIn.getText().toString(),
                            new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user != null && e == null)
                            {
                                FancyToast.makeText(LogIn.this , user.getUsername() + " is logged in!" ,
                                        FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();

                                Intent intent = new Intent(LogIn.this , WhatsAppUser.class);
                                startActivity(intent);

                                finish();

                            }
                            else
                            {
                                FancyToast.makeText(LogIn.this , e.getMessage() ,
                                        FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();
                            }
                        }
                    });
                }

                break;

            case R.id.btnSignUpLogIn :

                Intent intent = new Intent(LogIn.this , SignUp.class);
                startActivity(intent);

                finish();
        }
    }

    public void rootLayoutTapped(View view)
    {
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}