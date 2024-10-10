package com.example.demo.controllers.student;

import com.example.demo.Config.ResponseDefinition;
import com.example.demo.models.StudentsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDefinition<StudentsModel>> updateStudent(@PathVariable("id") int id, @RequestHeader("Authorization") String authToken, @RequestBody StudentRequest studentRequest) {
        ResponseDefinition<StudentsModel> response = studentService.updateStudent(id,authToken,studentRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/viewprofile/{id}")
    public ResponseEntity<ResponseDefinition<StudentsModel>> getStudent(@PathVariable("id") int id, @RequestHeader("Authorization") String authToken) {
        ResponseDefinition<StudentsModel> response = studentService.viewStudentByID(id, authToken);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/getcourses/{id}")
    public ResponseEntity<ResponseDefinition<List<CourseEnrolmentResponse>>> getCourses(@PathVariable int id,
                                                                                        @RequestHeader("Authorization") String authToken) {
        ResponseDefinition<List<CourseEnrolmentResponse>> response = studentService.viewEnrolledCourses(id,authToken);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/graduation/{id}")
    public ResponseEntity<GraduationResponse> graduate(@PathVariable int id, @RequestHeader("Authorization") String authToken) {
       GraduationResponse response = studentService.checkGraduateStudent(id,authToken);
        return ResponseEntity.ok(response);
    }

}
