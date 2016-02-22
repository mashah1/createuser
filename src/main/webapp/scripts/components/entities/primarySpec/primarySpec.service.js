'use strict';

angular.module('traqtionApp')
    .factory('PrimarySpec', function ($resource, DateUtils) {
        return $resource('api/primarySpecs/:id', {}, {
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
