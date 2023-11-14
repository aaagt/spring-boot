package aaagt.springboot.app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
    private static final GenericContainer<?> devapp = new GenericContainer("devapp:latest")
            .withExposedPorts(8080);
    private static final GenericContainer<?> prodapp = new GenericContainer("prodapp:latest")
            .withExposedPorts(8081);
    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public static void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void contextLoads() {
        final var urlPattern = "http://localhost:%s/profile";
        ResponseEntity<String> devEntity = restTemplate.getForEntity(urlPattern.formatted(devapp.getMappedPort(8080)), String.class);
        ResponseEntity<String> prodEntity = restTemplate.getForEntity(urlPattern.formatted(prodapp.getMappedPort(8081)), String.class);
        final var devPayload = devEntity.getBody();
        final var prodPayload = prodEntity.getBody();

        System.out.println(devPayload);
        System.out.println(prodPayload);

        assertEquals("Current profile is dev", devPayload);
        assertEquals("Current profile is production", prodPayload);
    }

}
