//_______________제품_______________________

// 제품 추가
function addProduct() {
    if (checkValidity('addProductForm')) {
        Request('/api/product', 'POST', {
                'name': $('#addProduct_name').val(),
                'price': $('#addProduct_price').val(),
                'count': $('#addProduct_count').val(),
                'introduction': $('#addProduct_introduction').val(),
                'category': $('#addProduct_category').val()
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

// 제품 목록 불러오기
function getProducts(page,option,desc) {
    Request('/api/products', 'GET', {
            page: page,
            option: option,
            desc: desc
        })
        .then(function(response) {
            if (response.status === 200) {
                let products = response.data;
                setProductList(products.content);
                setPagination(products);
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            console.log(error)
            alert(error.responseText);
        });
}

// 제품 목록 데이터 정렬하기
function setProductList(products) {
    const $productList = $('#product-list');
    $productList.empty();

    products.forEach((product, index) => {
        if (index % 5 === 0) {
            $productList.append('<div class="row"></div>');
        }
        const $row = $productList.find('.row').last();
        const $card = $(`
            <div class="col mb-4">
                <a href="/product/${product.id}" class="card-link">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">${product.name}</h5>
                            <p class="card-text">가격: ${product.price}</p>
                            <p class="card-text">수량: ${product.count}</p>
                            <span class="badge text-bg-primary">${product.category}</span>
                        </div>
                    </div>
                </a>
            </div>
        `);

        if (product.img) {
            $card.find('.card-body').prepend(`
                <img src="${product.img}" class="card-img-top" alt="${product.name}">
            `);
        } else {
            $card.find('.card-body').prepend(`
                <div class="card-img-top text-center">
                    <i class="bi bi-image" style="font-size: 48px;"></i>
                </div>
            `);
        }

        $row.append($card);
    });
}

// 제품 정보 불러오기
function getProduct() {
    Request('/api/product', 'GET', {
          id: product_id,
        })
        .then(function(response) {
            if (response.status === 200) {
                // 강의 정보 작성
                let product = response.data;
                $('#productName').text(product.name);
                $('#productCategory').text(product.category);
                $('#productPrice').text(product.price);
                $('#productCount').text(product.count);
                $('#productIntroduction').text(product.introduction);
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert(error.responseText);
        });
}

//_______________장바구니_______________________

//장바구니 추가
function addCart() {
    const input = document.getElementById('quantity-input');
    const quantity = parseInt(input.value);
    if (isNaN(quantity) || quantity < 0) {
        alert('올바른 수량을 입력해주세요');
        return;
    }
    Request('/api/cart', 'POST', {
        'user_id': user_id,
        'product_id': product_id,
        'count': quantity,
    })
    .then(function(response) {
        if (response.status === 200) {
            if(confirm("장바구니로 이동하시겠습니까?")){
                    location.href = "/cart";
        	}
            alert(response.message);
        } else {
            alert(response.message);
        }
    })
    .catch(function(error) {
        alert(error.responseText);
    });
}

// 장바구니 목록 불러오기
function getCart() {
    Request('/api/cart', 'GET', {
            id: user_id,
        })
        .then(function(response) {
            if (response.status === 200) {
                let carts = response.data;
                setCartList(carts);
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            console.log(error)
            alert(error.responseText);
        });
}

// 장바구니 내용 출력하는 함수
function setCartList(carts) {
    const cartTableBody = document.getElementById('cartTableBody');
    cartTableBody.innerHTML = ''; // 기존 목록 비우기
    let totalPrice = 0;

    // 한국식 원화 표기법 사용
    const formatter = new Intl.NumberFormat('ko-KR', {
        style: 'currency',
        currency: 'KRW'
    });

    carts.forEach(cart => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td><span class="badge text-bg-primary">${cart.product.category}</span></td>
            <td>${cart.product.name}</td>
            <td id="price-${cart.id}">${formatter.format(cart.product.price)} 원</td>
            <td>
                <div class="input-group">
                    <button class="btn btn-outline-secondary btn-sm" type="button" onclick="adjustQuantity(${cart.id}, ${cart.count - 1})"><i class="bi bi-dash"></i></button>
                    <input type="text" class="form-control quantity-input" id="quantity-${cart.id}" value="${cart.count}" data-item-id="${cart.id}" onchange="handleInputChange(${cart.id})">
                    <button class="btn btn-outline-secondary btn-sm" type="button" onclick="adjustQuantity(${cart.id}, ${cart.count + 1})"><i class="bi bi-plus"></i></button>
                </div>
            </td>
            <td id="subtotal-${cart.id}">${formatter.format(cart.product.price * cart.count)} 원</td>
            <td>
                <button class="btn btn-danger btn-sm" onclick="deleteItem(${cart.id})"><i class="bi bi-trash"></i></button>
            </td>
        `;
        cartTableBody.appendChild(row);
        totalPrice += cart.product.price * cart.count;
    });

    const totalPriceElement = document.getElementById('totalPrice');
    totalPriceElement.textContent = formatter.format(totalPrice);
}

// 입력 받아오는 함수
function handleInputChange(itemId) {
    const inputElement = document.getElementById(`quantity-${itemId}`);
    const newCount = parseInt(inputElement.value);

    if (!isNaN(newCount)) adjustQuantity(itemId, newCount);
}

// 장바구니 변경 적용하는 함수
function adjustQuantity(itemId, newCount) {
    Request('/api/cart', 'PUT', {
        'id' : itemId,
        'count': newCount
    })
    .then(function(response) {
        if (response.status === 200) {
            getCart();
        } else {
            alert(response.message);
        }
    })
    .catch(function(error) {
        alert(error.responseText);
    });
}

// 장바구니 항목 삭제
function deleteItem(itemId) {
    if (confirm('장바구니에서 삭제하시겠습니까?')) {
        Request('/api/cart?id=' + itemId, 'DELETE', null)
        .then(function(response) {
            if (response.status === 200) {
                alert(response.message);
                getCart();
            } else {
                alert(response.message);
            }
        })
        .catch(function(error) {
            alert(error.responseText);
        });
    }
}