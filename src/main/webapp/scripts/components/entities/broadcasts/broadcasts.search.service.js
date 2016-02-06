'use strict';

angular.module('natureseyesApp')
    .factory('BroadcastsSearch', function ($resource) {
        return $resource('api/_search/broadcastss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
