'use strict';

angular.module('traqtionApp')
    .factory('UserBusinessRoleSearch', function ($resource) {
        return $resource('api/_search/userBusinessRoles/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
