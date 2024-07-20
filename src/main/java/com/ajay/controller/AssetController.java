package com.ajay.controller;

import com.ajay.model.Asset;
import com.ajay.model.User;
import com.ajay.service.AssetService;
import com.ajay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
public class AssetController {
    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(
            @PathVariable Long assetId
    ) throws Exception {
       Asset asset = assetService.getAssetById(assetId);
       return ResponseEntity.ok().body(asset);
    }


    @GetMapping("/coin/{coinId}/user")
    public  ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @PathVariable String coinId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(),coinId);
        return  ResponseEntity.ok().body(asset);
    }


    @GetMapping()
    public ResponseEntity<List<Asset>> getAllAssets(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Asset> assets = assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok().body(assets);
    }





}
