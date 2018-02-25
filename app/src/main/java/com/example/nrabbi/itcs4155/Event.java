package com.example.nrabbi.itcs4155;

/**
 * Created by Nazmul Rabbi on 2/20/2018.
 */

public class Event {
    String title, city, state, country, zip;

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
