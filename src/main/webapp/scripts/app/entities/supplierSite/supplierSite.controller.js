'use strict';

angular.module('traqtionApp')
    .controller('SupplierSiteController', function ($scope, $state, SupplierSite, SupplierSiteSearch) {

        $scope.supplierSites = [];
        $scope.loadAll = function() {
            SupplierSite.query(function(result) {
               $scope.supplierSites = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SupplierSiteSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.supplierSites = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.supplierSite = {
                attribute: null,
                attribute1: null,
                id: null
            };
        };
    });
