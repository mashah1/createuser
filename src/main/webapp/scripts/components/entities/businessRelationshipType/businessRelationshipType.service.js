'use strict';

angular.module('traqtionApp')
    .factory('BusinessRelationshipType', function ($resource, DateUtils) {
        return $resource('api/businessRelationshipTypes/:id', {}, {
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
