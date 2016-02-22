'use strict';

angular.module('traqtionApp')
    .factory('ClientSearch', function ($resource) {
        return $resource('api/_search/clients/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
