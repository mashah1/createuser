'use strict';

angular.module('traqtionApp').controller('UserBusinessRoleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserBusinessRole', 'User', 'Role', 'Business',
        function($scope, $stateParams, $uibModalInstance, $q, entity, UserBusinessRole, User, Role, Business) {

        $scope.userBusinessRole = entity;
        $scope.users = User.query();
        $scope.roles = Role.query({filter: 'userbusinessrole-is-null'});
        $q.all([$scope.userBusinessRole.$promise, $scope.roles.$promise]).then(function() {
            if (!$scope.userBusinessRole.role || !$scope.userBusinessRole.role.id) {
                return $q.reject();
            }
            return Role.get({id : $scope.userBusinessRole.role.id}).$promise;
        }).then(function(role) {
            $scope.roles.push(role);
        });
        $scope.businesss = Business.query({filter: 'userbusinessrole-is-null'});
        $q.all([$scope.userBusinessRole.$promise, $scope.businesss.$promise]).then(function() {
            if (!$scope.userBusinessRole.business || !$scope.userBusinessRole.business.id) {
                return $q.reject();
            }
            return Business.get({id : $scope.userBusinessRole.business.id}).$promise;
        }).then(function(business) {
            $scope.businesss.push(business);
        });
        $scope.load = function(id) {
            UserBusinessRole.get({id : id}, function(result) {
                $scope.userBusinessRole = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:userBusinessRoleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userBusinessRole.id != null) {
                UserBusinessRole.update($scope.userBusinessRole, onSaveSuccess, onSaveError);
            } else {
                UserBusinessRole.save($scope.userBusinessRole, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
