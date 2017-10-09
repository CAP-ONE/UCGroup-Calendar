package fr.ucgroup.calendar.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fr.ucgroup.calendar.R;


public class BaseActivity extends AppCompatActivity {

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_title));
    }


    /**
     * Shows the progress UI
     */
    public void showProgress(final boolean show, String message) {
        if(message != null) {
            mProgressDialog.setMessage(message);
        }

        if(show)
            mProgressDialog.show();
        else if(mProgressDialog.isShowing())
            mProgressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mProgressDialog.dismiss();
    }


}
