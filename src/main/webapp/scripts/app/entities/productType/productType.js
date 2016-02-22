'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('productType', {
                parent: 'entity',
                url: '/productTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.productType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productType/productTypes.html',
                        controller: 'ProductTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('productType.detail', {
                parent: 'entity',
                url: '/productType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.productType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productType/productType-detail.html',
                        controller: 'ProductTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ProductType', function($stateParams, ProductType) {
                        return ProductType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('productType.new', {
                parent: 'productType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productType/productType-dialog.html',
                        controller: 'ProductTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('productType', null, { reload: true });
                    }, function() {
                        $state.go('productType');
                    })
                }]
            })
            .state('productType.edit', {
                parent: 'productType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productType/productType-dialog.html',
                        controller: 'ProductTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProductType', function(ProductType) {
                                return ProductType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('productType.delete', {
                parent: 'productType',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productType/productType-delete-dialog.html',
                        controller: 'ProductTypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ProductType', function(ProductType) {
                                return ProductType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
