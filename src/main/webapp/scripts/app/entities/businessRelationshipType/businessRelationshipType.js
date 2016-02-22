'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('businessRelationshipType', {
                parent: 'entity',
                url: '/businessRelationshipTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.businessRelationshipType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/businessRelationshipType/businessRelationshipTypes.html',
                        controller: 'BusinessRelationshipTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('businessRelationshipType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('businessRelationshipType.detail', {
                parent: 'entity',
                url: '/businessRelationshipType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.businessRelationshipType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/businessRelationshipType/businessRelationshipType-detail.html',
                        controller: 'BusinessRelationshipTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('businessRelationshipType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'BusinessRelationshipType', function($stateParams, BusinessRelationshipType) {
                        return BusinessRelationshipType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('businessRelationshipType.new', {
                parent: 'businessRelationshipType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessRelationshipType/businessRelationshipType-dialog.html',
                        controller: 'BusinessRelationshipTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('businessRelationshipType', null, { reload: true });
                    }, function() {
                        $state.go('businessRelationshipType');
                    })
                }]
            })
            .state('businessRelationshipType.edit', {
                parent: 'businessRelationshipType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessRelationshipType/businessRelationshipType-dialog.html',
                        controller: 'BusinessRelationshipTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BusinessRelationshipType', function(BusinessRelationshipType) {
                                return BusinessRelationshipType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('businessRelationshipType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('businessRelationshipType.delete', {
                parent: 'businessRelationshipType',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessRelationshipType/businessRelationshipType-delete-dialog.html',
                        controller: 'BusinessRelationshipTypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BusinessRelationshipType', function(BusinessRelationshipType) {
                                return BusinessRelationshipType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('businessRelationshipType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
