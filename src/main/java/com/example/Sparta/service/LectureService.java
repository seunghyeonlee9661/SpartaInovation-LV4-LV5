package com.example.Sparta.service;
import com.example.Sparta.dto.request.*;
import com.example.Sparta.dto.response.CommentResponseDTO;
import com.example.Sparta.dto.response.LectureResponseDTO;
import com.example.Sparta.dto.response.ResponseDTO;
import com.example.Sparta.dto.response.TeacherResponseDTO;
import com.example.Sparta.entity.*;
import com.example.Sparta.enums.LectureCategory;
import com.example.Sparta.global.JwtUtil;
import com.example.Sparta.repository.*;
import com.example.Sparta.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LectureService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public LectureService(UserRepository userRepository, TeacherRepository teacherRepository, LectureRepository lectureRepository, CommentRepository commentRepository, ReplyRepository replyRepository, LikeRepository likeRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.lectureRepository = lectureRepository;
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
        this.likeRepository = likeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    /*----------------------회원정보--------------------------------*/

    /* 회원가입 */
    @Transactional
    public ResponseEntity<String> createUser(UserCreateRequestDTO userCreateRequestDTO){
            if (userRepository.findByEmail(userCreateRequestDTO.getEmail()).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
            User user = new User(userCreateRequestDTO,passwordEncoder.encode(userCreateRequestDTO.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("회원가입 완료");
    }

    /* 회원 탈퇴*/
    @Transactional
    public ResponseEntity<String> removeUser(UserDetailsImpl userDetails, String password){
            if(password == null || password.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("올바르지 않은 비밀번호 입니다.");
            User user = userDetails.getUser();
            if(!passwordEncoder.matches(password,user.getPassword())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
            userRepository.delete(user);
            return ResponseEntity.ok("회원탈퇴 완료");
    }

    /*------------------------강사----------------------------------*/

    /* 강사 목록 불러오기 */
    public ResponseEntity<List<TeacherResponseDTO>> findTeacherList() {
        List<TeacherResponseDTO> teachers = teacherRepository.findAll().stream().map(TeacherResponseDTO::new).toList();
        return ResponseEntity.ok(teachers);
    }
    
    /* 강사 정보 불러오기 : 사용자 아이디 */
    public ResponseEntity<TeacherResponseDTO> findTeacher(int id) {// DB 조회
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Teacher"));
            return ResponseEntity.ok(new TeacherResponseDTO(teacher));
    }

    /* 강사 추가 */
    @Transactional
    public ResponseEntity<String> createTeacher(TeacherCreateRequestDTO teacherCreateRequestDTO){
        teacherRepository.save(new Teacher(teacherCreateRequestDTO));
        return ResponseEntity.ok("강사 정보가 추가되었습니다");
    }

    /* 강사 수정 */
    @Transactional
    public ResponseEntity<String> updateTeacher(int id, TeacherCreateRequestDTO teacherCreateRequestDTO) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
        teacher.update(teacherCreateRequestDTO);
        return ResponseEntity.ok("강사 정보가 수정되었습니다");
    }

    /* 강사 삭제 */
    @Transactional
    public ResponseEntity<String> removeTeacher(int id){
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
        teacherRepository.delete(teacher);
        return ResponseEntity.ok("강사 정보가 삭제되었습니다");
    }

    /*------------------------강의----------------------------------*/

    /* 강의 목록 불러오기 : 페이지, 카테고리 */
    public ResponseEntity<Page<LectureResponseDTO>> findLectureList(int page, String category, String option, boolean desc) {
        Sort sort = option.isEmpty() ? Sort.unsorted() : Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, option);
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<LectureResponseDTO> lectures;
        if (category.isEmpty()) {
            lectures = lectureRepository.findAll(pageable).map(LectureResponseDTO::new);
        } else {
            LectureCategory lectureCategory = LectureCategory.valueOf(category);
            lectures = lectureRepository.findByCategory(lectureCategory, pageable).map(LectureResponseDTO::new);
        }
        return ResponseEntity.ok(lectures);
    }
    
    /* 강의 정보 불러오기 */
    public ResponseEntity<LectureResponseDTO> findLecture(int id) {// DB 조회
            Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Lecture"));
            return ResponseEntity.ok(new LectureResponseDTO(lecture));
    }

    /* 강의 추가 */
    @Transactional
    public ResponseEntity<String> createLecture(LectureCreateRequestDTO lectureCreateRequestDTO){
        Teacher teacher = teacherRepository.findById(lectureCreateRequestDTO.getTeacher_id()).orElse(null);
        Lecture lecture = new Lecture(lectureCreateRequestDTO, teacher);
        lectureRepository.save(lecture);
        return ResponseEntity.ok("강의 정보 생성 완료");
    }

    /* 강의 수정 */
    @Transactional
    public ResponseEntity<String> updateLecture(int id, LectureCreateRequestDTO lectureCreateRequestDTO){
        Teacher teacher = teacherRepository.findById(lectureCreateRequestDTO.getTeacher_id()).orElse(null);
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
        lecture.update(lectureCreateRequestDTO, teacher);
        return ResponseEntity.ok("강의 정보 수정 완료");
    }

    /* 강의 삭제 */
    @Transactional
    public ResponseEntity<String> removeLecture(int id){
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
        lectureRepository.delete(lecture);
        return ResponseEntity.ok("강의 정보 삭제 완료");
    }

    /*------------------------댓글----------------------------------*/

    /* 댓글 목록 불러오기 */
    public ResponseEntity<List<CommentResponseDTO>> findComments(int id) {// DB 조회
        List<CommentResponseDTO> comments = commentRepository.findByLectureId(id).stream().map(CommentResponseDTO::new).toList();
        return ResponseEntity.ok(comments);
    }

    /* 댓글 추가 */
    @Transactional
    public ResponseEntity<String> createComment(CommentCreateRequestDTO commentCreateRequestDTO){
        Lecture lecture = lectureRepository.findById(commentCreateRequestDTO.getLecture_id()).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
        User user = userRepository.findById(commentCreateRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Comment comment = new Comment(lecture,user, commentCreateRequestDTO);
        commentRepository.save(comment);
        return ResponseEntity.ok("댓글 추가 완료");
    }

    /* 댓글 수정 */
    @Transactional
    public ResponseEntity<String> updateComment(CommentUpdateRequestDTO commentUpdateRequestDTO, UserDetailsImpl userDetails){
            Comment comment = commentRepository.findById(commentUpdateRequestDTO.getId()).orElseThrow(()-> new NoSuchElementException("댓글이 존재하지 않습니다."));
            if(userDetails.getUser().getId() != comment.getUser().getId()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 댓글이 아닙니다.");
            comment.update(commentUpdateRequestDTO);
            return ResponseEntity.ok("댓글 수정 완료");
    }

    /* 댓글 삭제 */
    @Transactional
    public ResponseEntity<String> removeComment(int id, UserDetailsImpl userDetails){
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new NoSuchElementException("댓글이 존재하지 않습니다."));
        if(userDetails.getUser().getId() != comment.getUser().getId()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 댓글이 아닙니다.");
        commentRepository.delete(comment);
        return ResponseEntity.ok("댓글 삭제 완료");
    }

    /*------------------------대댓글----------------------------------*/

    /* 대댓글 추가 */
    @Transactional
    public ResponseEntity<String> createReply(ReplyCreateRequestDTO replyCreateRequestDTO){
        Comment comment = commentRepository.findById(replyCreateRequestDTO.getComment_id()).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        User user = userRepository.findById(replyCreateRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Reply reply = new Reply(comment,user, replyCreateRequestDTO);
        replyRepository.save(reply);
        return ResponseEntity.ok("대댓글 추가 완료");
    }

    /* 대댓글 수정 */
    @Transactional
    public ResponseEntity<String> updateReply(ReplyUpdateRequestDTO replyUpdateRequestDTO, UserDetailsImpl userDetails){
        Reply reply = replyRepository.findById(replyUpdateRequestDTO.getId()).orElseThrow(()-> new NoSuchElementException("대댓글이 존재하지 않습니다."));
        if(userDetails.getUser().getId() != reply.getUser().getId()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 댓글이 아닙니다.");
        reply.update(replyUpdateRequestDTO);
        return ResponseEntity.ok("대댓글 수정 완료");
    }

    /* 대댓글 삭제 */
    @Transactional
    public ResponseEntity<String> removeReply(int id, UserDetailsImpl userDetails){
        Reply reply = replyRepository.findById(id).orElseThrow(()-> new NoSuchElementException("대댓글이 존재하지 않습니다."));
        if(userDetails.getUser().getId() != reply.getUser().getId()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 댓글이 아닙니다.");
        replyRepository.delete(reply);
        return ResponseEntity.ok("대댓글 삭제 완료");
    }

    /*------------------------좋아요----------------------------------*/

    /* 좋아요 추가 */
    @Transactional
    public ResponseEntity<Boolean> findLike(int lecture_id, int user_id){
        Like like = likeRepository.findByLectureIdAndUserId(lecture_id,user_id);
        if(like == null){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }

    /* 대댓글 수정 */
    @Transactional
    public ResponseEntity<Integer> setLike(LikeCreateRequestDTO likeCreateRequestDTO){
        Like like = likeRepository.findByLectureIdAndUserId(likeCreateRequestDTO.getLecture_id(), likeCreateRequestDTO.getUser_id());
        Lecture lecture = lectureRepository.findById(likeCreateRequestDTO.getLecture_id()).orElseThrow(()-> new NoSuchElementException("강의가 존재하지 않습니다."));
        if(like == null){
            User user = userRepository.findById(likeCreateRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            likeRepository.save(new Like(lecture,user));
        }else{
            likeRepository.delete(like);
        }
        return ResponseEntity.ok( lecture.getLikes().size());
    }
}