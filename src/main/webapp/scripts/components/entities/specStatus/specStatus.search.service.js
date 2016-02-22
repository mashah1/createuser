'use strict';

angular.module('traqtionApp')
    .factory('SpecStatusSearch', function ($resource) {
        return $resource('api/_search/specStatuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
