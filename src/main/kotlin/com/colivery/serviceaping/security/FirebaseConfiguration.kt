package com.colivery.serviceaping.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.nio.file.Files
import java.nio.file.Paths
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfiguration {

    @Value("\${firebase.database.url}")
    private val databaseUrl: String? = null

    @Value("\${firebase.project.id}")
    private val projectId: String? = null

    @Value("\${firebase.credentials.path}")
    private val credentialsPath: String? = null

    @PostConstruct
    fun init() {
        val optionsBuilder = FirebaseOptions.Builder()
                .setDatabaseUrl(databaseUrl)
                .setProjectId(projectId)

        val options = if (this.credentialsPath !== null) {
            optionsBuilder.setCredentials(
                    GoogleCredentials.fromStream(
                            Files.newInputStream(Paths.get(this.credentialsPath))
                    )
            ).build()
        } else {
            optionsBuilder.setCredentials(GoogleCredentials.getApplicationDefault()).build()
        }

        FirebaseApp.initializeApp(options)
    }
}
