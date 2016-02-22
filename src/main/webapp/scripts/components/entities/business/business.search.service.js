'use strict';

angular.module('traqtionApp')
    .factory('BusinessSearch', function ($resource) {
        return $resource('api/_search/businesss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
