package ikm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTest {

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.of(2015,4,4);
        System.out.println(localDate.format(DateTimeFormatter.ofPattern("MM dd, yyyy")));
        System.out.println(localDate.format(DateTimeFormatter.ofPattern("E, MMM dd, yyyy")));
        System.out.println(localDate.format(DateTimeFormatter.ofPattern("MM/dd/yy")));

        Locale locale=    new Locale("CN","zh"); 
        System.out.println(locale);


    }
}
