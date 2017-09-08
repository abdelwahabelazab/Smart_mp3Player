package com.abdelwahabelazab.smartmp3player;

/**
 * Created by Alazab on 8/19/2017.
 */
public class Utilities {

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";
        String minutesString="";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        if(minutes < 10){
            minutesString = "0" + minutes;
        }else{
            minutesString = "" + minutes;}

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
}
