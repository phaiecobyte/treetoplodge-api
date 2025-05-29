package com.treetoplodge.treetoplodge_api.Service;

public interface OtpService {
    void init();
    String generate(String key);
    String generate();
    Integer get(String key);
    void clear(String key);
    boolean verify(int opt, String key);
}
