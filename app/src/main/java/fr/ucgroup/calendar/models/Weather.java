package fr.ucgroup.calendar.models;

import java.util.List;

public class Weather {

    public String success;
    public String message;
    public String month;
    public String city;
    public List<Days> days;


    public class Days {
        public String day;
        public String meteo;
        public String temperature;
    }
}

