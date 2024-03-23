package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.repositories.EmployeeRepository;
import com.onedayoffer.taskdistribution.repositories.TaskRepository;
import com.onedayoffer.taskdistribution.repositories.entities.Employee;
import com.onedayoffer.taskdistribution.repositories.entities.Task;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public List<EmployeeDTO> getEmployees(@Nullable String sortDirection) {
        if (Optional.ofNullable(sortDirection).isPresent()) {
            return employeeRepository
                    .findAll(Sort.by(Sort.Direction.valueOf(sortDirection), "fio")).stream()
                    .map(this::convertToDto).collect(Collectors.toList());
        } else {
            return employeeRepository.findAll().stream()
                    .map(this::convertToDto).collect(Collectors.toList());
        }
        // if sortDirection.isPresent() ..
        // Sort.Direction direction = ...
        // employees = employeeRepository.findAllAndSort(Sort.by(direction, "fio"))
        // employees = employeeRepository.findAll()
        // Type listType = new TypeToken<List<EmployeeDTO>>() {}.getType()
        // List<EmployeeDTO> employeeDTOS = modelMapper.map(employees, listType)
    }

    @Transactional
    public EmployeeDTO getOneEmployee(Integer id) {
        return convertToDto(employeeRepository.getReferenceById(id));
    }

    public List<TaskDTO> getTasksByEmployeeId(Integer id) {
        Employee employee = employeeRepository.getReferenceById(id);
        return employee.getTasks().stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public void changeTaskStatus(Integer taskId, TaskStatus status) {
        throw new java.lang.UnsupportedOperationException("implement changeTaskStatus");
    }

    @Transactional
    public void postNewTask(Integer employeeId, TaskDTO newTask) {
        throw new java.lang.UnsupportedOperationException("implement postNewTask");
    }

    private EmployeeDTO convertToDto(Employee employee) {
        EmployeeDTO postDto = modelMapper.map(employee, EmployeeDTO.class);
//        TypeMap<Employee, EmployeeDTO> propertyMapper = modelMapper.createTypeMap(Employee.class, EmployeeDTO.class);
//        propertyMapper.addMappings(
//                mapper -> mapper.map(e -> e)
//        );
        return postDto;
    }

    private TaskDTO convertToDto(Task task) {
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        return taskDTO;
    }
}
