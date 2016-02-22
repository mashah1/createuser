'use strict';

angular.module('traqtionApp')
    .controller('SpecDocumentController', function ($scope, $state, SpecDocument, SpecDocumentSearch) {

        $scope.specDocuments = [];
        $scope.loadAll = function() {
            SpecDocument.query(function(result) {
               $scope.specDocuments = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            SpecDocumentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.specDocuments = result;
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
            $scope.specDocument = {
                name: null,
                description: null,
                path: null,
                type: null,
                attribute1: null,
                id: null
            };
        };
    });
