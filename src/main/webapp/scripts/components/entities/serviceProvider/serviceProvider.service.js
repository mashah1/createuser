'use strict';

angular.module('traqtionApp')
    .factory('ServiceProvider', function ($resource, DateUtils) {
        return $resource('api/serviceProviders/:id', {}, {
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
