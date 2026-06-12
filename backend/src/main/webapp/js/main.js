function getCurrentUser() {
    try {
        return JSON.parse(sessionStorage.getItem('currentUser'));
    } catch (e) {
        return null;
    }
}

function setCurrentUser(user) {
    if (user) {
        sessionStorage.setItem('currentUser', JSON.stringify(user));
    } else {
        sessionStorage.removeItem('currentUser');
    }
}

function checkAuth() {
    var cachedUser = getCurrentUser();
    if (cachedUser) {
        updateNavForAuth(true);
    }

    return API.user.getCurrent().then(function(res) {
        if (res.code === 200 && res.data) {
            setCurrentUser(res.data);
            updateNavForAuth(true);
            return res.data;
        } else {
            setCurrentUser(null);
            updateNavForAuth(false);
            return null;
        }
    }).catch(function() {
        if (cachedUser) {
            updateNavForAuth(true);
            return cachedUser;
        }
        setCurrentUser(null);
        updateNavForAuth(false);
        return null;
    });
}

function updateNavForAuth(loggedIn) {
    var navRight = document.querySelector('.nav-right');
    if (!navRight) return;

    var createBtn = navRight.querySelector('a[href="create-activity.html"]');
    var loginBtn = navRight.querySelector('a.nav-login');
    var registerBtn = navRight.querySelector('a.nav-register');
    var userAvatar = navRight.querySelector('.user-avatar');
    var mapBtn = navRight.querySelector('a[href="map.html"]');

    if (loggedIn) {
        if (loginBtn) loginBtn.style.display = 'none';
        if (registerBtn) registerBtn.style.display = 'none';
        if (createBtn) createBtn.style.display = '';
        if (userAvatar) userAvatar.style.display = '';
        if (mapBtn) mapBtn.style.display = '';
    } else {
        if (loginBtn) loginBtn.style.display = '';
        if (registerBtn) registerBtn.style.display = '';
        if (createBtn) createBtn.style.display = 'none';
        if (userAvatar) userAvatar.style.display = 'none';
        if (mapBtn) mapBtn.style.display = 'none';
    }
}

function requireAuth() {
    var user = getCurrentUser();
    if (!user) {
        window.location.href = 'login.html?redirect=' + encodeURIComponent(window.location.href);
        return null;
    }
    return user;
}

function formatDateTime(dateStr) {
    if (!dateStr) return '';
    var d = new Date(dateStr);
    var weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
    var month = d.getMonth() + 1;
    var day = d.getDate();
    var hours = String(d.getHours()).padStart(2, '0');
    var mins = String(d.getMinutes()).padStart(2, '0');
    return month + '月' + day + '日 ' + weekdays[d.getDay()] + ' ' + hours + ':' + mins;
}

function formatDateRange(startTime, endTime) {
    if (!startTime || !endTime) return '';
    var s = new Date(startTime);
    var e = new Date(endTime);
    var sh = String(s.getHours()).padStart(2, '0');
    var sm = String(s.getMinutes()).padStart(2, '0');
    var eh = String(e.getHours()).padStart(2, '0');
    var em = String(e.getMinutes()).padStart(2, '0');
    return formatDateTime(startTime) + ' - ' + sh + ':' + sm + ' - ' + eh + ':' + em;
}

var SPORT_LABELS = {
    'basketball': '篮球',
    'badminton': '羽毛球',
    'pingpong': '乒乓球',
    'football': '足球',
    'other': '其他'
};

function sportLabel(type) {
    if (!type) return '未知';
    if (type.indexOf(',') !== -1) {
        return type.split(',').map(function(t) {
            return SPORT_LABELS[t.trim()] || t.trim();
        }).join('、');
    }
    return SPORT_LABELS[type] || type;
}

function activityCardHtml(a) {
    var venueName = a.venueName || '未知场地';
    var organizerName = a.organizerName || '未知';
    var current = a.currentParticipants || 0;
    var max = a.maxParticipants || 0;
    var coverImg = a.coverImage || 'https://picsum.photos/seed/activity' + a.id + '/600/400';
    var isFull = current >= max;
    var badgeHtml = '';
    if (isFull) {
        badgeHtml = '<span class="activity-badge">已满</span>';
    } else if (max - current <= 2) {
        badgeHtml = '<span class="activity-badge" style="background: var(--airbnb-orange);">即将满员</span>';
    }

    // 活动状态遮罩
    var statusOverlay = '';
    var status = a.status || 'upcoming'; // 默认待开始
    var statusReason = a.rejectReason || a.offlineReason || '';
    
    if (status === 'pending_review') {
        statusOverlay = '<div class="activity-status-overlay pending-review">待审核</div>';
    } else if (status === 'in_progress') {
        statusOverlay = '<div class="activity-status-overlay in-progress">进行中</div>';
    } else if (status === 'rejected') {
        statusOverlay = '<div class="activity-status-overlay rejected">' +
            '<div>已驳回</div>' +
            (statusReason ? '<div class="activity-status-reason">' + statusReason + '</div>' : '') +
        '</div>';
    } else if (status === 'offline') {
        statusOverlay = '<div class="activity-status-overlay offline">' +
            '<div>已下架</div>' +
            (statusReason ? '<div class="activity-status-reason">' + statusReason + '</div>' : '') +
        '</div>';
    }
    // 待开始状态不需要遮罩

    return '<a href="activity-detail.html?id=' + a.id + '" class="card activity-card">' +
        '<div class="activity-image">' +
            '<img src="' + coverImg + '" alt="' + a.title + '" style="width: 100%; height: 100%; object-fit: cover;">' +
            badgeHtml +
            statusOverlay +
        '</div>' +
        '<div class="activity-info">' +
            '<div class="activity-time">' +
                '<i class="fa fa-calendar"></i>' +
                '<span>' + formatDateRange(a.startTime, a.endTime) + '</span>' +
            '</div>' +
            '<h3 class="activity-title">' + a.title + '</h3>' +
            '<div class="activity-location">' +
                '<i class="fa fa-map-marker"></i>' +
                '<span>' + venueName + '</span>' +
            '</div>' +
            '<div class="activity-meta">' +
                '<div class="activity-participants">' +
                    '<i class="fa fa-users"></i>' +
                    '<span>' + current + '/' + max + '人</span>' +
                '</div>' +
                '<div class="activity-organizer">组织者：' + organizerName + '</div>' +
            '</div>' +
            '<div class="activity-actions">' +
                '<button class="btn-primary" onclick="event.preventDefault(); joinActivity(' + a.id + ', this)"' + (isFull ? ' disabled' : '') + '>' +
                    (isFull ? '已满员' : '立即加入') +
                '</button>' +
                '<button class="btn-secondary" onclick="event.preventDefault(); shareActivity(' + a.id + ')">' +
                    '<i class="fa fa-share-alt"></i>' +
                '</button>' +
            '</div>' +
        '</div>' +
    '</a>';
}

function joinActivity(id, btn) {
    API.activity.join(id).then(function(res) {
        if (res.code === 200) {
            alert('加入成功！');
            location.reload();
        } else if (res.code === 401) {
            window.location.href = 'login.html?redirect=' + encodeURIComponent(window.location.href);
        } else {
            alert(res.message || '加入失败');
        }
    });
}

function shareActivity(id) {
    alert('分享功能开发中');
}

function showToast(msg, type) {
    var toast = document.createElement('div');
    toast.className = 'toast toast-' + (type || 'info');
    toast.textContent = msg;
    toast.style.cssText = 'position:fixed;top:20px;left:50%;transform:translateX(-50%);padding:12px 24px;border-radius:8px;color:white;z-index:9999;font-size:14px;' +
        (type === 'error' ? 'background:#ef4444;' : type === 'success' ? 'background:#10b981;' : 'background:#3b82f6;');
    document.body.appendChild(toast);
    setTimeout(function() {
        toast.remove();
    }, 3000);
}

document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
});
