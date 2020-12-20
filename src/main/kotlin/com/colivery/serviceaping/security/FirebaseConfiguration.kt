package com.colivery.serviceaping.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Files
import java.nio.file.Paths

@Configuration
class FirebaseConfiguration {

    @Value("\${firebase.database.url}")
    private val databaseUrl: String? = null

    @Value("\${firebase.project.id}")
    private val projectId: String? = null

    @Value("\${firebase.credentials.path}")
    private val credentialsPath: String? = null

    @Bean
    fun firebaseApp(): FirebaseApp {
        val optionsBuilder = FirebaseOptions.Builder()
                .setDatabaseUrl(databaseUrl)
                .setProjectId(projectId)

        val options = if (this.credentialsPath !== null && this.credentialsPath != "") {
            optionsBuilder.setCredentials(
                    GoogleCredentials.fromStream(
                            Files.newInputStream(Paths.get(this.credentialsPath))
                    )
            ).build()
        } else {
            optionsBuilder.setCredentials(GoogleCredentials.getApplicationDefault()).build()
        }

        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth =
            FirebaseAuth.getInstance(firebaseApp)
}
