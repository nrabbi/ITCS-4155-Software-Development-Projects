// Nazmul Rabbi
// ITCS 4155 : Event Finder
// Event.java
// Group 12
// 3/20/18

package com.example.nrabbi.itcs4155;

public class Event {
    String title, city, state, country, zip, url, address, description, startTime, endTIme;

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
                ", url='" + url + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTIme='" + endTIme + '\'' +
                '}';
    }
}
