package com.astronaut.TubesTestFinalFixInsyaAllah;


public class Reminder {
    private int id;
    private String title;
    private String date;
    private String time;
    private String repeat;
    private String repeatNo;
    private String repeatType;
    private String active;


    public Reminder(int ID, String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active){
        id = ID;
        title = Title;
        date = Date;
        time = Time;
        repeat = Repeat;
        repeatNo = RepeatNo;
        repeatType = RepeatType;
        active = Active;
    }

    public Reminder(String Title, String Date, String Time, String Repeat, String RepeatNo, String RepeatType, String Active){
        title = Title;
        date = Date;
        time = Time;
        repeat = Repeat;
        repeatNo = RepeatNo;
        repeatType = RepeatType;
        active = Active;
    }

    public Reminder(){}

    public int getID() {

        return id;
    }

    public void setID(int ID) {

        id = ID;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {

        this.time = time;
    }

    public String getRepeatType() {

        return repeatType;
    }

    public void setRepeatType(String repeatType) {

        this.repeatType = repeatType;
    }

    public String getRepeatNo() {

        return repeatNo;
    }

    public void setRepeatNo(String repeatNo) {

        this.repeatNo = repeatNo;
    }

    public String getRepeat() {

        return repeat;
    }

    public void setRepeat(String repeat) {

        this.repeat = repeat;
    }

    public String getActive() {

        return active;
    }

    public void setActive(String active) {

        this.active = active;
    }
}
