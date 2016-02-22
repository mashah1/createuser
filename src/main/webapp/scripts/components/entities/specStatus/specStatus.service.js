'use strict';

angular.module('traqtionApp')
    .factory('SpecStatus', function ($resource, DateUtils) {
        return $resource('api/specStatuss/:id', {}, {
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
