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
            .catch(function(error) {
                alert(error.responseText);
            });
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
        .catch(function(error) {
            alert(error.responseText);
        });
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
//                var phone = teacher.phone;
//                $('#editTeacher_phone1').val(phone.substring(0, 3));
//                $('#editTeacher_phone2').val(phone.substring(3, 7));
//                $('#editTeacher_phone3').val(phone.substring(7, 11));
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
                console.log(error);
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
//                'phone': $('#editTeacher_phone1').val() + $('#editTeacher_phone2').val() + $('#editTeacher_phone3').val(),
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
function getLectures(page,category,option,desc) {
    Request('/api/lectures', 'GET', {
            page: page,
            category: category,
            option: option,
            desc: desc
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
                    var teacher_name;
                    if(lecture.teacher != null){
                        teacher_name = lecture.teacher.name;
                    }else{
                        teacher_name = "미정"
                    }
                    html += '<td>' + teacher_name + '</td>';
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
                // 강의 정보 작성
                let lecture = response.data;
                $('#lectureTitle').text(lecture.title);
                $('#lecturePrice').text(lecture.price);
                $('#lectureCategory').text(lecture.category);
                var teacher_name;
                if(lecture.teacher != null){
                    teacher_name = lecture.teacher.name;
                }else{
                    teacher_name = "미정"
                }
                $('#lectureTeacher').text(teacher_name);
                $('#lectureDate').text(getFormattedDate(lecture.regist));
                $('#lectureIntroduction').text(lecture.introduction);
                // 댓글과 대댓글 작성
                setComments(lecture.comments);
                // 사용자가 좋아요를 눌렀는지 확인하는 기능
                if(user_id != null){
                    checkLike();
                }
                // 좋아요 갯수
                document.getElementById('likeCnt').textContent = lecture.likes;
                // 강의 수정을 위한 교사 목록 불러오는 기능
                if(lecture.teacher != null){
                    teacher_id = lecture.teacher.id;
                }else{
                    teacher_id = null
                }
                getTeacherList(teacher_id);
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
function getTeacherList(id){
    Request('/api/teachers', 'GET', null)
        .then(function(response) {
            if (response.status === 200) {
                var teacherSelect = $('#editLecture_teacher');
                teacherSelect.empty(); // 기존 옵션들을 모두 지움
                teacherSelect.append($('<option value="">미정</option>'));
                $.each(response.data, function(index, teacher) {
                    var option = $('<option></option>')
                        .attr('value', teacher.id)
                        .text(teacher.name);
                    if (id != null && teacher.id === id) {
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
// 수정 모달 열기 함수
function callEditLectureModal() {
    if (checkRole()) {
        // 강의 수정 모달에 값 설정
        $('#editLecture_title').val($('#lectureTitle').text());
        $('#editLecture_price').val($('#lecturePrice').text());
        $('#editLecture_introduction').val($('#lectureIntroduction').text());
        $('#editLecture_category').val($('#lectureCategory').text());
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
//댓글 불러오기
function getComments(id) {
    Request('/api/comment', 'GET', {
          id: id,
        })
        .then(function(response) {
            if (response.status === 200) {
                setComments(response.data);
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert('강의 정보를 불러오는 중 오류가 발생했습니다.');
            console.log(error);
        });
}
// 댓글 목록 정렬하는 코드
function setComments(comments){
    let commentsList = $('#commentsList');
    commentsList.empty(); // 기존 댓글 초기화

    comments.forEach(comment => {
        let isCommentOwner = (user_id && comment.user_id === user_id);
        let commentHtml = `
            <div class="comment" id="comment-${comment.id}">
                <div class="hstack gap-1">
                    <div class="comment-text">
                        <p><strong>${comment.user_email}</strong></p>
                    </div>
                    <div class="comment-text">
                        <p>${comment.regist}</p>
                    </div>
                    <button class="btn btn-sm float-end ms-auto" onclick="replyComment(${comment.id}, this)">
                        <i class="bi bi-reply"></i>
                    </button>

                    <button class="btn btn-sm float-end ${isCommentOwner ? '' : 'd-none'}" onclick="editComment(${comment.id},this)">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm float-end ${isCommentOwner ? '' : 'd-none'}" onclick="deleteComment(${comment.id})">
                        <i class="bi bi-trash"></i>
                    </button>

                </div>
                <div class="comment-text" id="comment-text-${comment.id}">
                    <p>${comment.text}</p>
                </div>
                <div class="reply-input d-none" id="reply-input-${comment.id}">
                    <textarea class="form-control" rows="3"></textarea>
                    <button class="btn btn-sm btn-primary mt-2" onclick="addReply(${comment.id}, this)">완료</button>
                    <button class="btn btn-sm btn-secondary mt-2" onclick="cancelReply(${comment.id}, this)">취소</button>
                </div>
                <div class="replies">
                    ${comment.replies.map(reply => {
                        let isReplyOwner = (user_id && reply.user_id === user_id);
                        return `
                            <div class="reply" id="reply-${reply.id}">
                                <div class="hstack gap-1">
                                    <div class="reply-text">
                                        <p><strong>${reply.user_email}</strong></p>
                                    </div>
                                    <div class="reply-text">
                                        <p>${reply.regist}</p>
                                    </div>

                                    <button class="btn btn-sm float-end ms-auto ${isReplyOwner ? '' : 'd-none'}" onclick="editReply(${reply.id}, this)">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="btn btn-sm float-end ${isReplyOwner ? '' : 'd-none'}" onclick="deleteReply(${reply.id})">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </div>
                                <div class="reply-text" id="reply-text-${reply.id}">
                                    <p>${reply.text}</p>
                                </div>
                            </div>
                        `;
                    }).join('')}
                </div>
            </div>
        `;
        commentsList.append(commentHtml);
    });
}
// 댓글 추가
function addComment() {
    var text = $('#addComment_text').val();
    if (text != "") {
        Request('/api/comment', 'POST', {
                'lecture_id': lecture_id,
                'user_id': user_id,
                'text': text
            })
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    $('#addComment_text').val("")
                    getComments(lecture_id);
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
    if (confirm('삭제하시겠습니까?')) {
        Request('/api/comment?id=' + id, 'DELETE', null)
        .then(function(response) {
            if (response.status === 200) {
                alert(response.message);
                getComments(lecture_id);
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert(error.responseText);
        });
    }
}
// 댓글 수정 작업을 위한 자바스크립트 작업
function editComment(commentId, button) {
    let commentTextElement = $(`#comment-text-${commentId}`);
    let currentText = commentTextElement.find('p').text();

    // 변경된 부분: 수정 버튼을 취소 버튼으로 변경
    $(button).attr('onclick', `cancelEditComment(${commentId}, '${currentText}', this)`);
    $(button).html(`<i class="bi bi-x-circle"></i>`); // 아이콘 변경 (선택 사항)

    commentTextElement.html(`
        <textarea class="form-control" rows="3">${currentText}</textarea>
        <button class="btn btn-sm btn-primary mt-2" onclick="saveComment(${commentId}, this)">저장</button>
    `);
}
// 댓글 수정
function saveComment(id, button) {
    Request('/api/comment', 'PUT', {
        'id' : id,
        'text': $(`#comment-text-${id} textarea`).val()
    })
    .then(function(response) {
        if (response.status === 200) {
            alert(response.message);
            getComments(lecture_id);
        } else {
            alert(response.message);
        }
    })
    .catch(function(error) {
        alert(error.responseText);
    });
}
// 댓글 수정 취소
function cancelEditComment(commentId, originalText, button) {
    let commentTextElement = $(`#comment-text-${commentId}`);
    commentTextElement.html(`<p>${originalText}</p>`);
    // 변경된 부분: 취소 버튼을 수정 버튼으로 변경
    $(button).attr('onclick', `editComment(${commentId}, this)`);
    $(button).html(`<i class="bi bi-pencil"></i>`); // 아이콘 변경 (선택 사항)
}

//_______________대댓글__________________________
// 대댓글 작성 입력창 보이기
function replyComment(commentId, button) {
    let replyInputElement = $(`#reply-input-${commentId}`);
    replyInputElement.removeClass('d-none');
}
// 대댓글 작성 취소
function cancelReply(commentId, button) {
    let replyInputElement = $(`#reply-input-${commentId}`);
    replyInputElement.addClass('d-none');
}
// 대댓글 추가
function addReply(comment_id, button) {
    let text = $(`#reply-input-${comment_id} textarea`).val();
    if (text != "") {
        Request('/api/reply', 'POST', {
                'comment_id': comment_id,
                'user_id': user_id,
                'text': text
            })
            .then(function(response) {
                if (response.status === 200) {
                    alert(response.message);
                    getComments(lecture_id);
                } else {
                    alert(response.message);
                }
            })
            .catch(function(error) {
                alert(error.responseText);
            });
    }
}
// 대댓글 삭제
function deleteReply(id) {
    if (confirm('삭제하시겠습니까?')) {
        Request('/api/reply?id=' + id, 'DELETE', null)
        .then(function(response) {
            if (response.status === 200) {
                alert(response.message);
                getComments(lecture_id);
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert(error.responseText);
        });
    }
}
// 대댓글 수정 작업을 위한 자바스크립트 작업
function editReply(replyId, button) {
    let replyTextElement = $(`#reply-text-${replyId}`);
    let currentText = replyTextElement.find('p').text();

    // 변경된 부분: 수정 버튼을 취소 버튼으로 변경
    $(button).attr('onclick', `cancelEditReply(${replyId}, '${currentText}', this)`);
    $(button).html(`<i class="bi bi-x-circle"></i>`); // 아이콘 변경 (선택 사항)

    replyTextElement.html(`
        <textarea class="form-control" rows="3">${currentText}</textarea>
        <button class="btn btn-sm btn-primary mt-2" onclick="saveReplyEdit(${replyId}, this)">저장</button>
    `);
}
// 대댓글 수정 취소
function cancelEditReply(replyId, originalText, button) {
    let replyTextElement = $(`#reply-text-${replyId}`);
    replyTextElement.html(`<p>${originalText}</p>`);
    // 변경된 부분: 취소 버튼을 수정 버튼으로 변경
    $(button).attr('onclick', `editReply(${replyId}, this)`);
    $(button).html(`<i class="bi bi-pencil"></i>`); // 아이콘 변경 (선택 사항)
}
// 대댓글 수정 저장
function saveReplyEdit(id, button) {
    Request('/api/reply', 'PUT', {
        'id' : id,
        'text': $(`#reply-text-${id} textarea`).val()
    })
    .then(function(response) {
        if (response.status === 200) {
            alert(response.message);
            getComments(lecture_id);
        } else {
            alert(response.message);
        }
    })
    .catch(function(error) {
        alert(error.responseText);
    });
}

//_______________좋아요__________________________
// 대댓글 작성 입력창 보이기
function checkLike() {
    Request('/api/like', 'GET',
        { 'lecture_id': lecture_id, 'user_id': user_id }
        )
        .then(function(response) {
            if (response.status === 200) {
                var heartIcon = document.getElementById('heartIcon');
                if(response.data){
                    heartIcon.classList.remove('bi-heart-fill'); // 색칠된 하트 클래스 제거
                    heartIcon.classList.add('bi-heart'); // 빈 하트 클래스 추가
                } else {
                    heartIcon.classList.remove('bi-heart'); // 빈 하트 클래스 제거
                    heartIcon.classList.add('bi-heart-fill'); // 색칠된 하트 클래스 추가
                }
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert(error.responseText);
        });
}
// 댓글 추가
function setLike() {
    Request('/api/like', 'POST', {
            'lecture_id': lecture_id,
            'user_id': user_id
        })
        .then(function(response) {
            if (response.status === 200) {
                var heartIcon = document.getElementById('heartIcon');
                if (heartIcon.classList.contains('bi-heart-fill')) {
                    heartIcon.classList.remove('bi-heart-fill'); // 색칠된 하트 클래스 제거
                    heartIcon.classList.add('bi-heart'); // 빈 하트 클래스 추가
                } else {
                    heartIcon.classList.remove('bi-heart'); // 빈 하트 클래스 제거
                    heartIcon.classList.add('bi-heart-fill'); // 색칠된 하트 클래스 추가
                }
                document.getElementById('likeCnt').textContent = response.data;
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert(error.responseText);
        });
}