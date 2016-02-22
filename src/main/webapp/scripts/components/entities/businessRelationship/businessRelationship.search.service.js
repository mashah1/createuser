'use strict';

angular.module('traqtionApp')
    .factory('BusinessRelationshipSearch', function ($resource) {
        return $resource('api/_search/businessRelationships/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
