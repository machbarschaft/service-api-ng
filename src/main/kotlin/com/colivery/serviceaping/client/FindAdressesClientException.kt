package com.colivery.serviceaping.client

class FindAdressesClientException(message: String, status: String) :
        Exception("Request failed with $status: $message")