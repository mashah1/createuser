'use strict';

angular.module('traqtionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('supplierSite', {
                parent: 'entity',
                url: '/supplierSites',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.supplierSite.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/supplierSite/supplierSites.html',
                        controller: 'SupplierSiteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supplierSite');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('supplierSite.detail', {
                parent: 'entity',
                url: '/supplierSite/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'traqtionApp.supplierSite.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/supplierSite/supplierSite-detail.html',
                        controller: 'SupplierSiteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('supplierSite');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SupplierSite', function($stateParams, SupplierSite) {
                        return SupplierSite.get({id : $stateParams.id});
                    }]
                }
            })
            .state('supplierSite.new', {
                parent: 'supplierSite',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/supplierSite/supplierSite-dialog.html',
                        controller: 'SupplierSiteDialogController',
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
                        $state.go('supplierSite', null, { reload: true });
                    }, function() {
                        $state.go('supplierSite');
                    })
                }]
            })
            .state('supplierSite.edit', {
                parent: 'supplierSite',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/supplierSite/supplierSite-dialog.html',
                        controller: 'SupplierSiteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SupplierSite', function(SupplierSite) {
                                return SupplierSite.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('supplierSite', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('supplierSite.delete', {
                parent: 'supplierSite',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/supplierSite/supplierSite-delete-dialog.html',
                        controller: 'SupplierSiteDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SupplierSite', function(SupplierSite) {
                                return SupplierSite.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('supplierSite', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
