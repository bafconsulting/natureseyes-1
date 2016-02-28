'use strict';

angular.module('natureseyesApp')
    .factory('YoutubeBroadcastSearch', function ($resource) {
        return $resource('api/_search/youtubeBroadcasts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
