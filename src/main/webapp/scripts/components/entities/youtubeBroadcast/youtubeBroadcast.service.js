'use strict';

angular.module('natureseyesApp')
    .factory('YoutubeBroadcast', function ($resource, DateUtils) {
        return $resource('api/youtubeBroadcasts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.scheduledStartTime = DateUtils.convertDateTimeFromServer(data.scheduledStartTime);
                    data.scheduledEndTime = DateUtils.convertDateTimeFromServer(data.scheduledEndTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
