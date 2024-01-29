package com.example.hirdaramani;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class HirdaramaniApplication {

    public static void main(String[] args) {
        SpringApplication.run(HirdaramaniApplication.class, args);
    }

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    public Bucket defaultBucket(@Value("hidaramani-406eb.appspot.com") String storageBucket) throws IOException {
        InputStream serviceAccount = new ClassPathResource("/firebase.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket)
                .build();

        if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options);
        return StorageClient.getInstance().bucket();
    }

    @Bean
    public SecretKey secretKey(){
        return Jwts.SIG.HS256.key().build();
    }

}
