package com.ajay.controller;

import com.ajay.model.Coin;
import com.ajay.model.User;
import com.ajay.model.WatchList;
import com.ajay.service.CoinService;
import com.ajay.service.UserService;
import com.ajay.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {
    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.
                findUserProfileByJwt(jwt);

        WatchList watchList = watchListService.findUserWatchList(user.getId());

        return  ResponseEntity.ok(watchList);
    }


    @GetMapping("/{watchListId}")
    public ResponseEntity<WatchList> getWatchListById(
        @PathVariable Long watchListId
    ) throws Exception {
        WatchList watchList = watchListService.findById(watchListId);

        return  ResponseEntity.ok(watchList);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(coinId);
        Coin addedCoin = watchListService.addItemToWatchList(coin,user);
        return  ResponseEntity.ok(addedCoin);
    }
}
