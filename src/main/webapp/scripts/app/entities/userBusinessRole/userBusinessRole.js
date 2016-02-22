'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userBusinessRole', {
                parent: 'entity',
                url: '/userBusinessRoles',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.userBusinessRole.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userBusinessRole/userBusinessRoles.html',
                        controller: 'UserBusinessRoleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userBusinessRole');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userBusinessRole.detail', {
                parent: 'entity',
                url: '/userBusinessRole/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.userBusinessRole.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userBusinessRole/userBusinessRole-detail.html',
                        controller: 'UserBusinessRoleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userBusinessRole');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'UserBusinessRole', function($stateParams, UserBusinessRole) {
                        return UserBusinessRole.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userBusinessRole.new', {
                parent: 'userBusinessRole',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userBusinessRole/userBusinessRole-dialog.html',
                        controller: 'UserBusinessRoleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    isActive: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userBusinessRole', null, { reload: true });
                    }, function() {
                        $state.go('userBusinessRole');
                    })
                }]
            })
            .state('userBusinessRole.edit', {
                parent: 'userBusinessRole',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userBusinessRole/userBusinessRole-dialog.html',
                        controller: 'UserBusinessRoleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserBusinessRole', function(UserBusinessRole) {
                                return UserBusinessRole.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userBusinessRole', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userBusinessRole.delete', {
                parent: 'userBusinessRole',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userBusinessRole/userBusinessRole-delete-dialog.html',
                        controller: 'UserBusinessRoleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserBusinessRole', function(UserBusinessRole) {
                                return UserBusinessRole.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userBusinessRole', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
