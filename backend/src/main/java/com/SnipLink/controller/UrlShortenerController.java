package com.SnipLink.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.SnipLink.model.UrlRequest;
import com.SnipLink.model.UrlResponse;
import com.SnipLink.service.UrlShortenerService;

import jakarta.validation.Valid;

@RestController
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    // POST request to shorten the URL
    @CrossOrigin(origins = "http://localhost:8081")  
    @PostMapping("/api/v1/")
    public ResponseEntity<UrlResponse> shortenUrl(@Valid @RequestBody UrlRequest urlRequest) {
        String longUrl = urlRequest.getUrl();
        UrlResponse response = urlShortenerService.shortenUrl(longUrl);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET request to handle redirect to long URL
    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirectUrl(@PathVariable String shortKey) {
        String longUrl = urlShortenerService.getOriginalUrl(shortKey);
        if (longUrl != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", longUrl)
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE request to remove the URL mapping
    @DeleteMapping("/{shortKey}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortKey) {
        boolean isDeleted = urlShortenerService.deleteUrl(shortKey);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
   
}