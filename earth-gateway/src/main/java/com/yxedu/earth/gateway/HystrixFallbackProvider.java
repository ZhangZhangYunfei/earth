package com.yxedu.earth.gateway;

import com.yxedu.earth.utils.json.JsonProviderHolder;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class HystrixFallbackProvider implements FallbackProvider {
  @Override
  public String getRoute() {
    return "*";
  }

  @Override
  public ClientHttpResponse fallbackResponse() {
    return new ClientHttpResponse() {
      @Override
      public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.OK;
      }

      @Override
      public int getRawStatusCode() throws IOException {
        return 200;
      }

      @Override
      public String getStatusText() throws IOException {
        return "OK";
      }

      @Override
      public void close() {
      }

      @Override
      public InputStream getBody() throws IOException {
        Map<String, String> map = new HashMap<>(3);
        map.put("status", "FAILED");
        map.put("code", "-1");
        map.put("message", "执行异常！");

        return new ByteArrayInputStream(
            JsonProviderHolder.JACKSON.toJsonString(map).getBytes());
      }

      @Override
      public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
      }
    };
  }

  @Override
  public ClientHttpResponse fallbackResponse(Throwable cause) {
    return fallbackResponse();
  }
}
