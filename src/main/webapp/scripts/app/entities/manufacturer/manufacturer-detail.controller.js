'use strict';

angular.module('traqtionApp')
    .controller('ManufacturerDetailController', function ($scope, $rootScope, $stateParams, entity, Manufacturer, Business) {
        $scope.manufacturer = entity;
        $scope.load = function (id) {
            Manufacturer.get({id: id}, function(result) {
                $scope.manufacturer = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:manufacturerUpdate', function(event, result) {
            $scope.manufacturer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
