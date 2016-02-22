'use strict';

angular.module('traqtionApp')
    .factory('SpecificationSearch', function ($resource) {
        return $resource('api/_search/specifications/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
