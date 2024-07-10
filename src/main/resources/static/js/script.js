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
            alert(response);
            location.href = "/login";
        })
        .catch(function(error) {
            alert(error);
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
                console.log(res);
                console.log(status);
                console.log(xhr);
                location.href = '/';
            })
            .fail(function (xhr, textStatus, errorThrown) {
                console.log('statusCode: ' + xhr.status);
                console.log(xhr);
                console.log(textStatus);
                console.log(errorThrown);
                location.href = '/login?error'
            });
    }
}
// 로그아웃
function logoutRequest() {
  if(confirm("로그아웃 하시겠습니까?")){
      Cookies.remove('Authorization', { path: '/' });
      location.href = location.href;
  }
}

//_______________강사_______________________
// 강사 추가
function addTeacher() {
    let form = document.getElementById('addTeacherForm');
    if (checkValidity(form)) {
        Request('/api/teacher', 'POST', {
                'name': $('#addTeacher_name').val(),
                'year': $('#addTeacher_year').val(),
                'company': $('#addTeacher_company').val(),
                'phone': $('#addTeacher_phone1').val() + $('#addTeacher_phone2').val() + $('#addTeacher_phone3').val(),
                'introduction': $('#addTeacher_introduction').val()
            })
            .then(function(response) {
                alert('강사가 추가되었습니다.');
                location.href = location.href;
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
// 강사 목록 불러오기
function getTeachers() {
    Request('/api/teachers', 'GET', null)
        .then(function(response) {
            console.log(response)
            var html = '';
            for (var i = 0; i < response.length; i++) {
                var teacher = response[i];
                html += '<li><a href="/teacher/' + teacher.id + '">' + teacher.name + '</a></li>';
            }
            $('.vertical-menu').html(html);

            var selectHtml = '<option value="">...</option>';
            for (var i = 0; i < response.length; i++) {
                var teacher = response[i];
                selectHtml += '<option value="' + teacher.id + '">' + teacher.name + '</option>';
            }
            $('#addLecture_teacher').html(selectHtml);
        })
       .catch(function(error) {
            alert('강사 목록을 불러오는 중 오류가 발생했습니다.');
            console.log(error);
       });
}
// 강사 정보 불러오기
function getTeacher(id) {
    Request('/api/teacher', 'GET', {
          id: id,
        })
        .then(function(response) {
            $('#teacher-name').text(response.name);
            $('#teacher-year').text(response.year);
            $('#teacher-company').text(response.company);
            $('#teacher-phone').text(response.phone);
            $('#teacher-introduction').text(response.introduction);

            $('#editTeacher_name').val(response.name);
            $('#editTeacher_year').val(response.year);
            $('#editTeacher_company').val(response.company);
            var phone = response.phone;
            $('#editTeacher_phone1').val(phone.substring(0, 3));
            $('#editTeacher_phone2').val(phone.substring(3, 7));
            $('#editTeacher_phone3').val(phone.substring(7, 11));
            $('#editTeacher_introduction').val(response.introduction);

            // 강의 목록 업데이트
            var lectureList = $('#lecture-list');
            lectureList.empty();  // 기존 목록 제거

            response.lectures.forEach(function(lecture) {
                var lectureItem = $('<li></li>');
                var lectureLink = $('<a></a>').attr('href', '/lecture/' + lecture.id).addClass('lecture-link');
                var lectureTitle = $('<span></span>').addClass('lecture-title').text(lecture.title);
                var lectureCategory = $('<span></span>').addClass('lecture-category').text(lecture.category);

                lectureLink.append(lectureTitle).append(lectureCategory);
                lectureItem.append(lectureLink);
                lectureList.append(lectureItem);
            });
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
                    alert('강사가 삭제되었습니다.');
                    window.location.href = '/backoffice/main';
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
                alert('강사 정보가 수정되었습니다.');
                location.href = location.href;
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
//_______________강의_______________________
// 강의 추가
function addLecture() {
    let form = document.getElementById('addLectureForm');
    if (checkValidity(form)) {
        Request('/api/lecture', 'POST', {
                'title': $('#addLecture_title').val(),
                'price': $('#addLecture_price').val(),
                'introduction': $('#addLecture_introduction').val(),
                'category': $('#addLecture_category').val(),
                'teacher_id': $('#addLecture_teacher').val()
            })
            .then(function(response) {
                alert('강의가 추가되었습니다.');
                location.href = location.href;
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
            console.log(response)
            var html = '';
            for (var i = 0; i < response.content.length; i++) {
                var lecture = response.content[i];
                html += '<tr>';
                html += '<td><a href="/lecture/' + lecture.id + '">' + lecture.title + '</a></td>';
                html += '<td>' + lecture.price + '</td>';
                html += '<td>' + lecture.teachername + '</td>';
                html += '<td>' + lecture.category + '</td>';
                html += '<td>' + getFormattedDate(lecture.regist) + '</td>';
                html += '</tr>';
            }
            document.querySelector('tbody').innerHTML = html;
            setPagination(response);
        })
        .catch(function(error) {
            alert('강의 목록을 불러오는 중 오류가 발생했습니다.');
            console.log(error);
        });
}
// 강의 목록 불러오기
function getLecture(id) {
    Request('/api/lecture', 'GET', {
          id: id,
        })
        .then(function(response) {
           $('#lectureTitle').text(response.title);
           $('#lecturePrice').text(response.price);
           $('#lectureCategory').text(response.category);
           $('#lectureTeacher').text(response.teachername);
           $('#lectureDate').text(getFormattedDate(response.regist));
           $('#lectureIntroduction').text(response.introduction);

            $('#editLecture_title').val(response.title);
             $('#editLecture_price').val(response.price);
             $('#editLecture_introduction').val(response.introduction);
             $('#editLecture_category').val(response.category);
             getTeacherList(response.teacherid)
        })
        .catch(function(error) {
            alert('강의 정보를 불러오는 중 오류가 발생했습니다.');
            console.log(error);
        });
}
// 강의 수정을 위한 강사 목록 불러오기
function getTeacherList(id) {
    Request('/api/teachers', 'GET', null)
        .then(function(response) {
            var teacherSelect = $('#editLecture_teacher');
            teacherSelect.empty(); // 기존 옵션들을 모두 지움
            $.each(response, function(index, teacher) {
                var option = $('<option></option>')
                    .attr('value', teacher.id)
                    .text(teacher.name);
                if (teacher.id === id) {
                    option.attr('selected', 'selected'); // 기본 선택 항목 설정
                }
                teacherSelect.append(option);
            });
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
                  alert('강의가 삭제되었습니다.');
                  window.location.href = '/main';
              })
              .catch(function(error) {
                  alert(error.responseText);
              });
      }
    }
}
// 강의 수정
function editLecture(id) {
    let form = document.getElementById('editLectureForm');
    if (checkValidity(form)) {
        Request('/api/lecture?id='+id, 'PUT', {
                'title': $('#editLecture_title').val(),
                'price': $('#editLecture_price').val(),
                'introduction': $('#editLecture_introduction').val(),
                'category': $('#editLecture_category').val(),
                'teacher_id': $('#editLecture_teacher').val()
            })
            .then(function(response) {
                alert('강의 정보가 수정되었습니다.');
                location.href = location.href;
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
//_______________강사__________________________

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