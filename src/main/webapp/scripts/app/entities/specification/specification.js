'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('specification', {
                parent: 'entity',
                url: '/specifications',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.specification.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/specification/specifications.html',
                        controller: 'SpecificationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('specification');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('specification.detail', {
                parent: 'entity',
                url: '/specification/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.specification.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/specification/specification-detail.html',
                        controller: 'SpecificationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('specification');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Specification', function($stateParams, Specification) {
                        return Specification.get({id : $stateParams.id});
                    }]
                }
            })
            .state('specification.new', {
                parent: 'specification',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specification/specification-dialog.html',
                        controller: 'SpecificationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    attribute: null,
                                    attribute1: null,
                                    version: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('specification', null, { reload: true });
                    }, function() {
                        $state.go('specification');
                    })
                }]
            })
            .state('specification.edit', {
                parent: 'specification',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specification/specification-dialog.html',
                        controller: 'SpecificationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Specification', function(Specification) {
                                return Specification.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('specification', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('specification.delete', {
                parent: 'specification',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specification/specification-delete-dialog.html',
                        controller: 'SpecificationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Specification', function(Specification) {
                                return Specification.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('specification', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
