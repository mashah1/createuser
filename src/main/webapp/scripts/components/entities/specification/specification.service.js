'use strict';

angular.module('traqtionApp')
    .factory('Specification', function ($resource, DateUtils) {
        return $resource('api/specifications/:id', {}, {
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
