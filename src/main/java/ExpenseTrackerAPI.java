import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpenseTrackerAPI {
    public static void main (String[] args) {
        SpringApplication.run(
                ExpenseTrackerAPI.class, args
        );
    }
}
