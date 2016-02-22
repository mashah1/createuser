'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('productStatus', {
                parent: 'entity',
                url: '/productStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.productStatus.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productStatus/productStatuss.html',
                        controller: 'ProductStatusController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('productStatus.detail', {
                parent: 'entity',
                url: '/productStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.productStatus.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/productStatus/productStatus-detail.html',
                        controller: 'ProductStatusDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('productStatus');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ProductStatus', function($stateParams, ProductStatus) {
                        return ProductStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('productStatus.new', {
                parent: 'productStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productStatus/productStatus-dialog.html',
                        controller: 'ProductStatusDialogController',
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
                        $state.go('productStatus', null, { reload: true });
                    }, function() {
                        $state.go('productStatus');
                    })
                }]
            })
            .state('productStatus.edit', {
                parent: 'productStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productStatus/productStatus-dialog.html',
                        controller: 'ProductStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ProductStatus', function(ProductStatus) {
                                return ProductStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('productStatus.delete', {
                parent: 'productStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/productStatus/productStatus-delete-dialog.html',
                        controller: 'ProductStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ProductStatus', function(ProductStatus) {
                                return ProductStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('productStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
