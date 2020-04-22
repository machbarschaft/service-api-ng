package com.colivery.serviceaping.client

class EsriClientException(message: String, status: String) :
        Exception("Request failed with $status: $message")