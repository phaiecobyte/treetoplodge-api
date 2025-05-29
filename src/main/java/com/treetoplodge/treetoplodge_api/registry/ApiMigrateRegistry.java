package com.treetoplodge.treetoplodge_api.registry;


import com.treetoplodge.treetoplodge_api.Service.impl.OtpServiceImpl;
import com.treetoplodge.treetoplodge_api.caches.SmsCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiMigrateRegistry {
    private final OtpServiceImpl otpServiceImpl;

    public ApiMigrateRegistry(OtpServiceImpl otpServiceImpl) {
        this.otpServiceImpl = otpServiceImpl;
    }

    public void loadComponent() {
        log.info("Loading component ...");
        this.loadResponseCode();
    }

    private void loadResponseCode() {
        SmsCache.init();
        log.info("Init otp service ...");
        this.otpServiceImpl.init();
    }

}
