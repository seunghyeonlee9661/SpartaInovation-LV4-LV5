package com.example.Sparta.service;
import com.example.Sparta.dto.*;
import com.example.Sparta.entity.*;
import com.example.Sparta.enums.LectureCategory;
import com.example.Sparta.global.JwtUtil;
import com.example.Sparta.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SpartaService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public SpartaService(UserRepository userRepository, TeacherRepository teacherRepository, LectureRepository lectureRepository, CommentRepository commentRepository, ReplyRepository replyRepository, LikeRepository likeRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
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
    public ResponseDTO  signup(UserRequestDTO userRequestDTO){
        try {
            if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
            User user = new User(userRequestDTO,passwordEncoder.encode(userRequestDTO.getPassword()));
            userRepository.save(user);
            return new ResponseDTO(HttpStatus.OK.value(), "회원가입 완료", null);
        }catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    /*------------------------강사----------------------------------*/

    /* 강사 목록 불러오기 */
    public ResponseDTO findTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return new ResponseDTO(HttpStatus.OK.value(), "강사 목록 조회",teachers.stream().map(TeacherResponseDTO::new).toList());
    }
    
    /* 강사 정보 불러오기 : 사용자 아이디 */
    public ResponseDTO findTeacher(int id) {// DB 조회
        try{
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Teacher"));
            return new ResponseDTO(HttpStatus.OK.value(), "강사 정보 호출", new TeacherResponseDTO(teacher));
        }catch (Exception e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 강사 추가 */
    @Transactional
    public ResponseDTO createLecture(TeacherRequestDTO teacherRequestDTO){
        try {
            teacherRepository.save(new Teacher(teacherRequestDTO));
            return new ResponseDTO(HttpStatus.OK.value(), "강사 정보가 추가되었습니다", null);
        }catch (Exception e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 강사 수정 */
    @Transactional
    public ResponseDTO updateTeacher(int id, TeacherRequestDTO teacherRequestDTO) {
        try {
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
            teacher.update(teacherRequestDTO);
            return new ResponseDTO(HttpStatus.OK.value(), "강사 정보가 수정되었습니다", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 강사 삭제 */
    @Transactional
    public ResponseDTO removeTeacher(int id){
        try {
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
            teacherRepository.delete(teacher);
            return new ResponseDTO(HttpStatus.OK.value(), "강사 정보가 삭제되었습니다", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }


    /*------------------------강의----------------------------------*/

    /* 강의 목록 불러오기 : 페이지, 카테고리 */
    public ResponseDTO findLectures(int page, String category) {// DB 조회
        Pageable pageable = PageRequest.of(page, 10);
        Page<LectureResponseDTO> lectures;
        if(category.isEmpty()){
            lectures =  lectureRepository.findAll(pageable).map(LectureResponseDTO::new);
        }else{
            LectureCategory lectureCategory = LectureCategory.valueOf(category);
            lectures =  lectureRepository.findByCategoryOrderByRegistDesc(lectureCategory, pageable).map(LectureResponseDTO::new);
        }
        return new ResponseDTO(HttpStatus.OK.value(), "강의 목록 검색 완료", lectures);
    }
    
    /* 강의 정보 불러오기 */
    public ResponseDTO findLecture(int id) {// DB 조회
        try{
            Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Lecture"));
            return new ResponseDTO(HttpStatus.OK.value(), "강의 정보 검색 완료", new LectureResponseDTO(lecture));
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 강의 추가 */
    @Transactional
    public ResponseDTO createLecture(LectureRequestDTO lectureRequestDTO){
        try {
            Teacher teacher = teacherRepository.findById(lectureRequestDTO.getTeacher_id()).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
            Lecture lecture = new Lecture(lectureRequestDTO, teacher);
            lectureRepository.save(lecture);
            return new ResponseDTO(HttpStatus.OK.value(), "강의 정보 생성 완료", new LectureResponseDTO(lecture));
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 강의 수정 */
    @Transactional
    public ResponseDTO updateLecture(int id, LectureRequestDTO lectureRequestDTO){
        try {
            Teacher teacher = teacherRepository.findById(lectureRequestDTO.getTeacher_id()).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
            Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
            lecture.update(lectureRequestDTO, teacher);
            return new ResponseDTO(HttpStatus.OK.value(), "강의 정보 수정 완료", new LectureResponseDTO(lecture));
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 강의 삭제 */
    @Transactional
    public ResponseDTO removeLecture(int id){
        try {
            Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
            lectureRepository.delete(lecture);
            return new ResponseDTO(HttpStatus.OK.value(), "강의 정보 삭제 완료", new LectureResponseDTO(lecture));
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /*------------------------댓글----------------------------------*/

    /* 댓글 목록 불러오기 */
    public ResponseDTO findComments(int id) {// DB 조회
        List<Comment> comments = commentRepository.findByLectureId(id);
        return new ResponseDTO(HttpStatus.OK.value(), "강사 목록 조회",comments.stream().map(CommentResponseDTO::new).toList());
    }

    /* 댓글 추가 */
    @Transactional
    public ResponseDTO createComment(CommentRequestDTO commentRequestDTO){
        try {
            Lecture lecture = lectureRepository.findById(commentRequestDTO.getLecture_id()).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
            User user = userRepository.findById(commentRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            Comment comment = new Comment(lecture,user,commentRequestDTO.getText());
            commentRepository.save(comment);
            return new ResponseDTO(HttpStatus.OK.value(), "댓글 추가 완료",null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 댓글 수정 */
    @Transactional
    public ResponseDTO updateComment(int id, CommentUpdateDTO commentUpdateDTO){
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(()-> new NoSuchElementException("댓글이 존재하지 않습니다."));
            comment.update(commentUpdateDTO);
            return new ResponseDTO(HttpStatus.OK.value(), "댓글 수정 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 댓글 삭제 */
    @Transactional
    public ResponseDTO removeComment(int id){
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(()-> new NoSuchElementException("댓글이 존재하지 않습니다."));
            commentRepository.delete(comment);
            return new ResponseDTO(HttpStatus.OK.value(), "댓글 삭제 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /*------------------------대댓글----------------------------------*/

    /* 대댓글 추가 */
    @Transactional
    public ResponseDTO createReply(ReplyRequestDTO replyRequestDTO){
        try {
            Comment comment = commentRepository.findById(replyRequestDTO.getComment_id()).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
            User user = userRepository.findById(replyRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            Reply reply = new Reply(comment,user,replyRequestDTO.getText());
            replyRepository.save(reply);
            return new ResponseDTO(HttpStatus.OK.value(), "대댓글 추가 완료",null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 대댓글 수정 */
    @Transactional
    public ResponseDTO updateReply(int id, ReplyUpdateDTO replyUpdateDTO){
        try {
            Reply reply = replyRepository.findById(id).orElseThrow(()-> new NoSuchElementException("대댓글이 존재하지 않습니다."));
            reply.update(replyUpdateDTO);
            return new ResponseDTO(HttpStatus.OK.value(), "대댓글 수정 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 대댓글 삭제 */
    @Transactional
    public ResponseDTO removeReply(int id){
        try {
            Reply reply = replyRepository.findById(id).orElseThrow(()-> new NoSuchElementException("대댓글이 존재하지 않습니다."));
            replyRepository.delete(reply);
            return new ResponseDTO(HttpStatus.OK.value(), "대댓글 삭제 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /*------------------------대댓글----------------------------------*/

    /* 대댓글 추가 */
    @Transactional
    public ResponseDTO findLike(int lecture_id, int user_id){
        try {
            Like like = likeRepository.findByLectureIdAndUserId(lecture_id,user_id);
            if(like == null){
                return new ResponseDTO(HttpStatus.OK.value(), "true",true);
            }else{
                return new ResponseDTO(HttpStatus.OK.value(), "false",false);
            }
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 대댓글 수정 */
    @Transactional
    public ResponseDTO setLike(LikeRequestDTO likeRequestDTO){
        try {
            Like like = likeRepository.findByLectureIdAndUserId(likeRequestDTO.getLecture_id(),likeRequestDTO.getUser_id());
            Lecture lecture = lectureRepository.findById(likeRequestDTO.getLecture_id()).orElseThrow(()-> new NoSuchElementException("강의가 존재하지 않습니다."));
            if(like == null){
                User user = userRepository.findById(likeRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
                likeRepository.save(new Like(lecture,user));
                return new ResponseDTO(HttpStatus.OK.value(), "좋아요 추가", lecture.getLikes().size());
            }else{
                likeRepository.delete(like);
                return new ResponseDTO(HttpStatus.OK.value(), "좋아요 취소", lecture.getLikes().size()-1);
            }
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }
}