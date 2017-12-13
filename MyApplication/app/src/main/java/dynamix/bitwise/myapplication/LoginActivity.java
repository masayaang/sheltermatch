package dynamix.bitwise.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initViews();
        initListener();
        initObjects();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }

    private void initListener() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        user = new User();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQL(); break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /*
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQL() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        user.setEmail(textInputEditTextEmail.getText().toString().trim());
        user.setPassword(textInputEditTextPassword.getText().toString().trim());
        new LoginThread().execute(user);
    }


    private class LoginThread extends AsyncTask<Object, Void, Boolean> {
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
            User newUser = (User) objects[0];
            String query = "SELECT * FROM sheltermatch_users WHERE user_email = '" + newUser.getEmail() + "'";
            ResultSet rs = null;
            Validate validate = new Validate();
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    if(validate.validatePassword(newUser.getPassword(), rs.getString("user_pass"))) {
                        return true;
                    }
                }
                st.close();
                con.close();
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
                return false;
            }
        }


        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if(isSuccess) {
                Toast.makeText(activity, "Successful login!", Toast.LENGTH_LONG).show();
                /* IF LOGIN IS SUCCESSFUL, THEN GO TO JAN'S ACTIVITY */
                Intent intentRegister = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentRegister);
            }

            else {
                Toast.makeText(activity, "Invalid email or password!", Toast.LENGTH_LONG).show();
                Intent intentRegister = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentRegister);
            }
        }
    }
}
