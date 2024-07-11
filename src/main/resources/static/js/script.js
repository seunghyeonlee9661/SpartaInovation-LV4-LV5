//_______________사용자__________________________

// 회원가입
function signup() {
    if (checkValidity('signupForm')) {
        Request('/api/user/signup', 'POST', {
            'email': $('#email').val(),
            'password': $('#password').val(),
            'gender': $(":input:radio[name=gender]:checked").val(),
            'phone': $('#phone1').val() + $('#phone2').val() + $('#phone3').val(),
            'address': $('#address').val(),
            'authority': $(":input:radio[name=authority]:checked").val()
        })
        .then(function(response) {
            if (response.status === 200) {
                location.href = "/login";
            } else {
                alert(response.message);
            }
        })
        .catch(function(xhr, status, error) {
            console.log(xhr)
            console.log(status)
            console.log(error)
            alert('서버 오류가 발생했습니다.');
        });
    }
}

// 로그인
function login() {
    if (checkValidity('loginForm')) {
        Request('/api/user/login', 'POST', {
                'username': $('#login_email').val(),
                'password': $('#login_password').val()
            })
            .done(function(res,status,xhr) {
                  if(status == "success") {
                    alert("로그인 성공")
                    location.href = '/';
                  }
              })
            .fail(function(xhr, textStatus, errorThrown) {
                   console.log('statusCode: ' + xhr.status);
                   console.log(xhr);
                   console.log(textStatus);
                   console.log(errorThrown);
                   location.href = '/login?error'
               });
    }
}

//_______________강사_______________________
// 강사 추가
function addTeacher() {
    if (checkValidity('addTeacherForm')) {
        Request('/api/teacher', 'POST', {
                'name': $('#addTeacher_name').val(),
                'year': $('#addTeacher_year').val(),
                'company': $('#addTeacher_company').val(),
                'phone': $('#addTeacher_phone1').val() + $('#addTeacher_phone2').val() + $('#addTeacher_phone3').val(),
                'introduction': $('#addTeacher_introduction').val()
            })
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    location.href = location.href;
                } else {
                    alert(response.message);
                }
            })
            .catch(function(xhr, status, error) {
                console.log(xhr)
                console.log(status)
                console.log(error)
                alert('서버 오류가 발생했습니다.');
            })
    }
}
// 강사 목록 불러오기
function getTeachers() {
    Request('/api/teachers', 'GET', null)
        .then(function(response) {
            let teachers = response.data;
            var html = '';
            for (var i = 0; i < teachers.length; i++) {
                var teacher = teachers[i];
                html += '<li><a href="/teacher/' + teacher.id + '">' + teacher.name + '</a></li>';
            }
            $('.vertical-menu').html(html);

            var selectHtml = '<option value="">...</option>';
            for (var i = 0; i < teachers.length; i++) {
                var teacher = teachers[i];
                selectHtml += '<option value="' + teacher.id + '">' + teacher.name + '</option>';
            }
            $('#addLecture_teacher').html(selectHtml);
        })
       .catch(function(xhr, status, error) {
           console.log(xhr)
           console.log(status)
           console.log(error)
           alert('서버 오류가 발생했습니다.');
       })
}
// 강사 정보 불러오기
function getTeacher(id) {
    Request('/api/teacher', 'GET', {
          id: id,
        })
        .then(function(response) {
            if (response.status === 200) {
                let teacher = response.data;
                $('#teacher-name').text(teacher.name);
                $('#teacher-year').text(teacher.year);
                $('#teacher-company').text(teacher.company);
                $('#teacher-phone').text(teacher.phone);
                $('#teacher-introduction').text(teacher.introduction);

                $('#editTeacher_name').val(teacher.name);
                $('#editTeacher_year').val(teacher.year);
                $('#editTeacher_company').val(teacher.company);
                var phone = teacher.phone;
                $('#editTeacher_phone1').val(phone.substring(0, 3));
                $('#editTeacher_phone2').val(phone.substring(3, 7));
                $('#editTeacher_phone3').val(phone.substring(7, 11));
                $('#editTeacher_introduction').val(teacher.introduction);

                // 강의 목록 업데이트
                var lectureList = $('#lecture-list');
                lectureList.empty();  // 기존 목록 제거

                teacher.lectures.forEach(function(lecture) {
                    var lectureItem = $('<li></li>');
                    var lectureLink = $('<a></a>').attr('href', '/lecture/' + lecture.id).addClass('lecture-link');
                    var lectureTitle = $('<span></span>').addClass('lecture-title').text(lecture.title);
                    var lectureCategory = $('<span></span>').addClass('lecture-category').text(lecture.category);
                    lectureLink.append(lectureTitle).append(lectureCategory);
                    lectureItem.append(lectureLink);
                    lectureList.append(lectureItem);
                });
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert(error);
        });
}
// 강사 정보 삭제
function deleteTeacher(id) {
    if (checkRole()) {
        if (confirm('삭제하시겠습니까?')) {
            Request('/api/teacher?id=' + id, 'DELETE', null)
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    window.location.href = '/';
                } else {
                    alert(response.message);
                }
            })
            .catch(function(error) {
                alert(error.responseText);
            });
        }
    }
}
// 강사 정보 수정
function editTeacher(id) {
    let form = document.getElementById('editTeacherForm');
    if (checkValidity(form)) {
        Request('/api/teacher?id='+id, 'PUT', {
                'name': $('#editTeacher_name').val(),
                'year': $('#editTeacher_year').val(),
                'company': $('#editTeacher_company').val(),
                'phone': $('#editTeacher_phone1').val() + $('#editTeacher_phone2').val() + $('#editTeacher_phone3').val(),
                'introduction': $('#editTeacher_introduction').val()
            })
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    location.href = location.href;
                } else {
                    alert(response.message);
                }
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
//_______________강의_______________________
// 강의 추가
function addLecture() {
    if (checkValidity('addLectureForm')) {
        Request('/api/lecture', 'POST', {
                'title': $('#addLecture_title').val(),
                'price': $('#addLecture_price').val(),
                'introduction': $('#addLecture_introduction').val(),
                'category': $('#addLecture_category').val(),
                'teacher_id': $('#addLecture_teacher').val()
            })
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    location.href = location.href;
                } else {
                    alert(response.message);
                }
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
// 강의 목록 불러오기
function getLectures(page,category) {
    Request('/api/lectures', 'GET', {
            page: page,
            category: category
        })
        .then(function(response) {
            if (response.status === 200) {
                let lectures = response.data;
                var html = '';
                for (var i = 0; i < lectures.content.length; i++) {
                    var lecture = lectures.content[i];
                    html += '<tr>';
                    html += '<td><a href="/lecture/' + lecture.id + '">' + lecture.title + '</a></td>';
                    html += '<td>' + lecture.price + '</td>';
                    html += '<td>' + lecture.teacher.name + '</td>';
                    html += '<td>' + lecture.category + '</td>';
                    html += '<td>' + getFormattedDate(lecture.regist) + '</td>';
                    html += '</tr>';
                }
                document.querySelector('tbody').innerHTML = html;
                setPagination(lectures);
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert('강의 목록을 불러오는 중 오류가 발생했습니다.');
            console.log(error);
        });
}
// 강의 정보 불러오기
function getLecture(id) {
    Request('/api/lecture', 'GET', {
          id: id,
        })
        .then(function(response) {
            if (response.status === 200) {
                alert(response.message);
                // 강의 정보 작성
                let lecture = response.data;
                $('#lectureTitle').text(lecture.title);
                $('#lecturePrice').text(lecture.price);
                $('#lectureCategory').text(lecture.category);
                $('#lectureTeacher').text(lecture.teacher.name);
                $('#lectureDate').text(getFormattedDate(lecture.regist));
                $('#lectureIntroduction').text(lecture.introduction);

                console.log(response.data);

            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert('강의 정보를 불러오는 중 오류가 발생했습니다.');
            console.log(error);
        });
}



// 강의 수정을 위한 강사 목록 불러오기
function getTeacherList() {
    Request('/api/teachers', 'GET', null)
        .then(function(response) {
            if (response.status === 200) {
                var teacherSelect = $('#editLecture_teacher');
                teacherSelect.empty(); // 기존 옵션들을 모두 지움
                $.each(response.data, function(index, teacher) {
                    var option = $('<option></option>')
                        .attr('value', teacher.id)
                        .text(teacher.name);
                    if (teacher.id === id) {
                        option.attr('selected', 'selected'); // 기본 선택 항목 설정
                    }
                    teacherSelect.append(option);
                });
            } else {
                alert(response.message);
            }
        })
        .catch(function(response) {
            alert('강사 목록을 불러오는 중 오류가 발생했습니다.');
            console.log(response);
        });
}
// 강의 삭제
function deleteLecture(id) {
    if (checkRole()) {
      if (confirm('삭제하시겠습니까?')) {
          Request('/api/lecture?id=' + id, 'DELETE', null)
          .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                     window.location.href = '/main';
                } else {
                    alert(response.message);
                }
          })
          .catch(function(error) {
              alert(error.responseText);
          });
      }
    }
}

// 수정 모달 열기 함수
function callEditLectureModal() {
    if (checkRole()) {
        // 강의 수정 모달에 값 설정
        $('#editLecture_title').val($('#lectureTitle').text());
        $('#editLecture_price').val($('#lecturePrice').text());
        $('#editLecture_introduction').val($('#lectureIntroduction').text());
        $('#editLecture_category').val($('#lectureCategory').text());
        getTeacherList();
        // 모달 열기
        let myModal = new bootstrap.Modal(document.getElementById('editModal'), {
            keyboard: false
        });
        myModal.show();
    }
}
// 강의 수정
function editLecture(id) {
    if (checkValidity('editLectureForm')) {
        Request('/api/lecture?id='+id, 'PUT', {
                'title': $('#editLecture_title').val(),
                'price': $('#editLecture_price').val(),
                'introduction': $('#editLecture_introduction').val(),
                'category': $('#editLecture_category').val(),
                'teacher_id': $('#editLecture_teacher').val()
            })
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    location.href = location.href;
                } else {
                    alert(response.message);
                }
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
//_______________댓글__________________________
// 댓글 추가
function addComment(lecture_id,user_id,text) {
    if (checkValidity('addLectureForm')) {
        Request('/api/comment', 'POST', {
                'lecture_id': lecture_id,
                'user_id': user_id,
                'text': $('#addComment_text').val(),
            })
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    location.href = location.href;
                } else {
                    alert(response.message);
                }
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
// 댓글 삭제
function deleteComment(id) {
    if (checkRole()) {
      if (confirm('삭제하시겠습니까?')) {
          Request('/api/comment?id=' + id, 'DELETE', null)
          .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                     window.location.href = '/main';
                } else {
                    alert(response.message);
                }
          })
          .catch(function(error) {
              alert(error.responseText);
          });
      }
    }
}
// 댓글 수정
function editComment(id) {
    if (checkValidity('editLectureForm')) {
        Request('/api/comment?id='+id, 'PUT', {
                'text': $('#addComment_text').val()
            })
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    location.href = location.href;
                } else {
                    alert(response.message);
                }
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
//_______________댓글__________________________

// 사용자 권한 확인
function checkRole(){
     const token = Cookies.get('Authorization'); // JWT가 저장된 쿠키의 이름을 넣으세요
     if (token) {
         try {
             const decoded = jwt_decode(token);
             if(decoded.auth === "MANAGER"){
                 return true;
             }else{
                 alert('MANAGER 권한이 필요합니다.');
                 return false;
             }
         } catch (error) {
             alert('JWT decoding Error');
             location.href = '/';
         }
     } else {
         alert('로그인이 필요합니다.');
         location.href = '/';
     }
 }