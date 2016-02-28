'use strict';

angular.module('natureseyesApp')
    .controller('YoutubeBroadcastController', function ($scope, $state, YoutubeBroadcast, YoutubeBroadcastSearch, ParseLinks) {

        $scope.youtubeBroadcasts = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            YoutubeBroadcast.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.youtubeBroadcasts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            YoutubeBroadcastSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.youtubeBroadcasts = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.youtubeBroadcast = {
                title: null,
                description: null,
                privacyStatus: null,
                scheduledStartTime: null,
                scheduledEndTime: null,
                id: null
            };
        };
    });
