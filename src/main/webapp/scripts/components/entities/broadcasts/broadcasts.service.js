'use strict';

angular.module('natureseyesApp')
    .factory('Broadcasts', function ($resource, DateUtils) {
        return $resource('api/broadcastss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
