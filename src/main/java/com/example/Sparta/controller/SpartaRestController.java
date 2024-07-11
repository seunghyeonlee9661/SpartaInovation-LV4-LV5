package com.example.Sparta.controller;

import com.example.Sparta.dto.*;
import com.example.Sparta.entity.Comment;
import com.example.Sparta.enums.UserAuthority;
import com.example.Sparta.service.SpartaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public ResponseDTO createUser(@RequestBody @Valid UserRequestDTO requestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 발생한 경우
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(),errorMessage,null);
        }
        return spartaService.signup(requestDTO);
    }

    /*____________________강의__________________________*/

    /* 강의 목록 불러오기 */
    @GetMapping("/lectures")
    public ResponseDTO findLectures(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="category", defaultValue="") String category) {
        return spartaService.findLectures(page, category);
    }

    /* 강의 내용 불러오기 */
    @GetMapping("/lecture")
    public ResponseDTO findLecture(@RequestParam("id") int id) {
        return spartaService.findLecture(id);
    }

    /* 강의 추가 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @PostMapping("/lecture")
    public ResponseDTO createLecture(@RequestBody @Valid LectureRequestDTO lectureRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 발생한 경우
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(),errorMessage,null);
        }
        return spartaService.createLecture(lectureRequestDTO);
    }

    /* 강의 삭제 */
    @DeleteMapping("/lecture")
    public ResponseDTO removeLecture(@RequestParam("id") int id) {
        return spartaService.removeLecture(id);
    }

    /* 강의 수정 */
    @PutMapping("/lecture")
    public ResponseDTO updateLecture(@RequestBody @Valid LectureRequestDTO lectureRequestDTO, @RequestParam("id") int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 발생한 경우
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(),errorMessage,null);
        }
        return spartaService.updateLecture(id, lectureRequestDTO);
    }

    /*____________________강사__________________________*/

    /* 강사 목록 불러오기 */
    @GetMapping("/teachers")
    public ResponseDTO findTeachers() {
        return spartaService.findTeachers();
    }

    /* 강사 정보 불러오기 */
    @GetMapping("/teacher")
    public ResponseDTO findTeacher(@RequestParam("id") int id) {
        return spartaService.findTeacher(id);
    }

    /* 강사 추가 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PostMapping("/teacher")
    public ResponseDTO createTeacher(@RequestBody @Valid TeacherRequestDTO teacherRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 발생한 경우
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(),errorMessage,null);
        }
        return spartaService.createLecture(teacherRequestDTO);
    }

    /* 강사 삭제 */
    @DeleteMapping("/teacher")
    public ResponseDTO removeTeacher(@RequestParam("id") int id) {
        return spartaService.removeTeacher(id);
    }

    /* 강사 수정 */
    @PutMapping("/teacher")
    public ResponseDTO updateTeacher(@RequestBody @Valid TeacherRequestDTO teacherRequestDTO, @RequestParam("id") int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 발생한 경우
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(),errorMessage,null);
        }
        return spartaService.updateTeacher(id, teacherRequestDTO);
    }

    /*____________________댓글__________________________*/

    /* 강의 댓글 불러오기 */
    @GetMapping("/comment")
    public List<CommentResponseDTO> findComments(@RequestParam("id") int id) {
        return spartaService.findComments(id).stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }

    /* 댓글 추가 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PostMapping("/comment")
    public ResponseDTO createComment(@RequestBody @Valid CommentRequestDTO commentRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 오류가 발생한 경우
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(),errorMessage,null);
        }
        return spartaService.createComment(commentRequestDTO);
    }

    /* 강사 삭제 */
    @DeleteMapping("/comment")
    public ResponseEntity<String> removeComment(@RequestParam("id") int id) {
        return spartaService.removeComment(id);
    }

    /* 강사 수정 */
    @PutMapping("/comment")
    public ResponseEntity<String> updateComment(@RequestBody CommentRequestDTO commentRequestDTO, @RequestParam("id") int id) {
        return spartaService.updateComment(id, commentRequestDTO);
    }

}
