'use strict';

angular.module('traqtionApp')
    .factory('PermissionsSearch', function ($resource) {
        return $resource('api/_search/permissionss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
