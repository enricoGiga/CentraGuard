
# Zuul Security Gateway Project

## Overview

This project aims to implement Zuul as a central point for security in our microservices architecture. Zuul is a powerful API gateway that provides dynamic routing, monitoring, resiliency, security, and more. By using Zuul, we can manage and secure our microservices in a centralized manner, ensuring that all requests pass through a single gateway for authentication, authorization, and logging.

## Key Features

- **Centralized Security**: Zuul acts as the gateway for all incoming requests, providing a single entry point for authentication and authorization.
- **Dynamic Routing**: Zuul dynamically routes requests to the appropriate microservice based on the configuration.
- **Monitoring and Resiliency**: Built-in monitoring and resilience features to handle service failures gracefully.
- **Logging and Auditing**: Centralized logging and auditing of all requests passing through the gateway.

## Technologies Used

- **Zuul**: API Gateway by Netflix.
- **Spring Boot**: To build the microservices.
- **Spring Security**: For implementing security mechanisms.

## Getting Started

### Installation

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-repository/zuul-security-gateway.git
    cd zuul-security-gateway
    ```

2. **Build the project**:
    ```bash
    ./gradlew clean build
    ```

3. **Run the Zuul server**:
    ```bash
    ./gradlew bootRun
    ```

### Configuration

Zuul configurations can be found in the `application.yml` file. Here, you can set up routes, security configurations, and other necessary settings.

```yaml
zuul:
  routes:
    service1:
      path: /service1/**
      url: http://localhost:8081
    service2:
      path: /service2/**
      url: http://localhost:8082


```

### Securing Routes

Security configurations can be defined in `SecurityConfig.java`. Use Spring Security to secure your routes and handle authentication and authorization.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/service1/**").authenticated()
                .antMatchers("/service2/**").authenticated()
                .and()
            .oauth2Login();
    }
}
```

## Usage

Once the Zuul gateway is running, all requests to your microservices should be routed through the gateway. For example, a request to `http://localhost:8080/service1/endpoint` will be routed to `http://localhost:8081/endpoint` if `service1` is configured as shown above.
Note: All your microservices should be configured to listen on internal or private IP addresses that are not accessible from outside your network.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for review.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
