'use strict';

angular.module('traqtionApp')
    .factory('UserBusinessRole', function ($resource, DateUtils) {
        return $resource('api/userBusinessRoles/:id', {}, {
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
