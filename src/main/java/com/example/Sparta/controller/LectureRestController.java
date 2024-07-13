package com.example.Sparta.controller;

import com.example.Sparta.dto.*;
import com.example.Sparta.enums.UserAuthority;
import com.example.Sparta.security.UserDetailsImpl;
import com.example.Sparta.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LectureRestController {

    private final LectureService lectureService;

    /* 사용자 추가 */
    @PostMapping("/user/signup")
    public ResponseDTO createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        return lectureService.createUser(requestDTO);
    }

    /* 사용자 추가 */
    @PostMapping("/user/withdraw")
    public ResponseDTO removeUser(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String password = request.get("password");
        if(password == null || password.isEmpty())
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 비밀번호 입니다.", null);
        return lectureService.removeUser(userDetails,password);
    }


    /*____________________강의__________________________*/

    /* 강의 목록 불러오기 */
    @GetMapping("/lectures")
    public ResponseDTO findLectures(@RequestParam(value="page", defaultValue="0") int page,
                                    @RequestParam(value="category", defaultValue="") String category,
                                    @RequestParam(value="option", defaultValue="") String option,
                                    @RequestParam(value="desc", defaultValue="") boolean desc) {
        return lectureService.findLectures(page, category,option,desc);
    }

    /* 강의 내용 불러오기 */
    @GetMapping("/lecture")
    public ResponseDTO findLecture(@RequestParam("id") int id) {
        return lectureService.findLecture(id);
    }

    /* 강의 추가 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @PostMapping("/lecture")
    public ResponseDTO createLecture(@RequestBody @Valid LectureRequestDTO lectureRequestDTO) {

        return lectureService.createLecture(lectureRequestDTO);
    }

    /* 강의 삭제 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @DeleteMapping("/lecture")
    public ResponseDTO removeLecture(@RequestParam("id") int id) {
        return lectureService.removeLecture(id);
    }

    /* 강의 수정 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @PutMapping("/lecture")
    public ResponseDTO updateLecture(@RequestBody @Valid LectureRequestDTO lectureRequestDTO, @RequestParam("id") int id) {
        return lectureService.updateLecture(id, lectureRequestDTO);
    }

    /*____________________강사__________________________*/

    /* 강사 목록 불러오기 */
    @GetMapping("/teachers")
    public ResponseDTO findTeachers() {
        return lectureService.findTeachers();
    }

    /* 강사 정보 불러오기 */
    @GetMapping("/teacher")
    public ResponseDTO findTeacher(@RequestParam("id") int id) {
        return lectureService.findTeacher(id);
    }

    /* 강사 추가 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PostMapping("/teacher")
    public ResponseDTO createTeacher(@Valid @RequestBody TeacherRequestDTO teacherRequestDTO) {
        return lectureService.createLecture(teacherRequestDTO);
    }

    /* 강사 삭제 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @DeleteMapping("/teacher")
    public ResponseDTO removeTeacher(@RequestParam("id") int id) {
        return lectureService.removeTeacher(id);
    }

    /* 강사 수정 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @PutMapping("/teacher")
    public ResponseDTO updateTeacher(@RequestBody @Valid TeacherRequestDTO teacherRequestDTO, @RequestParam("id") int id) {
        return lectureService.updateTeacher(id, teacherRequestDTO);
    }

    /*____________________댓글__________________________*/

    /* 댓글 불러오기 */
    @GetMapping("/comment")
    public ResponseDTO findComments(@RequestParam("id") int id) {
        return lectureService.findComments(id);
    }

    /* 댓글 추가*/
    @PostMapping("/comment")
    public ResponseDTO createComment(@RequestBody @Valid CommentRequestDTO commentRequestDTO) {
        return lectureService.createComment(commentRequestDTO);
    }

    /* 댓글 삭제 */
    @DeleteMapping("/comment")
    public ResponseDTO removeComment(@RequestParam("id") int id) {
        return lectureService.removeComment(id);
    }

    /* 댓글 수정 */
    @PutMapping("/comment")
    public ResponseDTO updateComment(@RequestBody @Valid CommentUpdateDTO commentUpdateDTO, @RequestParam("id") int id) {
        return lectureService.updateComment(id, commentUpdateDTO);
    }

    /*____________________대댓글_________________________*/

    /* 대댓글 추가 */
    @PostMapping("/reply")
    public ResponseDTO createReply(@RequestBody @Valid ReplyRequestDTO replyRequestDTO) {
        return lectureService.createReply(replyRequestDTO);
    }

    /* 대댓글 삭제 */
    @DeleteMapping("/reply")
    public ResponseDTO removeReply(@RequestParam("id") int id) {
        return lectureService.removeReply(id);
    }

    /* 대댓글 수정 */
    @PutMapping("/reply")
    public ResponseDTO updateReply(@RequestBody @Valid ReplyUpdateDTO replyUpdateDTO, @RequestParam("id") int id) {
        return lectureService.updateReply(id, replyUpdateDTO);
    }

    /*____________________좋아요_________________________*/

    /* 사용자 좋아요 확인 */
    @GetMapping("/like")
    public ResponseDTO findLike(@RequestParam("lecture_id") int lecture_id,@RequestParam("user_id") int user_id) {
        return lectureService.findLike(lecture_id,user_id);
    }

    /* 사용자 좋아요 변경 */
    @PostMapping("/like")
    public ResponseDTO createLike(@RequestBody @Valid LikeRequestDTO likeRequestDTO) {
        return lectureService.setLike(likeRequestDTO);
    }
}
