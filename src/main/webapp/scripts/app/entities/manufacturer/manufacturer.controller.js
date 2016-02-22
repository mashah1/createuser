'use strict';

angular.module('traqtionApp')
    .controller('ManufacturerController', function ($scope, $state, Manufacturer, ManufacturerSearch) {

        $scope.manufacturers = [];
        $scope.loadAll = function() {
            Manufacturer.query(function(result) {
               $scope.manufacturers = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ManufacturerSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.manufacturers = result;
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
            $scope.manufacturer = {
                id: null
            };
        };
    });
