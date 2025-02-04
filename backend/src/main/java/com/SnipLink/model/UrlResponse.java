package com.SnipLink.model;

public class UrlResponse {

    private String key;
    private String longUrl;
    private String shortUrl;

    public UrlResponse(String key, String longUrl, String shortUrl) {
        this.key = key;
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
