'use strict';

angular.module('natureseyesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('broadcasts', {
                parent: 'entity',
                url: '/broadcastss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'natureseyesApp.broadcasts.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/broadcasts/broadcastss.html',
                        controller: 'BroadcastsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('broadcasts');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('broadcasts.detail', {
                parent: 'entity',
                url: '/broadcasts/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'natureseyesApp.broadcasts.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/broadcasts/broadcasts-detail.html',
                        controller: 'BroadcastsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('broadcasts');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Broadcasts', function($stateParams, Broadcasts) {
                        return Broadcasts.get({id : $stateParams.id});
                    }]
                }
            })
            .state('broadcasts.new', {
                parent: 'broadcasts',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/broadcasts/broadcasts-dialog.html',
                        controller: 'BroadcastsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    broadcastName: null,
                                    broadcastDescription: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('broadcasts', null, { reload: true });
                    }, function() {
                        $state.go('broadcasts');
                    })
                }]
            })
            .state('broadcasts.edit', {
                parent: 'broadcasts',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/broadcasts/broadcasts-dialog.html',
                        controller: 'BroadcastsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Broadcasts', function(Broadcasts) {
                                return Broadcasts.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('broadcasts', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('broadcasts.delete', {
                parent: 'broadcasts',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/broadcasts/broadcasts-delete-dialog.html',
                        controller: 'BroadcastsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Broadcasts', function(Broadcasts) {
                                return Broadcasts.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('broadcasts', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
