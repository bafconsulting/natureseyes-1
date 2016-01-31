 'use strict';

angular.module('natureseyesApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-natureseyesApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-natureseyesApp-params')});
                }
                return response;
            }
        };
    });
