'use strict';

angular.module('traqtionApp')
    .factory('RolePermissionSearch', function ($resource) {
        return $resource('api/_search/rolePermissions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
