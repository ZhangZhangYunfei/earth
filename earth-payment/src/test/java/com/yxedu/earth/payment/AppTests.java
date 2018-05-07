package com.yxedu.earth.payment;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTests {

  @LocalServerPort
  private int port;

  @Test
  public void shouldReturnHealthy() throws Exception {
    when()
        .get(String.format("http://localhost:%s/health", port))
        .then()
        .statusCode(is(200))
       .body(containsString("Composite Discovery Client"));
  }
}
