'use strict';

angular.module('natureseyesApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


