package fr.ucgroup.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.ucgroup.calendar.R;
import fr.ucgroup.calendar.http.UCGroupAPIService;
import fr.ucgroup.calendar.models.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.login_username);

        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        Button mLoginButton = (Button) findViewById(R.id.form_button_login);
        Button mRegisterButton = (Button) findViewById(R.id.form_button_register);

        mLoginButton.setOnClickListener(view -> attemptLogin());

        mRegisterButton.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, InscriptionActivity.class)));

        mLoginFormView = findViewById(R.id.login_form);

        realm = Realm.getDefaultInstance();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true, getString(R.string.progress_signin));

            UCGroupAPIService.authenticate(username, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(apiResponse -> {
                        showProgress(false, null);
                        if(apiResponse.success) {
                            realm.executeTransaction(realmT -> realmT.where(User.class).equalTo("username", username).findFirst().token = apiResponse.token);
                            Toast.makeText(this, "Authentification réussie", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(this, apiResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }, throwable -> {

                    });

        }
    }

    private boolean isUsernameValid(String username) {
        return username.matches("[A-Za-z0-9]*");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

}

