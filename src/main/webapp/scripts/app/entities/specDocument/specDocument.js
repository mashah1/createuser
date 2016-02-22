'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('specDocument', {
                parent: 'entity',
                url: '/specDocuments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.specDocument.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/specDocument/specDocuments.html',
                        controller: 'SpecDocumentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('specDocument');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('specDocument.detail', {
                parent: 'entity',
                url: '/specDocument/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.specDocument.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/specDocument/specDocument-detail.html',
                        controller: 'SpecDocumentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('specDocument');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SpecDocument', function($stateParams, SpecDocument) {
                        return SpecDocument.get({id : $stateParams.id});
                    }]
                }
            })
            .state('specDocument.new', {
                parent: 'specDocument',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specDocument/specDocument-dialog.html',
                        controller: 'SpecDocumentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    path: null,
                                    type: null,
                                    attribute1: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('specDocument', null, { reload: true });
                    }, function() {
                        $state.go('specDocument');
                    })
                }]
            })
            .state('specDocument.edit', {
                parent: 'specDocument',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specDocument/specDocument-dialog.html',
                        controller: 'SpecDocumentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SpecDocument', function(SpecDocument) {
                                return SpecDocument.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('specDocument', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('specDocument.delete', {
                parent: 'specDocument',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/specDocument/specDocument-delete-dialog.html',
                        controller: 'SpecDocumentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SpecDocument', function(SpecDocument) {
                                return SpecDocument.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('specDocument', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
