package com.treetoplodge.treetoplodge_api.Service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.treetoplodge.treetoplodge_api.Service.OtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {
    @Value("${spring.otp.sms.expire}")
    protected String expireIn;
    @Value("${spring.otp.sms.template}")
    protected String smsTemplate;
    private LoadingCache<String, Integer> otpCache;

    @Override
    public void init() {
        this.otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(Long.parseLong(expireIn), TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    @Override
    public String generate(String key) {
        Random random = new Random();
        Integer otp = 100000 + random.nextInt(900000);
        this.otpCache.put(key, otp);
        return otp.toString();
    }

    @Override
    public String generate() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return Integer.toString(otp);
    }

    @Override
    public Integer get(String key) {
        try {
            return otpCache.get(key);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void clear(String key) {
        this.otpCache.invalidate(key);
    }

    @Override
    public boolean verify(int otp, String key) {
        if (otp < 0) {
            return false;
        }
        int serverOtp = this.get(key);
        if (serverOtp > 0) {
            if (otp == serverOtp) {
                clear(key);
                return true;
            }
        }
        return false;
    }
}
