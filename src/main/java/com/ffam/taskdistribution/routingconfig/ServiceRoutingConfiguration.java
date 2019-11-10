package com.ffam.taskdistribution.routingconfig;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebExceptionHandler;

import com.ffam.taskdistribution.exception.CustomWebExceptionHandler;
import com.ffam.taskdistribution.handlers.TaskDistributionHandler;

@Configuration
@EnableWebFlux
public class ServiceRoutingConfiguration {

    @Autowired
    protected ApplicationContext appContext;
    
    static final String CONTEXT_PATH = "/distribution-service/";
    static final String V1_DISTRIBUTION_URL = CONTEXT_PATH + "v1/tasks";
    static final String V1_TASK_URL = CONTEXT_PATH + "v1/tasks/{taskId}/status/{status}";

    @Bean
    @Order(-2)
    WebExceptionHandler webExceptionHandler() {
        return new CustomWebExceptionHandler();
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods(new String[]{"GET", "POST", "DELETE", "PUT", "HEAD"});
    }

    @Bean
    public RouterFunction<ServerResponse> orgRoutes() {
        TaskDistributionHandler serviceHandler = appContext.getBean(TaskDistributionHandler.class);
        return route(POST(V1_DISTRIBUTION_URL), serviceHandler::createTaskAssignment)
                .andRoute(PUT(V1_TASK_URL), serviceHandler::updateTaskAssignmentStatus);
    }
}