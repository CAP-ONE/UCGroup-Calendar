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
public class InscriptionActivity extends BaseActivity  {

    // UI references.
    private AutoCompleteTextView mUsernameView, mFirstnameView, mLastnameView;
    private EditText mPasswordView;
    private View mLoginFormView;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mFirstnameView = (AutoCompleteTextView) findViewById(R.id.register_firstname);
        mLastnameView = (AutoCompleteTextView) findViewById(R.id.register_lastname);

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptRegister();
                return true;
            }
            return false;
        });

        Button mLoginButton = (Button) findViewById(R.id.form_button_login);
        Button mRegisterButton = (Button) findViewById(R.id.form_button_register);

        mLoginButton.setOnClickListener(view -> startActivity(new Intent(InscriptionActivity.this, LoginActivity.class)));

        mRegisterButton.setOnClickListener(view -> attemptRegister());

        mLoginFormView = findViewById(R.id.login_form);

        realm = Realm.getDefaultInstance();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mFirstnameView.setError(null);
        mLastnameView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String firstname = mFirstnameView.getText().toString();
        String lastname = mLastnameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for valid fields.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstname)) {
            mFirstnameView.setError(getString(R.string.error_field_required));
            focusView = mFirstnameView;
            cancel = true;
        } else if (!isNameValid(firstname)) {
            mFirstnameView.setError(getString(R.string.error_invalid_firstname));
            focusView = mFirstnameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(lastname)) {
            mLastnameView.setError(getString(R.string.error_field_required));
            focusView = mLastnameView;
            cancel = true;
        } else if (!isNameValid(lastname)) {
            mLastnameView.setError(getString(R.string.error_invalid_lastname));
            focusView = mLastnameView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true, getString(R.string.progress_registering));

            UCGroupAPIService.register(username, password,firstname,lastname)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(apiResponse -> {
                        showProgress(false, null);
                        if(apiResponse.success) {
                            Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            realm.executeTransaction(realmT -> realmT.insertOrUpdate(new User(apiResponse.token, username, password, firstname, lastname)));
                            Intent intent = new Intent(InscriptionActivity.this, CalendarActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                    }, throwable -> {

                    });
        }
    }

    private boolean isUsernameValid(String username) {
        return username.matches("[A-Za-z0-9]*");
    }

    private boolean isNameValid(String name) {
        return name.matches("[A-Za-z]*");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

}

