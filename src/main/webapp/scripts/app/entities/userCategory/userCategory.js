'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userCategory', {
                parent: 'entity',
                url: '/userCategorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.userCategory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userCategory/userCategorys.html',
                        controller: 'UserCategoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userCategory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('userCategory.detail', {
                parent: 'entity',
                url: '/userCategory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.userCategory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userCategory/userCategory-detail.html',
                        controller: 'UserCategoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('userCategory');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'UserCategory', function($stateParams, UserCategory) {
                        return UserCategory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userCategory.new', {
                parent: 'userCategory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userCategory/userCategory-dialog.html',
                        controller: 'UserCategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userCategory', null, { reload: true });
                    }, function() {
                        $state.go('userCategory');
                    })
                }]
            })
            .state('userCategory.edit', {
                parent: 'userCategory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userCategory/userCategory-dialog.html',
                        controller: 'UserCategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserCategory', function(UserCategory) {
                                return UserCategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userCategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userCategory.delete', {
                parent: 'userCategory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userCategory/userCategory-delete-dialog.html',
                        controller: 'UserCategoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserCategory', function(UserCategory) {
                                return UserCategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userCategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
