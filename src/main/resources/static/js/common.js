function maxLengthCheck(object) {
    if (object.value.length > object.maxLength) {
        object.value = object.value.slice(0, object.maxLength); // 최대 길이를 초과한 부분을 잘라냅니다.
    } else if (!/^\d+$/.test(object.value)) {
        object.value = object.value.replace(/[^\d]/g, ''); // 숫자 이외의 입력을 제거합니다.
    }
}
 // ajax로 요청 보내는 함수
 function Request(url, type, data) {
   var ajaxOptions = {
     url: url,
     type: type
   };
   if (type === 'GET') {
     ajaxOptions.data = data;
     ajaxOptions.dataType = 'json';
   } else {
     ajaxOptions.contentType = "application/json";
     ajaxOptions.data = JSON.stringify(data);
   }
   return $.ajax(ajaxOptions);
 }


function goBack() { window.history.back(); }

// 날짜 형식 정리하는 자바스크립트
function getFormattedDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return year + "-" + month + "-" + day;
}

// 페이지네이션 세팅하는 기능
function setPagination(paging) {
  var paginationHtml = '';
  paginationHtml += '<li class="page-item ' + (!paging.hasPrevious ? 'disabled' : '') + '">';
  paginationHtml += '<a class="page-link" href="javascript:void(0)" data-page="' + (paging.number - 1) + '">Previous</a>';
  paginationHtml += '</li>';

  for (var i = 0; i < paging.totalPages; i++) {
      if (i >= paging.number - 5 && i <= paging.number + 5) {
          paginationHtml += '<li class="page-item ' + (i === paging.number ? 'active' : '') + '">';
          paginationHtml += '<a class="page-link" href="javascript:void(0)" data-page="' + i + '">' + i + '</a>';
          paginationHtml += '</li>';
      }
  }

  paginationHtml += '<li class="page-item ' + (!paging.hasNext ? 'disabled' : '') + '">';
  paginationHtml += '<a class="page-link" href="javascript:void(0)" data-page="' + (paging.number + 1) + '">Next</a>';
  paginationHtml += '</li>';

  document.getElementById('pagination').innerHTML = paginationHtml;

  // 페이지네이션 버튼 기능
  const page_elements = document.getElementsByClassName("page-link");
      Array.from(page_elements).forEach(function(element) {
      element.addEventListener('click', function() {
          document.getElementById('page').value = this.dataset.page;
          console.log(this.dataset.page);
          document.getElementById('searchForm').submit();
      });
  });
}

// Validation 확인하는 함수
function checkValidity(id) {
    let form = document.getElementById(id);
    if (form.checkValidity() === false) {
        event.preventDefault();
        event.stopPropagation();
        form.classList.add('was-validated');
        return false;
    }
    form.classList.add('was-validated');
    return true;
}

