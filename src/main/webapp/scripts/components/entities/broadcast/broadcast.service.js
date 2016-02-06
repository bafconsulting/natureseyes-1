'use strict';

angular.module('natureseyesApp')
    .factory('Broadcast', function ($resource, DateUtils) {
        return $resource('api/broadcasts/:id', {}, {
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
