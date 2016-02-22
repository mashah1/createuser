'use strict';

angular.module('traqtionApp').controller('BusinessTypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BusinessType', 'RolePermission', 'Business',
        function($scope, $stateParams, $uibModalInstance, $q, entity, BusinessType, RolePermission, Business) {

        $scope.businessType = entity;
        $scope.rolepermissions = RolePermission.query({filter: 'businesstype-is-null'});
        $q.all([$scope.businessType.$promise, $scope.rolepermissions.$promise]).then(function() {
            if (!$scope.businessType.rolePermission || !$scope.businessType.rolePermission.id) {
                return $q.reject();
            }
            return RolePermission.get({id : $scope.businessType.rolePermission.id}).$promise;
        }).then(function(rolePermission) {
            $scope.rolepermissions.push(rolePermission);
        });
        $scope.businesss = Business.query({filter: 'businesstype-is-null'});
        $q.all([$scope.businessType.$promise, $scope.businesss.$promise]).then(function() {
            if (!$scope.businessType.business || !$scope.businessType.business.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.businessType.business.id}).$promise;
        }).then(function(business) {
            $scope.businesss.push(business);
        });
        $scope.load = function(id) {
            BusinessType.get({id : id}, function(result) {
                $scope.businessType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:businessTypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.businessType.id != null) {
                BusinessType.update($scope.businessType, onSaveSuccess, onSaveError);
            } else {
                BusinessType.save($scope.businessType, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
