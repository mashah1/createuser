'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('permissions', {
                parent: 'entity',
                url: '/permissionss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.permissions.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/permissions/permissionss.html',
                        controller: 'PermissionsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('permissions');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('permissions.detail', {
                parent: 'entity',
                url: '/permissions/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.permissions.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/permissions/permissions-detail.html',
                        controller: 'PermissionsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('permissions');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Permissions', function($stateParams, Permissions) {
                        return Permissions.get({id : $stateParams.id});
                    }]
                }
            })
            .state('permissions.new', {
                parent: 'permissions',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/permissions/permissions-dialog.html',
                        controller: 'PermissionsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    attribute: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('permissions', null, { reload: true });
                    }, function() {
                        $state.go('permissions');
                    })
                }]
            })
            .state('permissions.edit', {
                parent: 'permissions',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/permissions/permissions-dialog.html',
                        controller: 'PermissionsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Permissions', function(Permissions) {
                                return Permissions.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('permissions', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('permissions.delete', {
                parent: 'permissions',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/permissions/permissions-delete-dialog.html',
                        controller: 'PermissionsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Permissions', function(Permissions) {
                                return Permissions.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('permissions', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
