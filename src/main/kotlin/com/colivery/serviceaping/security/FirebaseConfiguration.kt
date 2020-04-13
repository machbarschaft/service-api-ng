package com.colivery.serviceaping.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import javax.annotation.PostConstruct


@Configuration
class FirebaseConfiguration {

    @Value("\${firebase.database.url}")
    private val databaseUrl: String? = null

    @Value("\${firebase.project.id}")
    private val projectId: String? = null

    @PostConstruct
    fun init() {
        val options: FirebaseOptions = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl(databaseUrl)
                .setProjectId(projectId)
                .build()
        FirebaseApp.initializeApp(options)
    }
}