'use strict';

angular.module('traqtionApp')
    .controller('BusinessRelationshipDetailController', function ($scope, $rootScope, $stateParams, entity, BusinessRelationship, Business, BusinessRelationshipType) {
        $scope.businessRelationship = entity;
        $scope.load = function (id) {
            BusinessRelationship.get({id: id}, function(result) {
                $scope.businessRelationship = result;
            });
        };
        var unsubscribe = $rootScope.$on('traqtionApp:businessRelationshipUpdate', function(event, result) {
            $scope.businessRelationship = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
