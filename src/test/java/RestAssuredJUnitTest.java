import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.*;
import entities.Country;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.path.json.JSONAssertion;
import io.restassured.response.*;
import io.restassured.specification.*;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestAssuredJUnitTest
{
    private static WireMockServer wireMockServer;
    private static String baseUrl;
    private static RequestSpecification requestSpecification;
    private Response response;
    private ObjectMapper objectMapper;

    private List<Country> countries;
    private Country country;

    @BeforeAll
    public static void Initialize()
    {
        //Create wiremock server instance
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        //Configure host and port
        configureFor("localhost", 8090);
        baseUrl = "http://localhost:8080";

        //Create HTTP request
        requestSpecification = RestAssured.given().baseUri(baseUrl);
    }

    @Test
    @DisplayName("Get all countries and verify status OK")
    @Order(1)
    public void Test_GetCountries() throws Exception
    {
        //Create mock list for external dependencies
        countries = new ArrayList<Country>();
        countries.add(new Country(1,"Philippines","Manila"));
        countries.add(new Country(2,"USA","Washington"));
        countries.add(new Country(3,"Japan","Tokyo"));
        objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(countries);

        //Create stub for get countries endpoint
        //Given
        String baseResource = "/countries";
        stubFor(get(urlEqualTo(baseResource))
                .willReturn(aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBody(jsonBody)
                        .withStatus(200)));

        //When
        response = requestSpecification.get(baseResource);

        //Then
        System.out.println("Content Type: " + response.getContentType());
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response JSON Body: " + response.asPrettyString());

        //Assert
        assertEquals(HttpStatus.OK_200, response.getStatusCode());
        JSONAssert.assertEquals(jsonBody, response.asPrettyString(), false);
    }

    @AfterAll
    public static void Teardown()
    {
        wireMockServer.stop();
    }
}
