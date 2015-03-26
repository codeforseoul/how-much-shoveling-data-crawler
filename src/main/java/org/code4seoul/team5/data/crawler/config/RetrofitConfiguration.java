package org.code4seoul.team5.data.crawler.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.service.DaumService;
import org.code4seoul.team5.data.crawler.service.G2BService;
import org.code4seoul.team5.data.crawler.service.GoogleMapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.converter.SimpleXMLConverter;

/**
 * Created by papillon212 on 15. 3. 21..
 */
@Slf4j
@Configuration
public class RetrofitConfiguration {

    @Value("${g2b.rest.endpoint}")
    private String g2bRestEndpoint;

    @Value("${google.map.rest.endpoint}")
    private String googleMapRestEndpoint;

    @Value("${daum.rest.endpoint}")
    private String daumRestEndpoint;

    @Bean
    public RestAdapter restAdapterForG2B() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(g2bRestEndpoint)
//                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setConverter(new SimpleXMLConverter())
                .build();
        return adapter;
    }

    @Bean
    public G2BService g2BService() {
        return restAdapterForG2B().create(G2BService.class);
    }

    @Bean
    public RestAdapter restAdapterForGoogleMap() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(googleMapRestEndpoint)
//                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setConverter(new GsonConverter(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
                .build();
        return adapter;
    }

    @Bean
    public GoogleMapService googleMapService() {
        return restAdapterForGoogleMap().create(GoogleMapService.class);
    }

    @Bean
    public RestAdapter restAdapterForDaum() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(daumRestEndpoint)
//                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        return adapter;
    }

    @Bean
    public DaumService daumService() {
        return restAdapterForDaum().create(DaumService.class);
    }
}
