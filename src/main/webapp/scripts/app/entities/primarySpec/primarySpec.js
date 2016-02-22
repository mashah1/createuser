'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('primarySpec', {
                parent: 'entity',
                url: '/primarySpecs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.primarySpec.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/primarySpec/primarySpecs.html',
                        controller: 'PrimarySpecController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('primarySpec');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('primarySpec.detail', {
                parent: 'entity',
                url: '/primarySpec/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.primarySpec.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/primarySpec/primarySpec-detail.html',
                        controller: 'PrimarySpecDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('primarySpec');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PrimarySpec', function($stateParams, PrimarySpec) {
                        return PrimarySpec.get({id : $stateParams.id});
                    }]
                }
            })
            .state('primarySpec.new', {
                parent: 'primarySpec',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/primarySpec/primarySpec-dialog.html',
                        controller: 'PrimarySpecDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    attribute: null,
                                    attribute1: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('primarySpec', null, { reload: true });
                    }, function() {
                        $state.go('primarySpec');
                    })
                }]
            })
            .state('primarySpec.edit', {
                parent: 'primarySpec',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/primarySpec/primarySpec-dialog.html',
                        controller: 'PrimarySpecDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PrimarySpec', function(PrimarySpec) {
                                return PrimarySpec.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('primarySpec', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('primarySpec.delete', {
                parent: 'primarySpec',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/primarySpec/primarySpec-delete-dialog.html',
                        controller: 'PrimarySpecDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PrimarySpec', function(PrimarySpec) {
                                return PrimarySpec.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('primarySpec', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
