package com.ffam.taskdistribution.handlers;

import java.security.InvalidParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ffam.taskdistribution.entities.TaskAssignments;
import com.ffam.taskdistribution.exception.ErrorHandler;
import com.ffam.taskdistribution.services.TaskAssignmentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TaskDistributionHandler {
    
    @Autowired
    TaskAssignmentService taskAssignmentService;
    
    @Autowired
    public ErrorHandler errorHandler;
    
    public Mono<ServerResponse> createTaskAssignment(final ServerRequest request) {
        return request.bodyToMono(TaskAssignments.class).switchIfEmpty(Mono.error(new InvalidParameterException("")))
                .flatMap(taskAssignment -> {
            return taskAssignmentService.saveTaskAssignment(taskAssignment).transform(this :: serverResponsMonoTaskAssignment);
        });
    }
    
    public Mono<ServerResponse> updateTaskAssignmentStatus(final ServerRequest request) {
        return serverResponseMonoString(taskAssignmentService.updateTaskStatus(Integer.parseInt(request.pathVariable("taskId")),
                request.pathVariable("status")));
    }
    
    public Mono<ServerResponse> getAllTasks(final ServerRequest request) {
        return serverResponsFluxTaskAssignment(taskAssignmentService.getAllTasks());
    }
    
    private Mono<ServerResponse> serverResponsMonoTaskAssignment(Mono<TaskAssignments> taskAssignments) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(taskAssignments,
                TaskAssignments.class).onErrorResume(errorHandler :: throwableError);
    }
    
    private Mono<ServerResponse> serverResponsFluxTaskAssignment(Flux<TaskAssignments>  taskAssignments) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(taskAssignments,
                TaskAssignments.class).onErrorResume(errorHandler :: throwableError);
    }
    
    protected Mono<ServerResponse> serverResponseMonoString(Mono<Integer> response) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(response, Integer.class)
                .onErrorResume(errorHandler::throwableError);
    }
    
}
