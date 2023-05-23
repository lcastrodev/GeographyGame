package geographygame;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

public class AppTest {
  @Test
  public void successfulResponse() {
    App app = new App();
    APIGatewayProxyResponseEvent result = app.handleRequest(null, null);
    assertEquals(200, result.getStatusCode().intValue());
    assertEquals("application/json", result.getHeaders().get("Content-Type"));
    String content = result.getBody();
    assertNotNull(content);
    assertTrue(content.contains("\"names\""));
    assertTrue(content.contains("\"capital\""));
    assertTrue(content.contains("\"map\""));
    assertTrue(content.contains("\"flag\""));
    assertTrue(content.contains("\"area\""));
    assertTrue(content.contains("\"population\""));
    assertTrue(content.contains("\"currentWeather\""));
  }
}
