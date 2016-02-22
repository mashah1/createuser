'use strict';

angular.module('traqtionApp')
    .factory('SpecDocument', function ($resource, DateUtils) {
        return $resource('api/specDocuments/:id', {}, {
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
