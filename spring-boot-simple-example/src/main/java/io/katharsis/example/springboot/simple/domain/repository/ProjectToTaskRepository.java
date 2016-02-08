/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.katharsis.example.springboot.simple.domain.repository;

import io.katharsis.example.springboot.simple.domain.model.Project;
import io.katharsis.example.springboot.simple.domain.model.Task;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Component
public class ProjectToTaskRepository implements RelationshipRepository<Project, Long, Task, Long> {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectToTaskRepository(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void setRelation(Project project, Long taskId, String fieldName) {
        Task task = taskRepository.findOne(taskId, null);
        try {
            PropertyUtils.setProperty(project, fieldName, task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        projectRepository.save(project);
    }

    @Override
    public void setRelations(Project project, Iterable<Long> taskIds, String fieldName) {
        Iterable<Task> tasks = taskRepository.findAll(taskIds, null);
        try {
            PropertyUtils.setProperty(project, fieldName, tasks);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        projectRepository.save(project);
    }

    @Override
    public void addRelations(Project project, Iterable<Long> taskIds, String fieldName) {
        List<Task> newTaskList = new LinkedList<>();
        Iterable<Task> tasksToAdd = taskRepository.findAll(taskIds, null);
        tasksToAdd.forEach(newTaskList::add);
        try {
            if (PropertyUtils.getProperty(project, fieldName) != null) {
                Iterable<Task> projects = (Iterable<Task>) PropertyUtils.getProperty(project, fieldName);
                projects.forEach(newTaskList::add);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            PropertyUtils.setProperty(project, fieldName, newTaskList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        projectRepository.save(project);

    }

    @Override
    public void removeRelations(Project project, Iterable<Long> taskIds, String fieldName) {
        try {
            if (PropertyUtils.getProperty(project, fieldName) != null) {
                Iterable<Task> tasks = (Iterable<Task>) PropertyUtils.getProperty(project, fieldName);
                Iterator<Task> iterator = tasks.iterator();
                while (iterator.hasNext()) {
                    for (Long taskIdToRemove : taskIds) {
                        if (iterator.next().getId().equals(taskIdToRemove)) {
                            iterator.remove();
                            break;
                        }
                    }
                }
                List<Task> newTaskList = new LinkedList<>();
                tasks.forEach(newTaskList::add);

                PropertyUtils.setProperty(project, fieldName, newTaskList);
                projectRepository.save(project);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task findOneTarget(Long projectId, String fieldName, QueryParams requestParams) {
        Project project = projectRepository.findOne(projectId, requestParams);
        try {
            return (Task) PropertyUtils.getProperty(project, fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Task> findManyTargets(Long projectId, String fieldName, QueryParams requestParams) {
        Project project = projectRepository.findOne(projectId, requestParams);
        try {
            return (Iterable<Task>) PropertyUtils.getProperty(project, fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
