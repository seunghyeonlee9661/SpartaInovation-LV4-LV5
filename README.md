# 📚 이노베이션 캠프 - 주특기 주차 (Spring)

## 🎯 프로젝트 목표
Spring 심화 학습 및 개인 능력 향상을 위한 프로젝트입니다. 주요 목표는 Spring Security와 같은 권한 제어 도구 사용, HTTP Status Code와 같은 오류 처리와 핸들링, 인터셉터와 같은 기능을 추가하는 것입니다.
- **Spring Framework**와 **Spring Boot**의 심화 개념 이해
- **Spring Security**를 이용한 권한 제어 도구 사용
- **HTTP Status Code**와 같은 오류 처리 및 핸들링
- **인터셉터**와 같은 기능 추가

## 프로젝트 단계

### 4️⃣ LV4 (2024년 7월 11일 ~ 7월 14일)
- 📝 스파르타 강의 사이트 서버 만들기
- 주요 기능:
  - **데이터베이스 모델링 및 구현**
  - **강의 정보 CRUD 기능 구현**
  - **사용자 인증 기능 추가**
    - Spring Security를 이용한 로그인 및 회원가입 기능 구현
    - 인증된 사용자만 강의 정보 수정 및 삭제 권한 부여
  - **강의 검색 기능 추가**
    - 강의 제목, 강사명 등으로 강의를 검색할 수 있는 기능 구현
  - **댓글 및 대댓글 기능 구현**
    - 각각의 강의에 대한 댓글 작성 및 조회 기능
    - 댓글에 대한 대댓글 작성 및 조회 기능
- [프로젝트 Notion 링크](https://leather-pixie-4bc.notion.site/Spring-LV4-261319436608434cbf7df93ae3019a94?pvs=74)

### 5️⃣ LV5 (2024년 7월 15일 ~ 7월 17일)
- 📝 스파르타 굿즈 판매 사이트 서버 만들기
- 주요 기능:
  - **데이터베이스 모델링 및 구현**
  - **상품 관리 및 주문 처리 기능 구현**
  - **사용자 인증 및 결제 시스템 연동**
  - **회원 가입 기능**
    - `이메일`, `비밀번호`, `성별`, `전화번호`, `주소`, `권한` 저장.
    - `이메일` 형식, `비밀번호` 규칙 검증.
    - 가입 성공 여부 반환.
  - **로그인 기능**
    - `이메일`, `비밀번호`로 로그인 요청.
    - JWT 발급 및 로그인 성공 여부 반환.
  - **상품 관리 기능**
    - ADMIN 권한 필요. JWT 인증.
    - `상품명`, `가격`, `수량`, `소개`, `카테고리` 저장.
    - 등록된 상품 정보 반환.
  - **상품 조회 기능**
    - 모든 사용자가 상품 조회 가능.
    - 페이징, 정렬(상품명, 가격) 지원.
  - **장바구니 기능**
    - JWT 인증 필요.
    - 회원만 추가, 조회, 수정, 삭제 가능.
    - 총 결제 금액 확인. 
- [프로젝트 Notion 링크](https://leather-pixie-4bc.notion.site/Spring-LV5-261319436608434cbf7df93ae3019a94)

## 주요 학습 내용

### 사용자 댓글, 대댓글 수정 삭제 기능
사용자의 댓글과 대댓글을 수정 및 삭제하는 기능을 구현하였습니다. 개인의 정보를 검증하기 위해 `@AuthenticationPrincipal`를 활용하여 로그인 사용자 정보를 확인합니다.

```java
@PutMapping("/comments/{id}")
public ResponseEntity<CommentResponse> updateComment(
    @PathVariable Long id,
    @RequestBody CommentRequest commentRequest,
    @AuthenticationPrincipal UserDetailsImpl userDetails) {
    
    CommentResponse updatedComment = commentService.updateComment(id, commentRequest, userDetails.getUser());
    return ResponseEntity.ok(updatedComment);
}
```
### 클린코드를 위해서 try-catch 지양
클린코드를 위해 try-catch 블록을 지양하고, 대신 ResponseEntity를 반환하여 오류 처리를 @RestControllerAdvice를 사용한 GlobalExceptionHandler에서 처리하도록 구현하였습니다.
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
```

### 데이터 트래픽 발생과 수집에 대한 지식
트랜잭션과 락을 통해 로그 밸런싱을 적용하여 대용량 데이터 흐름에 대처하는 방법을 고려하였습니다. 락 처리만으로 서비스가 처리 가능한지, 서버 증설이 필요한지 평가하고, 락 선점 문제와 증설에 대한 대처 프로세스를 고려해야 합니다.
1. 트랜잭션과 락을 통한 데이터 일관성 유지
2. 데이터베이스 락 선점 문제 해결
3. 서버 증설과 로드 밸런싱 적용
4. 락 해제 및 재시도 메커니즘 구현

### JPA의 N+1 문제 해결
JPA의 N+1 문제를 해결하기 위해 지연 로딩(Lazy Loading)과 즉시 로딩(Eager Loading)을 적절히 사용하였습니다.
```java
@Entity
public class Post {
    
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments;
}
```

### FTP를 통한 이미지 업로드와 다운로드 처리
FTP를 통해 이미지 업로드와 다운로드를 처리하는 기능을 구현하였습니다.
```java
public void uploadFile(String ftpServer, String user, String password, String filePath) {
    FTPClient ftpClient = new FTPClient();
    try {
        ftpClient.connect(ftpServer);
        ftpClient.login(user, password);
        ftpClient.storeFile(filePath, new FileInputStream(new File(filePath)));
        ftpClient.logout();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 현재 댓글과 대댓글은 Comment와 reply의 관계로 한 단계만으로 구현되어 있음
댓글이 하위 댓글을 참조함으로써 원하는 만큼 단계를 만들 수 있습니다.
```java
@Entity
public class Comment {
    
    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
    private List<Comment> replies;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;
}
```

### Secured 말고 다른 방식으로 권한을 확인하는 방법
<code>@Secured</code> 외에도 <code>@PreAuthorize</code>, <code>@PostAuthorize</code>와 같은 어노테이션을 사용하여 메서드 수준에서 권한을 확인할 수 있습니다.
```java
@PreAuthorize("hasRole('ROLE_ADMIN')")
@GetMapping("/admin")
public String getAdminPage() {
    return "Admin page";
}
```
