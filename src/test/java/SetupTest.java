import org.junit.jupiter.api.BeforeAll;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SetupTest {
    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }
}   