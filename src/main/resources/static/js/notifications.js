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
  const emptyAll = document.getElementById('empty-all');
  const noNew = document.getElementById('no-new');
  const noRead = document.getElementById('no-read');
  const markAllBtn = document.getElementById('mark-all-read-btn');

  function toggle(el, show) {
    if (!el) return;
    el.style.display = show ? '' : 'none';
  }

  function syncStates() {
    const unreadCount = unreadList ? unreadList.querySelectorAll('.notification-card').length : 0;
    const readCount = readList ? readList.querySelectorAll('.notification-card').length : 0;

    toggle(unreadList, unreadCount > 0);
    toggle(readList, readCount > 0);
    toggle(noNew, unreadCount === 0);
    toggle(noRead, readCount === 0);

    const allEmpty = unreadCount === 0 && readCount === 0;
    toggle(emptyAll, allEmpty);

    if (markAllBtn) {
      if (unreadCount === 0) {
        markAllBtn.setAttribute('disabled', 'true');
        markAllBtn.textContent = 'No unread';
      } else {
        markAllBtn.removeAttribute('disabled');
        markAllBtn.textContent = 'Mark all as read';
      }
    }
  }

  syncStates();

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
          if (unreadCardsLeft === 0) {
            syncStates();
          }
        })
        .catch(err => {
          console.error('Failed to mark as read', err);
          alert('Could not mark notification as read. Please try again.');
        });
    });
  });

  // Delete notification
  document.querySelectorAll('.delete-btn').forEach(btn => {
    btn.addEventListener('click', function () {
      const id = this.getAttribute('data-id');
      if (!id) return;
      const url = `/notifications/delete/${id}`;
      fetch(url, {
        method: 'POST',
        headers: Object.assign({ 'Accept': 'application/json' }, csrfToken ? { [csrfHeader]: csrfToken } : {}),
        credentials: 'same-origin'
      }).then(handleJsonResponse)
        .then(() => {
          const card = document.querySelector(`.notification-card[data-id='${id}']`);
          if (card) {
            const parentList = card.parentElement;
            card.remove();
            if (parentList && parentList.classList.contains('notification-list')) {
              syncStates();
            }
          }
        })
        .catch(err => {
          console.error('Failed to delete notification', err);
          alert('Could not delete notification. Please try again.');
        });
    });
  });

  // Mark all as read
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
          syncStates();
        })
        .catch(err => {
          console.error('Failed to mark all as read', err);
          alert('Could not mark all notifications as read. Please try again.');
          markAllBtn.removeAttribute('disabled');
        });
    });
  }
});
