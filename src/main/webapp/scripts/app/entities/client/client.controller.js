'use strict';

angular.module('traqtionApp')
    .controller('ClientController', function ($scope, $state, Client, ClientSearch) {

        $scope.clients = [];
        $scope.loadAll = function() {
            Client.query(function(result) {
               $scope.clients = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ClientSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.clients = result;
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
            $scope.client = {
                id: null
            };
        };
    });
