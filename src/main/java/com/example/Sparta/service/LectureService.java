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
    public ResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO){
        try {
            if (userRepository.findByEmail(userCreateRequestDTO.getEmail()).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
            User user = new User(userCreateRequestDTO,passwordEncoder.encode(userCreateRequestDTO.getPassword()));
            userRepository.save(user);
            return new ResponseDTO(HttpStatus.OK.value(), "회원가입 완료", null);
        }catch (Exception e) {
            return new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        }
    }

    /* 회원 탈퇴*/
    @Transactional
    public ResponseDTO removeUser(UserDetailsImpl userDetails, String password){
        try {
            if(password == null || password.isEmpty()) return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 비밀번호 입니다.", null);
            User user = userDetails.getUser();
            if(!passwordEncoder.matches(password,user.getPassword())) return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.", null);
            userRepository.delete(user);
            return new ResponseDTO(HttpStatus.OK.value(), "회원탈퇴 완료", null);
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
    public ResponseDTO createLecture(TeacherCreateRequestDTO teacherCreateRequestDTO){
        try {
            teacherRepository.save(new Teacher(teacherCreateRequestDTO));
            return new ResponseDTO(HttpStatus.OK.value(), "강사 정보가 추가되었습니다", null);
        }catch (Exception e){
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 강사 수정 */
    @Transactional
    public ResponseDTO updateTeacher(int id, TeacherCreateRequestDTO teacherCreateRequestDTO) {
        try {
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
            teacher.update(teacherCreateRequestDTO);
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
    public ResponseDTO findLectures(int page, String category, String option, boolean desc) {
        Sort sort = option.isEmpty() ? Sort.unsorted() : Sort.by(desc ? Sort.Direction.DESC : Sort.Direction.ASC, option);
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<LectureResponseDTO> lectures;
        if (category.isEmpty()) {
            System.out.println("카테고리 없음");
            lectures = lectureRepository.findAll(pageable).map(LectureResponseDTO::new);
        } else {
            System.out.println("카테고리 있음");
            LectureCategory lectureCategory = LectureCategory.valueOf(category);
            pageable = PageRequest.of(page, 10,sort); // 카테고리에 따라 정렬 추가
            lectures = lectureRepository.findByCategory(lectureCategory, pageable).map(LectureResponseDTO::new);
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
    public ResponseDTO createLecture(LectureCreateRequestDTO lectureCreateRequestDTO){
        try {
            Teacher teacher = teacherRepository.findById(lectureCreateRequestDTO.getTeacher_id()).orElse(null);
            Lecture lecture = new Lecture(lectureCreateRequestDTO, teacher);
            lectureRepository.save(lecture);
            return new ResponseDTO(HttpStatus.OK.value(), "강의 정보 생성 완료", new LectureResponseDTO(lecture));
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 강의 수정 */
    @Transactional
    public ResponseDTO updateLecture(int id, LectureCreateRequestDTO lectureCreateRequestDTO){
        try {
            Teacher teacher = teacherRepository.findById(lectureCreateRequestDTO.getTeacher_id()).orElse(null);
            Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
            lecture.update(lectureCreateRequestDTO, teacher);
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
        List<CommentResponseDTO> comments = commentRepository.findByLectureId(id).stream().map(CommentResponseDTO::new).toList();
        return new ResponseDTO(HttpStatus.OK.value(), "강사 목록 조회",comments);
    }

    /* 댓글 추가 */
    @Transactional
    public ResponseDTO createComment(CommentCreateRequestDTO commentCreateRequestDTO){
        try {
            Lecture lecture = lectureRepository.findById(commentCreateRequestDTO.getLecture_id()).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
            User user = userRepository.findById(commentCreateRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            Comment comment = new Comment(lecture,user, commentCreateRequestDTO);
            commentRepository.save(comment);
            return new ResponseDTO(HttpStatus.OK.value(), "댓글 추가 완료",null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 댓글 수정 */
    @Transactional
    public ResponseDTO updateComment(CommentUpdateRequestDTO commentUpdateRequestDTO, UserDetailsImpl userDetails){
        try {
            Comment comment = commentRepository.findById(commentUpdateRequestDTO.getId()).orElseThrow(()-> new NoSuchElementException("댓글이 존재하지 않습니다."));
            if(userDetails.getUser().getId() != comment.getUser().getId()){
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "사용자의 댓글이 아닙니다.", null);
            }
            comment.update(commentUpdateRequestDTO);
            return new ResponseDTO(HttpStatus.OK.value(), "댓글 수정 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 댓글 삭제 */
    @Transactional
    public ResponseDTO removeComment(int id, UserDetailsImpl userDetails){
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(()-> new NoSuchElementException("댓글이 존재하지 않습니다."));
            if(userDetails.getUser().getId() != comment.getUser().getId()){
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "사용자의 댓글이 아닙니다.", null);
            }
            commentRepository.delete(comment);
            return new ResponseDTO(HttpStatus.OK.value(), "댓글 삭제 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /*------------------------대댓글----------------------------------*/

    /* 대댓글 추가 */
    @Transactional
    public ResponseDTO createReply(ReplyCreateRequestDTO replyCreateRequestDTO){
        try {
            Comment comment = commentRepository.findById(replyCreateRequestDTO.getComment_id()).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
            User user = userRepository.findById(replyCreateRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            Reply reply = new Reply(comment,user, replyCreateRequestDTO);
            replyRepository.save(reply);
            return new ResponseDTO(HttpStatus.OK.value(), "대댓글 추가 완료",null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 대댓글 수정 */
    @Transactional
    public ResponseDTO updateReply(ReplyUpdateRequestDTO replyUpdateRequestDTO, UserDetailsImpl userDetails){
        try {
            Reply reply = replyRepository.findById(replyUpdateRequestDTO.getId()).orElseThrow(()-> new NoSuchElementException("대댓글이 존재하지 않습니다."));
            if(userDetails.getUser().getId() != reply.getUser().getId()){
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "사용자의 대댓글이 아닙니다.", null);
            }
            reply.update(replyUpdateRequestDTO);
            return new ResponseDTO(HttpStatus.OK.value(), "대댓글 수정 완료", null);
        } catch (Exception e) {
            return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    /* 대댓글 삭제 */
    @Transactional
    public ResponseDTO removeReply(int id, UserDetailsImpl userDetails){
        try {
            Reply reply = replyRepository.findById(id).orElseThrow(()-> new NoSuchElementException("대댓글이 존재하지 않습니다."));
            if(userDetails.getUser().getId() != reply.getUser().getId()){
                return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "사용자의 대댓글이 아닙니다.", null);
            }
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
    public ResponseDTO setLike(LikeCreateRequestDTO likeCreateRequestDTO){
        try {
            Like like = likeRepository.findByLectureIdAndUserId(likeCreateRequestDTO.getLecture_id(), likeCreateRequestDTO.getUser_id());
            Lecture lecture = lectureRepository.findById(likeCreateRequestDTO.getLecture_id()).orElseThrow(()-> new NoSuchElementException("강의가 존재하지 않습니다."));
            if(like == null){
                User user = userRepository.findById(likeCreateRequestDTO.getUser_id()).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
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