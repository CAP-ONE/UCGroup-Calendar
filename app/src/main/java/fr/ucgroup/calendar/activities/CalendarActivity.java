package fr.ucgroup.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.ucgroup.calendar.R;
import fr.ucgroup.calendar.http.UCGroupAPIService;
import fr.ucgroup.calendar.models.User;
import fr.ucgroup.calendar.models.Weather;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class CalendarActivity extends BaseActivity implements RobotoCalendarView.RobotoCalendarListener {

    private Weather weather;
    private RobotoCalendarView robotoCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Realm realm = Realm.getDefaultInstance();

        String username = getIntent().getStringExtra("username");

        User user = realm.where(User.class).equalTo("username", username).findFirst();

        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(getSupportActionBar().getTitle()+" "+user.getFirstname());


        ProgressBar progressBar = (ProgressBar) findViewById(R.id.calendar_progress);

        robotoCalendarView = (RobotoCalendarView) findViewById(R.id.robotoCalendarPicker);

        robotoCalendarView.setRobotoCalendarListener(this);


        UCGroupAPIService.weather(user.getToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(weather -> {
                    if(weather.message == null) {
                        this.weather = weather;
                        fillCalendar();
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(this, weather.message, Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {

                });
    }

    private void fillCalendar() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            Date date = format.parse(weather.month);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        robotoCalendarView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDayClick(Calendar calendar) {
        Toast.makeText(this, "onDayClick: " + calendar.getTime(), Toast.LENGTH_SHORT).show();


        Date date = calendar.getTime();
        int day = date.getDay();
        Intent intent = new Intent(CalendarActivity.this, WeatherActivity.class);
        intent.putExtra("city", weather.city);
        intent.putExtra("day", day);
        startActivity(intent);
    }

    @Override
    public void onDayLongClick(Calendar calendar) {

    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void onLeftButtonClick() {

    }
}
