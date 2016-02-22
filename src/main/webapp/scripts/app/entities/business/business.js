'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('business', {
                parent: 'entity',
                url: '/businesss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.business.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/business/businesss.html',
                        controller: 'BusinessController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('business');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('business.detail', {
                parent: 'entity',
                url: '/business/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.business.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/business/business-detail.html',
                        controller: 'BusinessDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('business');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Business', function($stateParams, Business) {
                        return Business.get({id : $stateParams.id});
                    }]
                }
            })
            .state('business.new', {
                parent: 'business',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/business/business-dialog.html',
                        controller: 'BusinessDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    displayName: null,
                                    address: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('business', null, { reload: true });
                    }, function() {
                        $state.go('business');
                    })
                }]
            })
            .state('business.edit', {
                parent: 'business',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/business/business-dialog.html',
                        controller: 'BusinessDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Business', function(Business) {
                                return Business.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('business', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('business.delete', {
                parent: 'business',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/business/business-delete-dialog.html',
                        controller: 'BusinessDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Business', function(Business) {
                                return Business.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('business', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
