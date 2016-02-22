'use strict';

angular.module('traqtionApp').controller('UserCategoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserCategory', 'Category', 'UserBusinessRole',
        function($scope, $stateParams, $uibModalInstance, $q, entity, UserCategory, Category, UserBusinessRole) {

        $scope.userCategory = entity;
        $scope.categorys = Category.query({filter: 'usercategory-is-null'});
        $q.all([$scope.userCategory.$promise, $scope.categorys.$promise]).then(function() {
            if (!$scope.userCategory.category || !$scope.userCategory.category.id) {
                return $q.reject();
            }
            return Category.get({id : $scope.userCategory.category.id}).$promise;
        }).then(function(category) {
            $scope.categorys.push(category);
        });
        $scope.userbusinessroles = UserBusinessRole.query({filter: 'usercategory-is-null'});
        $q.all([$scope.userCategory.$promise, $scope.userbusinessroles.$promise]).then(function() {
            if (!$scope.userCategory.userBusinessRole || !$scope.userCategory.userBusinessRole.id) {
                return $q.reject();
            }
            return UserBusinessRole.get({id : $scope.userCategory.userBusinessRole.id}).$promise;
        }).then(function(userBusinessRole) {
            $scope.userbusinessroles.push(userBusinessRole);
        });
        $scope.load = function(id) {
            UserCategory.get({id : id}, function(result) {
                $scope.userCategory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('traqtionApp:userCategoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userCategory.id != null) {
                UserCategory.update($scope.userCategory, onSaveSuccess, onSaveError);
            } else {
                UserCategory.save($scope.userCategory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
