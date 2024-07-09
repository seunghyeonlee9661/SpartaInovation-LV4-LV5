package com.example.Sparta.service;
import com.example.Sparta.dto.*;
import com.example.Sparta.entity.Lecture;
import com.example.Sparta.entity.Teacher;
import com.example.Sparta.entity.User;
import com.example.Sparta.global.JwtUtil;
import com.example.Sparta.repository.LectureRepository;
import com.example.Sparta.repository.TeacherRepository;
import com.example.Sparta.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpartaService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final LectureRepository lectureRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public SpartaService(UserRepository userRepository, TeacherRepository teacherRepository, LectureRepository lectureRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.lectureRepository = lectureRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    /*----------------------회원정보--------------------------------*/

    /* 로그인 */
    public ResponseEntity<String> login(LoginRequestDTO loginRequestDTO, HttpServletResponse res) {
        try {
            User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
            }
            /* 사용자 이메일, 사용자 역할 기반으로 토큰 생성 */
            String token = jwtUtil.createToken(user.getEmail(),user.getAuthority());
            /* 쿠키에 토큰 저장 */
            jwtUtil.addJwtToCookie(token, res);
            return ResponseEntity.ok().body("로그인 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /* 회원가입 */
    public ResponseEntity<String> signup(UserRequestDTO userRequestDTO){
        try {
            if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
            User user = new User(userRequestDTO,passwordEncoder.encode(userRequestDTO.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*------------------------강사----------------------------------*/

    /* 강사 목록 불러오기 */
    public List<Teacher> findTeachers() {
        return teacherRepository.findAll();
    }

    /* 강사 추가 */
    public ResponseEntity<String> createLecture(TeacherRequestDTO teacherRequestDTO){
        try {
            teacherRepository.save(new Teacher(teacherRequestDTO));
            return ResponseEntity.ok("강사가 추가되었습니다.");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* 강사 정보 불러오기 */
    public Teacher findTeacher(int id) {// DB 조회
        return teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Teacher"));
    }

    /* 강사 삭제 */
    public ResponseEntity<String> removeTeacher(int id){
        try {
            Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
            if(optionalTeacher.isPresent()){
                teacherRepository.delete(optionalTeacher.get());
                return ResponseEntity.ok("강사가 삭제되었습니다.");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("강사가 존재하지 않습니다.");
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* 강사 수정 */
    public ResponseEntity<String> updateTeacher(int id, TeacherRequestDTO teacherRequestDTO) {
        try {
            Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
            teacher.update(teacherRequestDTO);
            teacherRepository.save(teacher);
            return ResponseEntity.ok("강사가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*------------------------강의----------------------------------*/

    /* 강의 목록 불러오기 */
    public Page<LectureResponseDTO> findLectures(int page, String category) {// DB 조회
        Pageable pageable = PageRequest.of(page, 10);
        Page<Lecture> lectures = (category.isEmpty()) ? lectureRepository.findAll(pageable) : lectureRepository.findByCategoryOrderByRegistDesc(category, pageable);
        return lectures.map(LectureResponseDTO::new);
    }

    /* 강의 추가 */
    public ResponseEntity<String> createLecture(LectureRequestDTO lectureRequestDTO){
        try {
            Teacher teacher = teacherRepository.findById(lectureRequestDTO.getTeacher_id()).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
            Lecture lecture = new Lecture(lectureRequestDTO, teacher);
            lectureRepository.save(lecture);
            return ResponseEntity.ok("강의가 추가되었습니다.");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* 강의 정보 불러오기 */
    public Lecture findLecture(int id) {// DB 조회
        return lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Lecture"));
    }

    /* 강의 수정 */
    public ResponseEntity<String> updateLecture(int id, LectureRequestDTO lectureRequestDTO){
        try {
            Teacher teacher = teacherRepository.findById(lectureRequestDTO.getTeacher_id()).orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
            Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));
            lecture.update(lectureRequestDTO, teacher);
            lectureRepository.save(lecture); // 업데이트된 강의 저장
            return ResponseEntity.ok("강의가 수정되었습니다.");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* 강의 삭제 */
    public ResponseEntity<String> removeLecture(int id){
        try {
            Optional<Lecture> optionalLecture = lectureRepository.findById(id);
            if(optionalLecture.isPresent()){
                lectureRepository.delete(optionalLecture.get());
                return ResponseEntity.ok("강의가 삭제되었습니다.");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("강의가 존재하지 않습니다.");
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
