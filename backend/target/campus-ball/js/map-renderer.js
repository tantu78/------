function initCampusMap(containerId, options) {
    var opts = options || {};
    var onClickVenue = opts.onClickVenue || function() {};
    var selectable = opts.selectable !== false;

    var container = document.getElementById(containerId);
    if (!container) return;

    container.style.position = 'relative';
    container.style.display = 'inline-block';

    if (!container.querySelector('img')) {
        var img = document.createElement('img');
        img.src = opts.mapSrc || '学校地图.png';
        img.alt = '校园地图';
        img.style.display = 'block';
        img.style.maxWidth = '100%';
        img.style.maxHeight = opts.maxHeight || '500px';
        container.appendChild(img);
    }

    var img = container.querySelector('img');

    var api = {
        container: container,
        img: img,
        selectedVenueId: null,

        loadVenues: function() {
            return API.venue.list().then(function(res) {
                if (res.code === 200 && res.data) {
                    api.renderMarkers(res.data);
                    return res.data;
                }
                return [];
            });
        },

        renderMarkers: function(venues) {
            container.querySelectorAll('.map-marker, .map-marker-label').forEach(function(el) { el.remove(); });

            venues.forEach(function(v) {
                if (v.mapX == null || v.mapY == null) return;

                var px = v.mapX / 100;
                var py = v.mapY / 100;

                var marker = document.createElement('div');
                marker.className = 'map-marker';
                marker.setAttribute('data-venue-id', v.id);
                marker.style.left = px + '%';
                marker.style.top = py + '%';
                marker.title = v.name;

                if (selectable) {
                    marker.style.cursor = 'pointer';
                    marker.addEventListener('click', function(e) {
                        e.stopPropagation();
                        api.selectVenue(v.id);
                        onClickVenue(v);
                    });
                }

                container.appendChild(marker);

                var label = document.createElement('div');
                label.className = 'map-marker-label';
                label.style.left = px + '%';
                label.style.top = py + '%';
                label.textContent = v.name;
                container.appendChild(label);
            });
        },

        selectVenue: function(venueId) {
            api.selectedVenueId = venueId;
            container.querySelectorAll('.map-marker').forEach(function(m) {
                m.classList.toggle('selected', m.getAttribute('data-venue-id') === String(venueId));
            });
        },

        getSelectedVenueId: function() {
            return api.selectedVenueId;
        },

        resize: function() {
            api.renderMarkers(api._lastVenues || []);
        }
    };

    window.addEventListener('resize', function() {
        api.resize();
    });

    return api;
}
