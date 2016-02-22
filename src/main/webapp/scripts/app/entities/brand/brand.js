'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('brand', {
                parent: 'entity',
                url: '/brands',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.brand.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/brand/brands.html',
                        controller: 'BrandController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('brand');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('brand.detail', {
                parent: 'entity',
                url: '/brand/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.brand.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/brand/brand-detail.html',
                        controller: 'BrandDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('brand');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Brand', function($stateParams, Brand) {
                        return Brand.get({id : $stateParams.id});
                    }]
                }
            })
            .state('brand.new', {
                parent: 'brand',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/brand/brand-dialog.html',
                        controller: 'BrandDialogController',
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
                        $state.go('brand', null, { reload: true });
                    }, function() {
                        $state.go('brand');
                    })
                }]
            })
            .state('brand.edit', {
                parent: 'brand',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/brand/brand-dialog.html',
                        controller: 'BrandDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Brand', function(Brand) {
                                return Brand.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('brand', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('brand.delete', {
                parent: 'brand',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/brand/brand-delete-dialog.html',
                        controller: 'BrandDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Brand', function(Brand) {
                                return Brand.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('brand', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
