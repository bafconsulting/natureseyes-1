'use strict';

angular.module('natureseyesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('broadcast', {
                parent: 'entity',
                url: '/broadcasts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'natureseyesApp.broadcast.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/broadcast/broadcasts.html',
                        controller: 'BroadcastController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('broadcast');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('broadcast.detail', {
                parent: 'entity',
                url: '/broadcast/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'natureseyesApp.broadcast.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/broadcast/broadcast-detail.html',
                        controller: 'BroadcastDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('broadcast');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Broadcast', function($stateParams, Broadcast) {
                        return Broadcast.get({id : $stateParams.id});
                    }]
                }
            })
            .state('broadcast.new', {
                parent: 'broadcast',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/broadcast/broadcast-dialog.html',
                        controller: 'BroadcastDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('broadcast', null, { reload: true });
                    }, function() {
                        $state.go('broadcast');
                    })
                }]
            })
            .state('broadcast.edit', {
                parent: 'broadcast',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/broadcast/broadcast-dialog.html',
                        controller: 'BroadcastDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Broadcast', function(Broadcast) {
                                return Broadcast.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('broadcast', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('broadcast.delete', {
                parent: 'broadcast',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/broadcast/broadcast-delete-dialog.html',
                        controller: 'BroadcastDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Broadcast', function(Broadcast) {
                                return Broadcast.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('broadcast', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
