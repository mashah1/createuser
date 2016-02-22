'use strict';

angular.module('traqtionApp')
    .controller('SpecStatusDetailController', function ($scope, $rootScope, $stateParams, entity, SpecStatus) {
        $scope.specStatus = entity;
        $scope.load = function (id) {
            SpecStatus.get({id: id}, function(result) {
                $scope.specStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:specStatusUpdate', function(event, result) {
            $scope.specStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
