'use strict';

angular.module('traqtionApp')
    .factory('Permissions', function ($resource, DateUtils) {
        return $resource('api/permissionss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
