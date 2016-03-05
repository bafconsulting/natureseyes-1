'use strict';

angular.module('natureseyesApp')
    .factory('SocialService', function ($http) {
        var socialService = {};

        socialService.getProviderSetting = function (provider) {
            switch(provider) {
                case 'google': return 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/urlshortener';
                case 'facebook': return 'public_profile,email';
                case 'twitter': return '';
                // jhipster-needle-add-social-button
                default: return 'Provider setting not defined';
            }
        };

        socialService.getProviderURL = function (provider) {
            return 'signin/' + provider;
        };

        socialService.getCSRF = function () {
            var name = "CSRF-TOKEN=";
            var ca = document.cookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') c = c.substring(1);
                if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
            }
            return "";
        };

        return socialService;
    });
