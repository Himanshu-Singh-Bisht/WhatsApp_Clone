package com.himanshu.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmailSignUp , edtUsernameSignUp ,edtPasswordSignUp;
    private Button btnSignUp , btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        setTitle("SignUp");

        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        edtUsernameSignUp = findViewById(R.id.edtEmailLogIn);
        edtPasswordSignUp = findViewById(R.id.edtPasswordLogIn);

        btnSignUp = findViewById(R.id.btnLogInLogIn);
        btnLogIn = findViewById(R.id.btnSignUpLogIn);

        btnSignUp.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);

        edtPasswordSignUp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnSignUp);
                }
                return false;
            }
        });


        if(ParseUser.getCurrentUser() != null)
        {
            Intent intent = new Intent(SignUp.this ,WhatsAppUser.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btnLogInLogIn:

                if(edtEmailSignUp.getText().toString().equals("") ||
                edtPasswordSignUp.getText().toString().equals("") ||
                edtUsernameSignUp.getText().toString().equals(""))
                {
                    FancyToast.makeText(SignUp.this , "Email , Username , Password is required!" ,
                            FancyToast.LENGTH_SHORT , FancyToast.INFO , true).show();
                }
                else {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEmailSignUp.getText().toString());
                    appUser.setUsername(edtUsernameSignUp.getText().toString());
                    appUser.setPassword(edtPasswordSignUp.getText().toString());

                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Signing Up " + edtUsernameSignUp.getText().toString());
                    dialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null)
                            {
                                FancyToast.makeText(SignUp.this , appUser.getUsername() + " is signed up." ,
                                        FancyToast.LENGTH_SHORT , FancyToast.SUCCESS , true).show();

                                Intent intent = new Intent(SignUp.this , WhatsAppUser.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                FancyToast.makeText(SignUp.this , e.getMessage() ,
                                        FancyToast.LENGTH_SHORT , FancyToast.ERROR , true).show();
                            }

                            dialog.dismiss();
                        }
                    });


                }

                break;

            case R.id.btnSignUpLogIn:

                Intent intent = new Intent(SignUp.this , LogIn.class);
                startActivity(intent);
                finish();

                break;
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
