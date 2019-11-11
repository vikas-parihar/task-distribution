package com.ffam.taskdistribution.services;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ffam.taskdistribution.entities.Agent;
import com.ffam.taskdistribution.entities.TaskAssignments;
import com.ffam.taskdistribution.entities.TaskStatus;
import com.ffam.taskdistribution.repository.AgentRepository;
import com.ffam.taskdistribution.repository.SkillRepository;
import com.ffam.taskdistribution.repository.TaskAssignmentRepository;

import javassist.NotFoundException;
import reactor.core.publisher.Mono;

@Service
public class TaskAssignmentService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskAssignmentService.class);

    @Autowired
    TaskAssignmentRepository taskAssignmentRepository;
    
    @Autowired 
    AgentRepository agentRepository;
    
    @Autowired
    SkillRepository skillRepository;
    
    public Mono<TaskAssignments> saveTaskAssignment(TaskAssignments taskAssignments) {
        LOGGER.info("entering save taskassignment for skills {}",taskAssignments.getSkillNames().toString());
        Collection<String> names = Arrays.asList(taskAssignments.getSkillNames());
        if (CollectionUtils.isEmpty(names)) {
            return Mono.error(new InvalidParameterException("skill names are empty"));
        }
        Integer [] skillIds = skillRepository.getSkillIdsBySkillNames(names);
        List<Agent> agentsHavingSkills = agentRepository.getAgentBySkillIds(skillIds);
        Integer agentId = null;
        if(CollectionUtils.isEmpty(agentsHavingSkills)) {
            return Mono.error(new NotFoundException("agent not found with required skills"));
        }
        Collection<Integer> agentIds = agentsHavingSkills.stream().map(Agent::getAgentId).collect(Collectors.toList());
        LinkedList<TaskAssignments> tasksByAgents = taskAssignmentRepository.getTasksBySkillIdAndAgent(skillIds, agentIds);
        List<Integer> assignedAgentIds = tasksByAgents.stream().map(TaskAssignments::getAgentId).collect(Collectors.toList());
        //finding out available agent to allocate the task
        agentIds.removeAll(assignedAgentIds);
        
        if(!CollectionUtils.isEmpty(agentIds)) {
            agentId = agentIds.stream().findAny().get();
        }
        boolean isAgentAvailble = false;
        // No available agent is free to pickup the task finding out agent having low priority task
        if (agentId == null) {
            for (Agent agent : agentsHavingSkills) {
                isAgentAvailble = isAgentAvailableToPickTaks(tasksByAgents, taskAssignments.getPriority(), agent.getAgentId());
                if(isAgentAvailble) {
                    agentId = agent.getAgentId();
                    break;
                }
            }
            
            //All agents are busy performing the tasks so returning the error. 
            if (!isAgentAvailble) {
                return Mono.error(new NotFoundException("agent not available with required skills"));
            }
        }
        //Agent is allocated to the task saving to database. 
        taskAssignments.setAgentId(agentId);
        taskAssignments.setStartDate(LocalDateTime.now());
        taskAssignments.setSkillIds(skillIds);
        return Mono.just(taskAssignmentRepository.save(taskAssignments));
        
    }
    
    public Mono<Integer> updateTaskStatus(Integer taskId, String taskStatus) {
        LOGGER.info("updating the task status for taskId{}",taskId);
        TaskStatus status = TaskStatus.fromValue(taskStatus);
        LocalDateTime completionDate = LocalDateTime.now();
        return Mono.just(taskAssignmentRepository.updateTaskStatus(taskId, status.value(), completionDate));
    }
    
    
    private boolean isAgentAvailableToPickTaks(List<TaskAssignments> agentTasks, String taskPriority, Integer agentId) {
        boolean isAgentAvailableToPick = false; 
        for(TaskAssignments taskAssignment: agentTasks) {
            if (taskAssignment.getAgentId().equals(agentId)) {
                if ("low".equalsIgnoreCase(taskAssignment.getPriority()) && "high".equalsIgnoreCase(taskPriority)){
                    LOGGER.info("agent is performing low priorty task returning him as available {}", agentId);
                    return true;
                } else if (taskPriority.equalsIgnoreCase(taskAssignment.getPriority())) {
                    LOGGER.info("agent is performing same priorty task returning him as not available {}", agentId);
                   return false;
                }
            }
        }
        return isAgentAvailableToPick;
    }
}
