package com.colivery.serviceaping.business.mail

import com.colivery.serviceaping.configuration.MailjetConfig
import com.mailjet.client.MailjetClient
import com.mailjet.client.MailjetRequest
import com.mailjet.client.errors.MailjetException
import com.mailjet.client.errors.MailjetSocketTimeoutException
import com.mailjet.client.resource.Emailv31
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.scheduling.annotation.Async

open class MailService(
    private val mailjetClient: MailjetClient,
    private val mailjetConfig: MailjetConfig
) {

    private fun getSenderObject(): JSONObject {
        return JSONObject()
            .put("Email", mailjetConfig.senderAddress)
            .put("Name", mailjetConfig.senderName)
    }

    private fun getRecipientObject(mail: Mail): JSONArray {
        return JSONArray()
            .put(
                JSONObject()
                    .put("Email", mail.recipient)
            )
    }

    @Throws(MailjetSocketTimeoutException::class, MailjetException::class)
    @Async
    open fun sendEmail(mail: Mail) {
        val sender: JSONObject = getSenderObject()
        val recipient: JSONArray = getRecipientObject(mail)
        val mailjetRequest = MailjetRequest(Emailv31.resource)
            .property(
                Emailv31.MESSAGES,
                JSONArray().put(
                    JSONObject()
                        .put(Emailv31.Message.FROM, sender)
                        .put(Emailv31.Message.TO, recipient)
                        .put(Emailv31.Message.SUBJECT, mail.subject)
                        .put(Emailv31.Message.HTMLPART, mail.htmlBody)
                )
            )
        val mailjetResponse = mailjetClient.post(mailjetRequest)
        // if the response status code is not one of the 200 codes (200, 201, ...), we
        // experienced an error on the API side
        if (mailjetResponse.status / 200 != 1) {
            throw MailjetException(
                "Error Status from Mailjet-API: ${mailjetResponse.status} - Details: ${mailjetResponse.data}"
            )
        }
    }

    private fun buildEmail(subject: String, recipient: String, template: String): Mail {
        return Mail(recipient, subject, template)
    }

}
