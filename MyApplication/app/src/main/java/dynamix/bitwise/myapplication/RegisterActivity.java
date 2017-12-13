package dynamix.bitwise.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by AJ on 11/8/2017.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    //private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     *
     * @param
     * @return void
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        //databaseHelper = new DatabaseHelper(activity);
        user = new User();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                postDataToSQL();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private class MyThread extends AsyncTask<Object, Void, Boolean> {
        private static final String url = "jdbc:mysql://sheltermatch.cdpafwooszcm.us-east-2.rds.amazonaws.com:3306/users";
        private static final String user = "Memegineer";
        private static final String pass = "12345678";

        @Override
        protected void onPreExecute() {
            Intent intentRegister = new Intent(getApplicationContext(), Splash.class);
            startActivity(intentRegister);
        }

        @Override
        protected Boolean doInBackground(Object... objects) {
            boolean isSuccess = false;
            int code = (Integer) objects[0];
            User newUser = (User) objects[1];
            ResultSet rs = null;
            switch (code) {
                case 1: //Add user to database
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection(url, user, pass);
                        String query = "INSERT INTO sheltermatch_users (user_name, user_pass, user_email) VALUES (?, ?, ?)";
                        PreparedStatement ps = con.prepareStatement(query);

                        Protect protect = new Protect();
                        String pw = protect.generateStrongPasswordHash(newUser.getPassword());
                        ps.setString(1, newUser.getUserName());
                        ps.setString(2, pw);
                        ps.setString(3, newUser.getEmail());
                        ps.execute();
                        con.close();
                        return true;
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                        return false;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                        return false;
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        return false;
                    }
                case 2: //login user
                    break;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if(isSuccess) {
                Toast.makeText(activity, "Registration successful!", Toast.LENGTH_LONG).show();
                Intent intentRegister = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentRegister);
            }
            else {
                Toast.makeText(activity, "Account already exists!", Toast.LENGTH_LONG).show();
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQL() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }
        user.setName(textInputEditTextName.getText().toString().trim());
        user.setEmail(textInputEditTextEmail.getText().toString().trim());
        user.setPassword(textInputEditTextPassword.getText().toString().trim());
        new MyThread().execute(1, user);
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}

