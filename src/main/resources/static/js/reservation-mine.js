const RESERVATION_API_ENDPOINT = '/reservations/mine';

document.addEventListener('DOMContentLoaded', () => {
  requestRead(RESERVATION_API_ENDPOINT)
      .then(render)
      .catch(error => console.error('Error fetching reservations:', error));
});

function render(data) {
  const tableBody = document.getElementById('table-body');
  tableBody.innerHTML = '';

  data.forEach(item => {
    const row = tableBody.insertRow();

    row.insertCell(0).textContent = item.theme;
    row.insertCell(1).textContent = item.date;
    row.insertCell(2).textContent = item.time;
    row.insertCell(3).textContent = item.status;

    // 새 셀을 만들고 취소 버튼을 추가
    if (item.status !== '예약') {
      const cancelCell = row.insertCell(4);
      const cancelButton = document.createElement('button');
      cancelButton.textContent = '취소 '; // 버튼 텍스트 설정
      cancelButton.className = 'btn btn-danger'; // 필요한 경우 CSS 클래스 설정
      cancelButton.onclick = function () {
        requestDeleteWaiting(item.reservationId).then(() => {
          alert('예약 취소가 완료되었습니다.');
          window.location.reload();
        }).catch(error => {
          console.error('Delete failed:', error);
          alert('예약 취소에 실패했습니다.');
        });
      };
      cancelCell.appendChild(cancelButton); // 버튼을 셀에 추가
    } else {
      row.insertCell(4).textContent = ''; // 취소 버튼이 없는 빈 셀 추가
    }
  });
}

function requestRead(endpoint) {
  return fetch(endpoint)
      .then(response => {
        if (response.status === 200) return response.json();
        throw new Error('Read failed');
      });
}

function requestDeleteWaiting(id) {
  const endpoint = '/reservations/' + id;
  return fetch(endpoint, { method: 'DELETE' })
      .then(response => {
        if (response.status === 204) return;
        throw new Error('Delete failed');
      });
}