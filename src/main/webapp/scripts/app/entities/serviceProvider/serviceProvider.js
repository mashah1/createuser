'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('serviceProvider', {
                parent: 'entity',
                url: '/serviceProviders',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.serviceProvider.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/serviceProvider/serviceProviders.html',
                        controller: 'ServiceProviderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('serviceProvider');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('serviceProvider.detail', {
                parent: 'entity',
                url: '/serviceProvider/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.serviceProvider.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/serviceProvider/serviceProvider-detail.html',
                        controller: 'ServiceProviderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('serviceProvider');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ServiceProvider', function($stateParams, ServiceProvider) {
                        return ServiceProvider.get({id : $stateParams.id});
                    }]
                }
            })
            .state('serviceProvider.new', {
                parent: 'serviceProvider',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/serviceProvider/serviceProvider-dialog.html',
                        controller: 'ServiceProviderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('serviceProvider', null, { reload: true });
                    }, function() {
                        $state.go('serviceProvider');
                    })
                }]
            })
            .state('serviceProvider.edit', {
                parent: 'serviceProvider',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/serviceProvider/serviceProvider-dialog.html',
                        controller: 'ServiceProviderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ServiceProvider', function(ServiceProvider) {
                                return ServiceProvider.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('serviceProvider', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('serviceProvider.delete', {
                parent: 'serviceProvider',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/serviceProvider/serviceProvider-delete-dialog.html',
                        controller: 'ServiceProviderDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ServiceProvider', function(ServiceProvider) {
                                return ServiceProvider.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('serviceProvider', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
