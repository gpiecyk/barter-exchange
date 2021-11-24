package com.gp.barter.exchange.util;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gp.barter.exchange.persistence.model.UserData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Map;

public class FirebaseUtil {

    private static final String NOTIFICATIONS = "notifications/";

    private FirebaseUtil() {}

    public static void saveData(String path, Map<String, Object> data) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
            ref.setValue(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUserAccount(UserData user) {
        try {
            String path = NOTIFICATIONS + user.getId();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
            ref.removeValue(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initFirebase() {
        try {
            Resource resource = new ClassPathResource("Barter Exchange-f7c25de25c09.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://your-firebase-url.firebaseio.com") // TODO setup your firebase database
                    .setServiceAccount(resource.getInputStream())
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
