'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('businessRelationship', {
                parent: 'entity',
                url: '/businessRelationships',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.businessRelationship.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/businessRelationship/businessRelationships.html',
                        controller: 'BusinessRelationshipController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('businessRelationship');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('businessRelationship.detail', {
                parent: 'entity',
                url: '/businessRelationship/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.businessRelationship.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/businessRelationship/businessRelationship-detail.html',
                        controller: 'BusinessRelationshipDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('businessRelationship');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'BusinessRelationship', function($stateParams, BusinessRelationship) {
                        return BusinessRelationship.get({id : $stateParams.id});
                    }]
                }
            })
            .state('businessRelationship.new', {
                parent: 'businessRelationship',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessRelationship/businessRelationship-dialog.html',
                        controller: 'BusinessRelationshipDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('businessRelationship', null, { reload: true });
                    }, function() {
                        $state.go('businessRelationship');
                    })
                }]
            })
            .state('businessRelationship.edit', {
                parent: 'businessRelationship',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessRelationship/businessRelationship-dialog.html',
                        controller: 'BusinessRelationshipDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BusinessRelationship', function(BusinessRelationship) {
                                return BusinessRelationship.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('businessRelationship', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('businessRelationship.delete', {
                parent: 'businessRelationship',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/businessRelationship/businessRelationship-delete-dialog.html',
                        controller: 'BusinessRelationshipDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BusinessRelationship', function(BusinessRelationship) {
                                return BusinessRelationship.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('businessRelationship', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
