'use strict';

angular.module('natureseyesApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0 )){
	                $rootScope.$emit('natureseyesApp.httpError', response);
	            }
                return $q.reject(response);
            }
        };
    });