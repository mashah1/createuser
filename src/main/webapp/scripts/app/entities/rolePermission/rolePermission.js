'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('rolePermission', {
                parent: 'entity',
                url: '/rolePermissions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.rolePermission.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/rolePermission/rolePermissions.html',
                        controller: 'RolePermissionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rolePermission');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('rolePermission.detail', {
                parent: 'entity',
                url: '/rolePermission/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.rolePermission.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/rolePermission/rolePermission-detail.html',
                        controller: 'RolePermissionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('rolePermission');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'RolePermission', function($stateParams, RolePermission) {
                        return RolePermission.get({id : $stateParams.id});
                    }]
                }
            })
            .state('rolePermission.new', {
                parent: 'rolePermission',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/rolePermission/rolePermission-dialog.html',
                        controller: 'RolePermissionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('rolePermission', null, { reload: true });
                    }, function() {
                        $state.go('rolePermission');
                    })
                }]
            })
            .state('rolePermission.edit', {
                parent: 'rolePermission',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/rolePermission/rolePermission-dialog.html',
                        controller: 'RolePermissionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['RolePermission', function(RolePermission) {
                                return RolePermission.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('rolePermission', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('rolePermission.delete', {
                parent: 'rolePermission',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/rolePermission/rolePermission-delete-dialog.html',
                        controller: 'RolePermissionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['RolePermission', function(RolePermission) {
                                return RolePermission.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('rolePermission', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
