'use strict';

angular.module('traqtionApp')
    .controller('SpecDocumentDetailController', function ($scope, $rootScope, $stateParams, entity, SpecDocument) {
        $scope.specDocument = entity;
        $scope.load = function (id) {
            SpecDocument.get({id: id}, function(result) {
                $scope.specDocument = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:specDocumentUpdate', function(event, result) {
            $scope.specDocument = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
