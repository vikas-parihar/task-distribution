package com.ffam.taskdistribution.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ffam.taskdistribution.entities.Agent;
import com.ffam.taskdistribution.entities.TaskAssignments;
import com.ffam.taskdistribution.repository.AgentRepository;
import com.ffam.taskdistribution.repository.SkillRepository;
import com.ffam.taskdistribution.repository.TaskAssignmentRepository;

import reactor.core.publisher.Mono;

public class TaskAssignmentServiceTest {
    
    @Mock
    TaskAssignmentRepository taskAssignmentRepository;
    
    @Mock
    AgentRepository agentRepository;
    
    @Mock
    SkillRepository skillRepository;
    
    @InjectMocks
    TaskAssignmentService service;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testSaveTaskAssignment() {
        Integer [] skillIds = {1, 2, 3};
        TaskAssignments taskAssignments = mockTaskAssignments();
        Collection<String> names = Arrays.asList(taskAssignments.getSkillNames());
        when(skillRepository.getSkillIdsBySkillNames(names)).thenReturn(skillIds);
        List<Agent> agents = mockAgents();
        when(agentRepository.getAgentBySkillIds(skillIds)).thenReturn(agents);
        when(taskAssignmentRepository.save(Mockito.any())).thenReturn(taskAssignments);
        Mono<TaskAssignments> returnedAssignments = service.saveTaskAssignment(taskAssignments);
        assertNotNull(returnedAssignments);
        TaskAssignments returnedTaskAssignments = returnedAssignments.block();
        assertTrue(1 == returnedTaskAssignments.getAgentId());
    }
    
    @Test
    public void testSaveTaskAssignmentWithSkillNamesEmpty() {
        Integer [] skillIds = {1, 2, 3};
        String [] skillNames = {};
        TaskAssignments taskAssignments = mockTaskAssignments();
        taskAssignments.setSkillNames(skillNames);
        Collection<String> names = Arrays.asList(taskAssignments.getSkillNames());
        when(skillRepository.getSkillIdsBySkillNames(names)).thenReturn(skillIds);
        List<Agent> agents = mockAgents();
        when(agentRepository.getAgentBySkillIds(skillIds)).thenReturn(agents);
        when(taskAssignmentRepository.save(Mockito.any())).thenReturn(taskAssignments);
        Mono<TaskAssignments> returnedAssignments = service.saveTaskAssignment(taskAssignments);
        assertNotNull(returnedAssignments);
        try {
            TaskAssignments returnedTaskAssignments = returnedAssignments.block();
        } catch (Exception e) {
           assertTrue(e instanceof InvalidParameterException);
        }
    }
    
    @Test
    public void testSaveTaskAssignmentAgentsNotAvailable() {
        Integer [] skillIds = {1, 2, 3};
        TaskAssignments taskAssignments = mockTaskAssignments();
        Collection<String> names = Arrays.asList(taskAssignments.getSkillNames());
        when(skillRepository.getSkillIdsBySkillNames(names)).thenReturn(skillIds);
        List<Agent> agents = new ArrayList<>();
        when(agentRepository.getAgentBySkillIds(skillIds)).thenReturn(agents);
        when(taskAssignmentRepository.save(Mockito.any())).thenReturn(taskAssignments);
        Mono<TaskAssignments> returnedAssignments = service.saveTaskAssignment(taskAssignments);
        assertNotNull(returnedAssignments);
        try {
            TaskAssignments returnedTaskAssignments = returnedAssignments.block();
        } catch (Exception e) {
          assertNotNull(e);
        }
    }
    
    @Test
    public void testSaveTaskAssignmentWithAvailableAgentsWithTasks() {
        Integer [] skillIds = {1, 2, 3};
        TaskAssignments taskAssignments = mockTaskAssignments();
        Collection<String> names = Arrays.asList(taskAssignments.getSkillNames());
        when(skillRepository.getSkillIdsBySkillNames(names)).thenReturn(skillIds);
        List<Agent> agents = mockAgents();
        when(agentRepository.getAgentBySkillIds(skillIds)).thenReturn(agents);
        Collection<Integer> agentIds = new ArrayList<>(1);
        when(taskAssignmentRepository.getTasksBySkillIdAndAgent(skillIds, agentIds)).thenReturn(getMockTaskAssignmentWithAgents());
        when(taskAssignmentRepository.save(Mockito.any())).thenReturn(taskAssignments);
        Mono<TaskAssignments> returnedAssignments = service.saveTaskAssignment(taskAssignments);
        assertNotNull(returnedAssignments);
        TaskAssignments returnedTaskAssignments = returnedAssignments.block();
        assertTrue(1 == returnedTaskAssignments.getAgentId());
    }
    
    @Test
    public void testUpdateTaskStatus() {
        String taskStatus = "Completed";
        Integer taskId = 1;
        when(taskAssignmentRepository.updateTaskStatus(taskId, taskStatus)).thenReturn(0);
        Mono<Integer> returedValue = service.updateTaskStatus(taskId, taskStatus);
        Integer integer1 = returedValue.block();
        assertTrue ( integer1 == 0);
    }
    
    private TaskAssignments mockTaskAssignments () {
        TaskAssignments taskAssignments = new TaskAssignments();
        String [] skillNames = {"skill1", "skill2", "skill3"};
        taskAssignments.setSkillNames(skillNames);
        taskAssignments.setPriority("Low");
        taskAssignments.setTaskStatus("Not Started");
        return taskAssignments;
        
    }
    
    private List<Agent> mockAgents() {
        List<Agent> agents = new ArrayList<>();
        Integer [] skillIds = {1, 2, 3};
        Agent agent = new Agent();
        agent.setAgentId(1);
        agent.setAgentName("agent1");
        agent.setSkillIds(skillIds);
        agents.add(agent);
        return agents;
    }
    
    private LinkedList<TaskAssignments> getMockTaskAssignmentWithAgents() {
        LinkedList<TaskAssignments> taskAssignmentsList = new LinkedList<>();
        TaskAssignments taskAssignments = new TaskAssignments();
        String [] skillNames = {"skill1", "skill2", "skill3"};
        Integer [] skillIds = {1, 2, 3};
        taskAssignments.setSkillIds(skillIds);
        taskAssignments.setSkillNames(skillNames);
        taskAssignments.setPriority("High");
        taskAssignments.setTaskStatus("Not Started");
        taskAssignments.setAgentId(1);
        taskAssignmentsList.add(taskAssignments);
        return taskAssignmentsList;
    }

}
