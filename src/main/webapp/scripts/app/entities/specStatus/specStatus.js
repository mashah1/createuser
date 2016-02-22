'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('specStatus', {
                parent: 'entity',
                url: '/specStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.specStatus.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/specStatus/specStatuss.html',
                        controller: 'SpecStatusController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('specStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('specStatus.detail', {
                parent: 'entity',
                url: '/specStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.specStatus.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/specStatus/specStatus-detail.html',
                        controller: 'SpecStatusDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('specStatus');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SpecStatus', function($stateParams, SpecStatus) {
                        return SpecStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('specStatus.new', {
                parent: 'specStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specStatus/specStatus-dialog.html',
                        controller: 'SpecStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('specStatus', null, { reload: true });
                    }, function() {
                        $state.go('specStatus');
                    })
                }]
            })
            .state('specStatus.edit', {
                parent: 'specStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specStatus/specStatus-dialog.html',
                        controller: 'SpecStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SpecStatus', function(SpecStatus) {
                                return SpecStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('specStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('specStatus.delete', {
                parent: 'specStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specStatus/specStatus-delete-dialog.html',
                        controller: 'SpecStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SpecStatus', function(SpecStatus) {
                                return SpecStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('specStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
