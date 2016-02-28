'use strict';

angular.module('natureseyesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('youtubeBroadcast', {
                parent: 'entity',
                url: '/youtubeBroadcasts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'natureseyesApp.youtubeBroadcast.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/youtubeBroadcast/youtubeBroadcasts.html',
                        controller: 'YoutubeBroadcastController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('youtubeBroadcast');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('youtubeBroadcast.detail', {
                parent: 'entity',
                url: '/youtubeBroadcast/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'natureseyesApp.youtubeBroadcast.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/youtubeBroadcast/youtubeBroadcast-detail.html',
                        controller: 'YoutubeBroadcastDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('youtubeBroadcast');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'YoutubeBroadcast', function($stateParams, YoutubeBroadcast) {
                        return YoutubeBroadcast.get({id : $stateParams.id});
                    }]
                }
            })
            .state('youtubeBroadcast.new', {
                parent: 'youtubeBroadcast',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/youtubeBroadcast/youtubeBroadcast-dialog.html',
                        controller: 'YoutubeBroadcastDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    description: null,
                                    privacyStatus: null,
                                    scheduledStartTime: null,
                                    scheduledEndTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('youtubeBroadcast', null, { reload: true });
                    }, function() {
                        $state.go('youtubeBroadcast');
                    })
                }]
            })
            .state('youtubeBroadcast.edit', {
                parent: 'youtubeBroadcast',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/youtubeBroadcast/youtubeBroadcast-dialog.html',
                        controller: 'YoutubeBroadcastDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['YoutubeBroadcast', function(YoutubeBroadcast) {
                                return YoutubeBroadcast.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('youtubeBroadcast', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('youtubeBroadcast.delete', {
                parent: 'youtubeBroadcast',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/youtubeBroadcast/youtubeBroadcast-delete-dialog.html',
                        controller: 'YoutubeBroadcastDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['YoutubeBroadcast', function(YoutubeBroadcast) {
                                return YoutubeBroadcast.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('youtubeBroadcast', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
