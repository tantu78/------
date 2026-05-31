var API_BASE;

(function() {
    if (window.location.protocol === 'file:') {
        document.body.innerHTML = '<div style="display:flex;align-items:center;justify-content:center;height:100vh;font-family:sans-serif;">' +
            '<div style="text-align:center;padding:40px;background:#fff3cd;border:2px solid #ffc107;border-radius:12px;max-width:500px;">' +
            '<h2 style="color:#856404;">无法访问后端 <br>请通过浏览器地址栏输入以下地址访问</h2>' +
            '<p style="font-size:20px;font-weight:700;color:#c0392b;margin:16px 0;">' +
            '<code>http://localhost:8083/backend' + window.location.pathname.replace(/\\/g, '/') + '</code></p>' +
            '<p style="color:#666;font-size:14px;">不要用双击文件的方式打开，必须通过后端服务访问</p></div></div>';
        throw new Error('Must access via http://localhost:8083/backend, not file://');
    }
    API_BASE = '/backend/api';
})();

function request(url, options) {
    var opts = options || {};
    opts.credentials = 'include';
    if (opts.body && typeof opts.body === 'object' && !(opts.body instanceof FormData)) {
        if (opts.isForm) {
            var params = new URLSearchParams();
            Object.keys(opts.body).forEach(function(key) {
                params.append(key, opts.body[key]);
            });
            opts.body = params.toString();
            opts.headers = opts.headers || {};
            opts.headers['Content-Type'] = 'application/x-www-form-urlencoded';
            delete opts.isForm;
        } else {
            opts.headers = opts.headers || {};
            opts.headers['Content-Type'] = 'application/json';
            opts.body = JSON.stringify(opts.body);
        }
    }
    return fetch(API_BASE + url, opts).then(function(res) {
        return res.json();
    });
}

var API = {
    user: {
        register: function(user) {
            return request('/user/register', { method: 'POST', body: user });
        },
        login: function(username, password) {
            var formData = new FormData();
            formData.append('username', username);
            formData.append('password', password);
            return request('/user/login', { method: 'POST', body: formData });
        },
        logout: function() {
            return request('/user/logout', { method: 'POST' });
        },
        getCurrent: function() {
            return request('/user/current');
        },
        update: function(user) {
            return request('/user/update', { method: 'PUT', body: user });
        },
        search: function(keyword) {
            return request('/user/search?keyword=' + encodeURIComponent(keyword));
        }
    },
    activity: {
        create: function(activity) {
            return request('/activity/create', { method: 'POST', body: activity });
        },
        join: function(id) {
            return request('/activity/join/' + id, { method: 'POST' });
        },
        quit: function(id) {
            return request('/activity/quit/' + id, { method: 'POST' });
        },
        list: function(params) {
            var query = [];
            if (params) {
                if (params.sportType) query.push('sportType=' + encodeURIComponent(params.sportType));
                if (params.page !== undefined) query.push('page=' + params.page);
                if (params.size !== undefined) query.push('size=' + params.size);
                if (params.sortBy) query.push('sortBy=' + params.sortBy);
                if (params.direction) query.push('direction=' + params.direction);
            }
            var qs = query.length > 0 ? '?' + query.join('&') : '';
            return request('/activity/list' + qs);
        },
        detail: function(id) {
            return request('/activity/detail/' + id);
        },
        my: function() {
            return request('/activity/my');
        },
        byVenue: function(venueId) {
            return request('/activity/venue/' + venueId);
        },
        recommend: function() {
            return request('/activity/recommend');
        },
        participants: function(id) {
            return request('/activity/participants/' + id);
        }
    },
    venue: {
        list: function(sportType) {
            var qs = sportType ? '?sportType=' + encodeURIComponent(sportType) : '';
            return request('/venue/list' + qs);
        },
        detail: function(id) {
            return request('/venue/detail/' + id);
        },
        updateCoordinates: function(id, mapX, mapY) {
            return request('/venue/coordinates/' + id, {
                method: 'PUT',
                body: { mapX: mapX, mapY: mapY }
            });
        },
        batchCoordinates: function(venues) {
            return request('/venue/batch-coordinates', {
                method: 'POST',
                body: venues
            });
        }
    },
    friend: {
        add: function(friendId) {
            return request('/friend/add?friendId=' + friendId, { method: 'POST' });
        },
        accept: function(id) {
            return request('/friend/accept/' + id, { method: 'POST' });
        },
        reject: function(id) {
            return request('/friend/reject/' + id, { method: 'POST' });
        },
        list: function() {
            return request('/friend/list');
        },
        activities: function() {
            return request('/friend/activities');
        },
        requests: function() {
            return request('/friend/requests');
        }
    },
    notification: {
        list: function() {
            return request('/notification/list');
        },
        unreadCount: function() {
            return request('/notification/unread-count');
        },
        markRead: function(id) {
            return request('/notification/read/' + id, { method: 'POST' });
        },
        markAllRead: function() {
            return request('/notification/read-all', { method: 'POST' });
        }
    },
    evaluation: {
        submit: function(activityId, targetUserId, skillScore, organizingScore, comment) {
            return request('/evaluation/submit', {
                method: 'POST',
                body: {
                    activityId: activityId,
                    targetUserId: targetUserId,
                    skillScore: skillScore,
                    organizingScore: organizingScore,
                    comment: comment || ''
                },
                isForm: true
            });
        },
        getByActivity: function(activityId) {
            return request('/evaluation/activity/' + activityId);
        },
        getByUser: function(userId) {
            return request('/evaluation/user/' + userId);
        },
        getUserScore: function(userId) {
            return request('/evaluation/user-score/' + userId);
        }
    },
    upload: {
        image: function(file) {
            var formData = new FormData();
            formData.append('file', file);
            return request('/upload/image', { method: 'POST', body: formData });
        }
    }
};
