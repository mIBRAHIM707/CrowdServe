document.addEventListener('DOMContentLoaded', function () {
  const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
  const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
  const csrfToken = csrfTokenMeta ? csrfTokenMeta.getAttribute('content') : null;
  const csrfHeader = csrfHeaderMeta ? csrfHeaderMeta.getAttribute('content') : 'X-CSRF-TOKEN';

  function handleJsonResponse(res) {
    if (!res.ok) return res.json().then(j => Promise.reject(j));
    return res.json();
  }

  const unreadList = document.getElementById('unread-list');
  const readList = document.getElementById('read-list');

  function moveCardToRead(card) {
    if (!card || !readList) return;
    card.classList.remove('unread');
    card.classList.add('read');
    const badge = card.querySelector('.notification-badge');
    if (badge) badge.remove();
    const btn = card.querySelector('.mark-read-btn');
    if (btn) btn.remove();
    readList.prepend(card);
  }

  // Single notification mark-as-read
  document.querySelectorAll('.mark-read-btn').forEach(btn => {
    btn.addEventListener('click', function (e) {
      const id = this.getAttribute('data-id');
      if (!id) return;
      const url = `/notifications/mark-read/${id}`;
      fetch(url, {
        method: 'POST',
        headers: Object.assign({ 'Accept': 'application/json' }, csrfToken ? { [csrfHeader]: csrfToken } : {}),
        credentials: 'same-origin'
      }).then(handleJsonResponse)
        .then(() => {
          const card = document.querySelector(`.notification-card[data-id='${id}']`);
          moveCardToRead(card);
          const unreadCardsLeft = unreadList ? unreadList.querySelectorAll('.notification-card.unread').length : 0;
          if (markAllBtn && unreadCardsLeft === 0) {
            markAllBtn.setAttribute('disabled', 'true');
            markAllBtn.textContent = 'No unread';
          }
        })
        .catch(err => {
          console.error('Failed to mark as read', err);
          alert('Could not mark notification as read. Please try again.');
        });
    });
  });

  // Mark all as read
  const markAllBtn = document.getElementById('mark-all-read-btn');
  if (markAllBtn) {
    markAllBtn.addEventListener('click', function () {
      const url = '/notifications/mark-all-read';
      if (!unreadList) return;
      markAllBtn.setAttribute('disabled', 'true');
      fetch(url, {
        method: 'POST',
        headers: Object.assign({ 'Accept': 'application/json' }, csrfToken ? { [csrfHeader]: csrfToken } : {}),
        credentials: 'same-origin'
      }).then(handleJsonResponse)
        .then(data => {
          const cards = unreadList.querySelectorAll('.notification-card.unread');
          cards.forEach(card => moveCardToRead(card));
          markAllBtn.textContent = (data && data.marked) ? `Marked ${data.marked} read` : 'Marked read';
        })
        .catch(err => {
          console.error('Failed to mark all as read', err);
          alert('Could not mark all notifications as read. Please try again.');
          markAllBtn.removeAttribute('disabled');
        });
    });
  }
});
