package sample;

import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;


@SpringBootApplication
public class LogbookESTestApp {

    @Bean
    public RestClientBuilderCustomizer esCustomizer(
            LogbookHttpRequestInterceptor httpRequestInterceptor,
            LogbookHttpResponseInterceptor httpResponseInterceptor
    ) {
        return new RestClientBuilderCustomizer() {
            @Override
            public void customize(final RestClientBuilder builder) {
                builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(final HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder
                                .addInterceptorFirst(httpRequestInterceptor)
                                .addInterceptorLast(httpResponseInterceptor);
                    }
                });
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(LogbookESTestApp.class, args);
    }
}
