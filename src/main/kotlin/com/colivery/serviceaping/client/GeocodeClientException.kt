package com.colivery.serviceaping.client

class GeocodeClientException(message: String, status: String) :
        Exception("Request failed with $status: $message")