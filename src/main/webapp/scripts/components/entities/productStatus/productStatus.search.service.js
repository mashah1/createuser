'use strict';

angular.module('traqtionApp')
    .factory('ProductStatusSearch', function ($resource) {
        return $resource('api/_search/productStatuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
