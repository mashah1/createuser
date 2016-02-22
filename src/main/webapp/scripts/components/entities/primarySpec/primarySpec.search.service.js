'use strict';

angular.module('traqtionApp')
    .factory('PrimarySpecSearch', function ($resource) {
        return $resource('api/_search/primarySpecs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
