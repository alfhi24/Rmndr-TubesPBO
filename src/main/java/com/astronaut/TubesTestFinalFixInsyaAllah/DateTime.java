package com.astronaut.TubesTestFinalFixInsyaAllah;

// date time object
public class DateTime {
    public int idx;
    public String datetime;


    public DateTime(int index, String DateTime){
        idx = index;
        datetime = DateTime;
    }

  //  public DateTime(){}


    public int getIndex() {
        return idx;
    }

    public void setIndex(int index) {
        idx = index;
    }

    public String getDateTime() {
        return datetime;
    }

    public void setDateTime(String dateTime) {
        datetime = dateTime;
    }
}
