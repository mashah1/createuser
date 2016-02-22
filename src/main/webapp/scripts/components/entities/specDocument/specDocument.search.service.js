'use strict';

angular.module('traqtionApp')
    .factory('SpecDocumentSearch', function ($resource) {
        return $resource('api/_search/specDocuments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
