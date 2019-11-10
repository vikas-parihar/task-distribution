package com.ffam.taskdistribution.services;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    TaskAssignmentRepository taskAssignmentRepository;
    
    @Autowired 
    AgentRepository agentRepository;
    
    @Autowired
    SkillRepository skillRepository;
    
    public Mono<TaskAssignments> saveTaskAssignment(TaskAssignments taskAssignments) {
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
        agentIds.removeAll(assignedAgentIds);
        
        if(!CollectionUtils.isEmpty(agentIds)) {
            agentId = agentIds.stream().findAny().get();
        }
        boolean isAgentAvailble = false;
        if (agentId == null) {
            for (Agent agent : agentsHavingSkills) {
                isAgentAvailble = isAgentAvailableToPickTaks(tasksByAgents, taskAssignments.getPriority(), agent.getAgentId());
                if(isAgentAvailble) {
                    agentId = agent.getAgentId();
                    break;
                }
            }
            if (!isAgentAvailble) {
                return Mono.error(new NotFoundException("agent not available with required skills"));
            }
        }
        taskAssignments.setAgentId(agentId);
        taskAssignments.setStartDate(LocalDateTime.now());
        taskAssignments.setSkillIds(skillIds);
        return Mono.just(taskAssignmentRepository.save(taskAssignments));
        
    }
    
    public Mono<Integer> updateTaskStatus(Integer taskId, String taskStatus) {
        TaskStatus status = TaskStatus.fromValue(taskStatus);
        return Mono.just(taskAssignmentRepository.updateTaskStatus(taskId, status.value()));
    }
    
    public TaskAssignments geTaskAssignments(Integer taskId) {
        return taskAssignmentRepository.getTaskAssignmentById(taskId);
        
    }
    
    private boolean isAgentAvailableToPickTaks(List<TaskAssignments> agentTasks, String taskPriority, Integer agentId) {
        boolean isAgentAvailableToPick = false; 
        for(TaskAssignments taskAssignment: agentTasks) {
            if (taskAssignment.getAgentId().equals(agentId)) {
                if ("low".equalsIgnoreCase(taskAssignment.getPriority()) && "high".equalsIgnoreCase(taskPriority)){
                    return true;
                } else if (taskPriority.equalsIgnoreCase(taskAssignment.getPriority())) {
                   return false;
                }
            }
        }
        return isAgentAvailableToPick;
    }
}
