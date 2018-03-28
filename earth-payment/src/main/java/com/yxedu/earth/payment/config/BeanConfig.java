package com.yxedu.earth.payment.config;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

@Configuration
public class BeanConfig {
  /**
   * HttpClient.
   */
  @Bean
  public HttpClient httpClient(@Value("${http-client.max-total}") int maxTotal,
                               @Value("${http-client.max-per-route}") int maxPerRoute,
                               @Value("${http-client.timeout.connect}") int connectTimeout,
                               @Value("${http-client.timeout.request}") int requestTimeout,
                               @Value("${http-client.timeout.socket}") int socketTimeout,
                               @Value("${http-client.keep-alive.duration}") int keepAliveDuration) {
    try {
      SSLContext context = SSLContextBuilder
          .create()
          .useProtocol("TLS")
          .loadTrustMaterial(null, new TrustSelfSignedStrategy())
          .setSecureRandom(new SecureRandom()).build();

      Registry<ConnectionSocketFactory> registry = RegistryBuilder
          .<ConnectionSocketFactory>create()
          .register("http", PlainConnectionSocketFactory.getSocketFactory())
          .register("https", new SSLConnectionSocketFactory(context, new NoopHostnameVerifier()))
          .build();

      PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
      cm.setMaxTotal(maxTotal);
      cm.setDefaultMaxPerRoute(maxPerRoute);

      return HttpClients
          .custom()
          .setDefaultRequestConfig(RequestConfig
              .custom()
              .setConnectTimeout(connectTimeout)
              .setConnectionRequestTimeout(requestTimeout)
              .setSocketTimeout(socketTimeout)
              .build())
          .setConnectionManager(cm)
          .setKeepAliveStrategy(new CustomizedConnectionKeepAliveStrategy(keepAliveDuration))
          .build();
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException err) {
      throw new BeanCreationException("Error initializing the HTTP client", err);
    }
  }

  @Slf4j
  public static class CustomizedConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
    private static final String HEADER_TIMEOUT = "timeout";
    private static final int MILLI = 1000;

    private long keepAliveDuration;

    CustomizedConnectionKeepAliveStrategy(long keepAliveDuration) {
      this.keepAliveDuration = keepAliveDuration;
    }

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
      // Honor 'keep-alive' header
      HeaderElementIterator it = new BasicHeaderElementIterator(
          response.headerIterator(HTTP.CONN_KEEP_ALIVE));

      while (it.hasNext()) {
        HeaderElement he = it.nextElement();
        String param = he.getName();
        String value = he.getValue();

        if (value != null && param.equalsIgnoreCase(HEADER_TIMEOUT)) {
          try {
            return Long.parseLong(value) * MILLI;
          } catch (NumberFormatException err) {
            log.warn("Failed to parse the timeout value because of wrong format!", err);
          }
        }
      }

      return keepAliveDuration;
    }
  }
}
