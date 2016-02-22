'use strict';

angular.module('traqtionApp').controller('BusinessRelationshipDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BusinessRelationship', 'Business', 'BusinessRelationshipType',
        function($scope, $stateParams, $uibModalInstance, $q, entity, BusinessRelationship, Business, BusinessRelationshipType) {

        $scope.businessRelationship = entity;
        $scope.relatedtos = Business.query({filter: 'businessrelationship-is-null'});
        $q.all([$scope.businessRelationship.$promise, $scope.relatedtos.$promise]).then(function() {
            if (!$scope.businessRelationship.relatedto || !$scope.businessRelationship.relatedto.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.businessRelationship.relatedto.id}).$promise;
        }).then(function(relatedto) {
            $scope.relatedtos.push(relatedto);
        });
        $scope.businessrelationshiptypes = BusinessRelationshipType.query({filter: 'businessrelationship-is-null'});
        $q.all([$scope.businessRelationship.$promise, $scope.businessrelationshiptypes.$promise]).then(function() {
            if (!$scope.businessRelationship.businessRelationshipType || !$scope.businessRelationship.businessRelationshipType.id) {
                return $q.reject();
            }
            return BusinessRelationshipType.get({id : $scope.businessRelationship.businessRelationshipType.id}).$promise;
        }).then(function(businessRelationshipType) {
            $scope.businessrelationshiptypes.push(businessRelationshipType);
        });
        $scope.relateds = Business.query({filter: 'businessrelationship-is-null'});
        $q.all([$scope.businessRelationship.$promise, $scope.relateds.$promise]).then(function() {
            if (!$scope.businessRelationship.related || !$scope.businessRelationship.related.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.businessRelationship.related.id}).$promise;
        }).then(function(related) {
            $scope.relateds.push(related);
        });
        $scope.load = function(id) {
            BusinessRelationship.get({id : id}, function(result) {
                $scope.businessRelationship = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:businessRelationshipUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.businessRelationship.id != null) {
                BusinessRelationship.update($scope.businessRelationship, onSaveSuccess, onSaveError);
            } else {
                BusinessRelationship.save($scope.businessRelationship, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
