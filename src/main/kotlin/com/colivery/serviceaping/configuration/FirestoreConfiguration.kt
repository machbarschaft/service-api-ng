package com.colivery.serviceaping.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirestoreConfiguration {

    @Bean
    fun firestore(firebaseApp: FirebaseApp) =
            FirestoreClient.getFirestore(firebaseApp)

    @Bean
    fun firebaseApp(options: FirebaseOptions) =
            FirebaseApp.initializeApp(options)

    @Bean
    fun firebaseOptions(configuration: FirebaseProperties): FirebaseOptions {
        val keyLocation = configuration.keyLocation
        val stream = if (keyLocation == null || keyLocation == "") {
            null
        } else if (keyLocation.startsWith("classpath:")) {
            javaClass.getResourceAsStream(keyLocation.substringAfter("classpath:"))
        } else {
            FileInputStream(keyLocation)
        }
        val options = FirebaseOptions.Builder()
                .setProjectId(configuration.projectId)
                .setDatabaseUrl(configuration.apiUrl)

        if (stream != null) {
            options.setCredentials(GoogleCredentials.fromStream(stream))
        } else {
            options.setCredentials(GoogleCredentials.getApplicationDefault())
        }

        return options.build()
    }

}
