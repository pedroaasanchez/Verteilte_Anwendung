package ConsumerProducer;

import java.util.Date;

public class GetTime {
    public static long getIt(){
            //gets current time in milliseconds
            Date date = new Date();
            long timeMilli = date.getTime();
            return (timeMilli);
    }

    public static String getDate(){
        //gets current date and time (returns String)
        Date date = new Date();
        String stringDate = date.toString();
        return (stringDate);
    }
}
