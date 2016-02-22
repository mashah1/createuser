'use strict';

angular.module('traqtionApp')
    .factory('BusinessRelationshipTypeSearch', function ($resource) {
        return $resource('api/_search/businessRelationshipTypes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
