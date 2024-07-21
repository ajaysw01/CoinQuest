package com.ajay.service;

import com.ajay.model.CoinDTO;
import com.ajay.response.ApiResponse;

public interface ChatBotService {
    ApiResponse getCoinDetails(String coinName);

    CoinDTO getCoinByName(String coinName);

    String simpleChat(String prompt);
}
