import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

public class SeleniumTestNGTest
{
    private static WebDriver AutoDriver;
    @BeforeAll
    public static void Initialize()
    {
        AutoDriver = WebDriverManager.chromedriver().create();
        AutoDriver.manage().window().maximize();
    }

    @Test
    public void Test()
    {
        AutoDriver.navigate().to("https://demoqa.com/");
        System.out.println(AutoDriver.getTitle());
    }

    @AfterAll
    public static void Teardown()
    {
        AutoDriver.quit();
    }
}
