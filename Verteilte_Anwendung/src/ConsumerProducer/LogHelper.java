package ConsumerProducer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHelper {

    public static void printThreadLog(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(GetTime.getDate());
        sb.append(" (Thread #");
        sb.append(Thread.currentThread().getId());
        sb.append("): ");
        sb.append(message);
        System.out.println(sb);
    }
}
