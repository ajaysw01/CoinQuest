package com.ajay.service;

import com.ajay.model.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinList(int page) throws Exception;

    String getMarketChart(String coinId, int days) throws Exception;

    //for this gecko api is used
    String getCoinDetails(String coinId) throws Exception;

    // this for db
    Coin findById(String coinId) throws Exception;

    String searchCoin(String keyword) throws Exception;

    String getTop50CoinByMarketCapRank() throws Exception;

    String getTradingCoins() throws Exception;
}
