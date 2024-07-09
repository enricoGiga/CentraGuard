package com.security.gateway.helper;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

@Component
public class ResourceHelper {


    @Value("classpath:sql/security.sql")
    private Resource initSecurity;
    @Getter
    private String initSecurityQuery;

    @Value("classpath:sql/init.sql")
    private Resource init;
    @Getter
    private String initQuery;

    @PostConstruct
    private void postConstruct() {
        initSecurityQuery = ResourceHelper.resourceToString(initSecurity);
        initQuery = ResourceHelper.resourceToString(init);
    }

    private static String resourceToString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
