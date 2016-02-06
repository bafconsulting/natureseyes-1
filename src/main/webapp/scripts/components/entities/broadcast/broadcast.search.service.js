'use strict';

angular.module('natureseyesApp')
    .factory('BroadcastSearch', function ($resource) {
        return $resource('api/_search/broadcasts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
