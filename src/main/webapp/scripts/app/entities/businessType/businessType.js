'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('businessType', {
                parent: 'entity',
                url: '/businessTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.businessType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/businessType/businessTypes.html',
                        controller: 'BusinessTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('businessType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('businessType.detail', {
                parent: 'entity',
                url: '/businessType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.businessType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/businessType/businessType-detail.html',
                        controller: 'BusinessTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('businessType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'BusinessType', function($stateParams, BusinessType) {
                        return BusinessType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('businessType.new', {
                parent: 'businessType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessType/businessType-dialog.html',
                        controller: 'BusinessTypeDialogController',
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
                        $state.go('businessType', null, { reload: true });
                    }, function() {
                        $state.go('businessType');
                    })
                }]
            })
            .state('businessType.edit', {
                parent: 'businessType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessType/businessType-dialog.html',
                        controller: 'BusinessTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BusinessType', function(BusinessType) {
                                return BusinessType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('businessType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('businessType.delete', {
                parent: 'businessType',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessType/businessType-delete-dialog.html',
                        controller: 'BusinessTypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BusinessType', function(BusinessType) {
                                return BusinessType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('businessType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
