package com.security.gateway.bootstrap;

import com.security.gateway.helper.ResourceHelper;
import com.security.gateway.dto.UserRoleDTO;
import com.security.gateway.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.persistence.EntityManager;
import java.util.List;


@Slf4j
//TODO: uncomment if you want init data, combined@ with ddl-auto different from none
//@Component
public class SecurityBootstrap implements CommandLineRunner, ApplicationListener<ContextRefreshedEvent> {


    private final UserRoleService userRoleService;

    private final TransactionTemplate transactionTemplate;
    private final EntityManager entityManager;
    private final ResourceHelper resourceHelper;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods()
                .forEach((requestMappingInfo, handlerMethod) -> {
                    System.out.println(requestMappingInfo);
                    System.out.println(handlerMethod);
                });
    }

    public SecurityBootstrap(
            UserRoleService userRoleService,
            TransactionTemplate transactionTemplate,
            EntityManager entityManager,
            ResourceHelper resourceHelper) {

        this.userRoleService = userRoleService;
        this.transactionTemplate = transactionTemplate;
        this.entityManager = entityManager;
        this.resourceHelper = resourceHelper;
    }


    @Override
    public void run(String... args)  {
        init();
        insertSecurity();


    }

    private void init() {

        transactionTemplate.execute(transactionStatus -> {
            entityManager.createNativeQuery(resourceHelper.getInitQuery()).executeUpdate();
            transactionStatus.flush();
            return null;
        });
    }

    private void insertSecurity() {

        UserRoleDTO role_admin = UserRoleDTO.builder().userRoleId(1)
                .role("ROLE_ADMIN").build();
        UserRoleDTO roleUser = UserRoleDTO.builder().userRoleId(2)
                .role("ROLE_USER").build();
        userRoleService.saveAll(List.of(role_admin, roleUser));

        transactionTemplate.execute(transactionStatus -> {
            entityManager.createNativeQuery(resourceHelper.getInitSecurityQuery()).executeUpdate();
            transactionStatus.flush();
            return null;
        });

    }

}
