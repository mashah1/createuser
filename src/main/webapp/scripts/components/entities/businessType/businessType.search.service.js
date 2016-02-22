'use strict';

angular.module('traqtionApp')
    .factory('BusinessTypeSearch', function ($resource) {
        return $resource('api/_search/businessTypes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
