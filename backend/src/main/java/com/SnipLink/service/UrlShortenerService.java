package com.SnipLink.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.SnipLink.model.UrlResponse;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
public class UrlShortenerService {

	  	@Autowired
	    private RedisTemplate<String, String> redisTemplate; // Inject RedisTemplate for Redis operations

	    private static final long EXPIRATION_TIME = 7; // Set expiration time for URLs (in days)
 // In-memory storage for URLs
    public UrlResponse shortenUrl(String longUrl) {
        if (longUrl == null || longUrl.isEmpty()) {
            throw new IllegalArgumentException("Long URL cannot be null or empty");
        }

        // Check if URL already exists (idempotency)
        String existingKey = getKeyByValue(longUrl);
        if (existingKey != null) {
            return new UrlResponse(existingKey, longUrl, "http://snipurl/" + existingKey);
        }

        // Generate the short key
        String shortKey = generateShortKey(longUrl);

        // Ensure unique key by rehashing if necessary
        while (redisTemplate.hasKey(shortKey)) {
            shortKey = generateShortKey(longUrl + System.currentTimeMillis());
        }

        
        // Store the short key and long URL in Redis with an expiration time
        redisTemplate.opsForValue().set(shortKey, longUrl, EXPIRATION_TIME, TimeUnit.DAYS);

        // Return the response DTO
        String shortUrl = "http://localhost:8080/" + shortKey;
        return new UrlResponse(shortKey, longUrl, shortUrl);
    }

    // Generate a short key from the URL
    private String generateShortKey(String longUrl) {
    	
        return generateHash(longUrl).substring(0, 8);
    }
    
    private String generateHash(String longUrl) {
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = hash.digest(longUrl.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash!", e);
        }
    }


    // Retrieve the original URL by short key
    public String getOriginalUrl(String shortKey) {
    	return redisTemplate.opsForValue().get(shortKey); 
    }

    // Helper method to retrieve the key by long URL (for idempotency)
    private String getKeyByValue(String longUrl) {
    	  return redisTemplate.opsForValue().get("url:" + longUrl);
    }

    // Delete URL mapping
    public boolean deleteUrl(String shortKey) {
    	return redisTemplate.delete(shortKey); // Delete the key from Redis
    }
}
