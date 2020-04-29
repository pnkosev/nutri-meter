package pn.nutrimeter.web.base;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import pn.nutrimeter.base.TestBase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiTestBase extends TestBase {
    @LocalServerPort
    protected int port;

    protected String getFullUrl(String route) {
        return "http://localhost:" + port + route;
    }

    protected TestRestTemplate getRestTemplate() {
        return new TestRestTemplate();
    }

    protected TestRestTemplate getRestTemplate(String username, String password) {
        if (username != null && password != null) {
            return new TestRestTemplate(username, password);
        }

        return getRestTemplate();
    }
}
