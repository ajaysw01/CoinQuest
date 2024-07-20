package com.ajay.service;

import com.ajay.model.Coin;
import com.ajay.model.User;
import com.ajay.model.WatchList;

public interface WatchListService {
    WatchList findUserWatchList(Long userId) throws Exception;

    WatchList createWatchList(User user);

    WatchList findById(Long id) throws Exception;

    Coin addItemToWatchList(Coin coin, User user) throws Exception;
}
