package com.example.Sparta.controller;

import com.example.Sparta.dto.*;
import com.example.Sparta.entity.User;
import com.example.Sparta.service.SpartaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SpartaRestController {

    private final SpartaService spartaService;

    /* 사용자 추가 */
    @PostMapping("/user/signup")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO requestDTO) {
        return spartaService.signup(requestDTO);
    }

//    /* 로그인 */
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
//        return spartaService.login(loginRequestDTO,response);
//    }
//
//    /* 사용자 정보 불러오기 */
//    @GetMapping("/user")
//    public UserResponseDTO findUser(HttpServletRequest req) {
//        return new UserResponseDTO((User) req.getAttribute("user"));
//    }
//
//    /*____________________강의__________________________*/
//
//    /* 강의 목록 불러오기 */
//    @GetMapping("/lectures")
//    public Page<LectureResponseDTO> findLectures(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="category", defaultValue="") String category) {
//        return spartaService.findLectures(page, category);
//    }
//
//    /* 강의 내용 불러오기 */
//    @GetMapping("/lecture")
//    public LectureResponseDTO findLecture(@RequestParam("id") int id) {
//        return new LectureResponseDTO(spartaService.findLecture(id));
//    }
//
//    /* 강의 추가 */
//    @PostMapping("/lecture")
//    public ResponseEntity<String> createLecture(@RequestBody LectureRequestDTO lectureRequestDTO) {
//        return spartaService.createLecture(lectureRequestDTO);
//    }
//
//    /* 강의 삭제 */
//    @DeleteMapping("/lecture")
//    public ResponseEntity<String> removeLecture(@RequestParam("id") int id) {
//        return spartaService.removeLecture(id);
//    }
//
//    /* 강의 수정 */
//    @PutMapping("/lecture")
//    public ResponseEntity<String> updateLecture(@RequestBody LectureRequestDTO lectureRequestDTO, @RequestParam("id") int id) {
//        return spartaService.updateLecture(id, lectureRequestDTO);
//    }
//    /*____________________강사__________________________*/
//    /* 강사 목록 불러오기 */
//    @GetMapping("/teachers")
//    public List<TeacherResponseDTO> findTeachers() {
//        return spartaService.findTeachers().stream().map(TeacherResponseDTO::new).collect(Collectors.toList());
//    }
//
//    /* 강사 목록 불러오기 */
//    @GetMapping("/teacher")
//    public TeacherResponseDTO findTeacher(@RequestParam("id") int id) {
//        return new TeacherResponseDTO(spartaService.findTeacher(id));
//    }
//
//    /* 강사 추가 */
//    @PostMapping("/teacher")
//    public ResponseEntity<String> createTeacher(@RequestBody TeacherRequestDTO teacherRequestDTO) {
//        return spartaService.createLecture(teacherRequestDTO);
//    }
//
//    /* 강사 삭제 */
//    @DeleteMapping("/teacher")
//    public ResponseEntity<String> removeTeacher(@RequestParam("id") int id) {
//        return spartaService.removeTeacher(id);
//    }
//
//    /* 강사 수정 */
//    @PutMapping("/teacher")
//    public ResponseEntity<String> updateTeacher(@RequestBody TeacherRequestDTO teacherRequestDTO, @RequestParam("id") int id) {
//        return spartaService.updateTeacher(id, teacherRequestDTO);
//    }

}
