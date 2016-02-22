'use strict';

angular.module('traqtionApp')
    .controller('SpecStatusController', function ($scope, $state, SpecStatus, SpecStatusSearch) {

        $scope.specStatuss = [];
        $scope.loadAll = function() {
            SpecStatus.query(function(result) {
               $scope.specStatuss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SpecStatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.specStatuss = result;
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
            $scope.specStatus = {
                name: null,
                id: null
            };
        };
    });
