package com.example.demo.controller.student;

import com.example.demo.Config.JwtService;
import com.example.demo.Config.ResponseDefinition;
import com.example.demo.controllers.student.CourseEnrolmentResponse;
import com.example.demo.controllers.student.StudentController;
import com.example.demo.controllers.student.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // You can initialize common objects here if needed.
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"STUDENT"})
    void testGetCoursesSuccess() throws Exception {
        // Mock expected courses
        List<CourseEnrolmentResponse> expectedCourses = Arrays.asList(
                new CourseEnrolmentResponse("Course 1", "Description 1", 100.0, "REF001"),
                new CourseEnrolmentResponse("Course 2", "Description 2", 200.0, "REF002")
        );

        ResponseDefinition<List<CourseEnrolmentResponse>> expectedResponse = ResponseDefinition.<List<CourseEnrolmentResponse>>builder()
                .success(true)
                .errorMessage(null)
                .data(expectedCourses)
                .build();

        // Mock service method
        Mockito.when(studentService.viewEnrolledCourses(anyInt(), any()))
                .thenReturn(expectedResponse);

        // Perform the GET request and validate the response
        mockMvc.perform(get("/getcourses/{id}", 1)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].courseName", is("Course 1")))
                .andExpect(jsonPath("$.data[1].courseName", is("Course 2")));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"STUDENT"})
    void testGetCoursesFailure() throws Exception {
        // Mock failure response
        ResponseDefinition<List<CourseEnrolmentResponse>> expectedResponse = ResponseDefinition.<List<CourseEnrolmentResponse>>builder()
                .success(false)
                .errorMessage("No courses found")
                .data(null)
                .build();

        // Mock service method
        Mockito.when(studentService.viewEnrolledCourses(anyInt(), any()))
                .thenReturn(expectedResponse);

        // Perform the GET request and validate the response
        mockMvc.perform(get("/getcourses/{id}", 1)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.errorMessage", is("No courses found")));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"STUDENT"})
    void testGetCoursesUnauthorized() throws Exception {
        // Simulate an unauthorized scenario
        Mockito.when(jwtService.extractUsername(any())).thenReturn("wrongUser");

        mockMvc.perform(get("/getcourses/{id}", 1)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
