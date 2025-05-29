package com.treetoplodge.treetoplodge_api.config;

import com.treetoplodge.treetoplodge_api.registry.ApiMigrateRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final ApiMigrateRegistry apiMigrateRegistry;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Initializing application components...");
        apiMigrateRegistry.loadComponent();
        log.info("Application components initialized successfully");
    }
}