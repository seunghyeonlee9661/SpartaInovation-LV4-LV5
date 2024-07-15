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

//_______________장바구니_______________________

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