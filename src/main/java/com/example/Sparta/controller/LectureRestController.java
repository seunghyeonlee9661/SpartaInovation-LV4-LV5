package com.example.Sparta.controller;

import com.example.Sparta.dto.request.*;
import com.example.Sparta.dto.response.CommentResponseDTO;
import com.example.Sparta.dto.response.LectureResponseDTO;
import com.example.Sparta.dto.response.TeacherResponseDTO;
import com.example.Sparta.enums.UserAuthority;
import com.example.Sparta.security.UserDetailsImpl;
import com.example.Sparta.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LectureRestController {

    private final LectureService lectureService;

    /*____________________사용자__________________________*/

    /* 사용자 추가 */
    @PostMapping("/user/signup")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserCreateRequestDTO requestDTO) {
        return lectureService.createUser(requestDTO);
    }

    /* 사용자 추가 */
    @PostMapping("/user/withdraw")
    public ResponseEntity<String> removeUser(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return lectureService.removeUser(userDetails,request.get("password"));
    }

    /*____________________강의__________________________*/
    
    /* 강의 목록 불러오기 : page(페이지네이션),category(카테고리),option(정렬옵션),desc(정렬방향) */
    @GetMapping("/lecture")
    public ResponseEntity<Page<LectureResponseDTO>> findLectures(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="category", defaultValue="") String category, @RequestParam(value="option", defaultValue="") String option, @RequestParam(value="desc", defaultValue="") boolean desc) {
        return lectureService.findLectureList(page, category,option,desc);
    }

    /* 강의 내용 불러오기 */
    @GetMapping("/lecture/{id}")
    public ResponseEntity<LectureResponseDTO> findLecture(@PathVariable(name = "id") int id) {
        return lectureService.findLecture(id);
    }

    /* 강의 추가 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PostMapping("/lecture")
    public ResponseEntity<String> createLecture(@RequestBody @Valid LectureCreateRequestDTO lectureCreateRequestDTO) {
        return lectureService.createLecture(lectureCreateRequestDTO);
    }

    /* 강의 삭제 */
    @Secured(UserAuthority.Authority.ADMIN)
    @DeleteMapping("/lecture")
    public ResponseEntity<String> removeLecture(@RequestParam("id") int id) {
        return lectureService.removeLecture(id);
    }

    /* 강의 수정 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PutMapping("/lecture")
    public ResponseEntity<String> updateLecture(@RequestBody @Valid LectureCreateRequestDTO lectureCreateRequestDTO, @RequestParam("id") int id) {
        return lectureService.updateLecture(id, lectureCreateRequestDTO);
    }

    /*____________________강사__________________________*/

    /* 강사 목록 불러오기 */
    @GetMapping("/teacher")
    public ResponseEntity<List<TeacherResponseDTO>> findTeachers() {
        return lectureService.findTeacherList();
    }

    /* 강사 정보 불러오기 */
    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherResponseDTO> findTeacher(@PathVariable(name = "id") int id) {
        return lectureService.findTeacher(id);
    }

    /* 강사 추가 */
    @Secured(UserAuthority.Authority.ADMIN)
    @PostMapping("/teacher")
    public ResponseEntity<String> createTeacher(@Valid @RequestBody TeacherCreateRequestDTO teacherCreateRequestDTO) {
        return lectureService.createTeacher(teacherCreateRequestDTO);
    }

    /* 강사 삭제 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @DeleteMapping("/teacher")
    public ResponseEntity<String> removeTeacher(@RequestParam("id") int id) {
        return lectureService.removeTeacher(id);
    }

    /* 강사 수정 */
    @Secured(UserAuthority.Authority.ADMIN) // 관리자용
    @PutMapping("/teacher")
    public ResponseEntity<String> updateTeacher(@Valid @RequestBody TeacherCreateRequestDTO teacherCreateRequestDTO, @RequestParam("id") int id) {
        return lectureService.updateTeacher(id, teacherCreateRequestDTO);
    }

    /*____________________댓글__________________________*/

    /* 댓글 불러오기 */
    @GetMapping("/comment/{id}")
    public ResponseEntity<List<CommentResponseDTO>> findComments(@PathVariable(name = "id") int id) {
        return lectureService.findComments(id);
    }

    /* 댓글 추가*/
    @PostMapping("/comment")
    public ResponseEntity<String> createComment(@Valid @RequestBody CommentCreateRequestDTO commentCreateRequestDTO) {
        return lectureService.createComment(commentCreateRequestDTO);
    }

    /* 댓글 삭제 */
    @DeleteMapping("/comment")
    public ResponseEntity<String> removeComment(@RequestParam("id") int id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return lectureService.removeComment(id,userDetails);
    }

    /* 댓글 수정 */
    @PutMapping("/comment")
    public ResponseEntity<String> updateComment(@Valid @RequestBody CommentUpdateRequestDTO commentUpdateRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return lectureService.updateComment(commentUpdateRequestDTO,userDetails);
    }

    /*____________________대댓글_________________________*/

    /* 대댓글 추가 */
    @PostMapping("/reply")
    public ResponseEntity<String> createReply(@RequestBody @Valid ReplyCreateRequestDTO replyCreateRequestDTO) {
        return lectureService.createReply(replyCreateRequestDTO);
    }

    /* 대댓글 삭제 */
    @DeleteMapping("/reply")
    public ResponseEntity<String> removeReply(@RequestParam("id") int id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return lectureService.removeReply(id,userDetails);
    }

    /* 대댓글 수정 */
    @PutMapping("/reply")
    public ResponseEntity<String> updateReply(@RequestBody @Valid ReplyUpdateRequestDTO replyUpdateRequestDTO,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return lectureService.updateReply(replyUpdateRequestDTO,userDetails);
    }

    /*____________________좋아요_________________________*/

    /* 사용자 좋아요 확인 */
    @GetMapping("/like")
    public ResponseEntity<Boolean> findLike(@RequestParam("lecture_id") int lecture_id,@RequestParam("user_id") int user_id) {
        return lectureService.findLike(lecture_id,user_id);
    }

    /* 사용자 좋아요 변경 */
    @PostMapping("/like")
    public ResponseEntity<Integer> createLike(@RequestBody @Valid LikeCreateRequestDTO likeCreateRequestDTO) {
        return lectureService.setLike(likeCreateRequestDTO);
    }
}
