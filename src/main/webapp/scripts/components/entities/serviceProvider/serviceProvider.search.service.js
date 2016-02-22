'use strict';

angular.module('traqtionApp')
    .factory('ServiceProviderSearch', function ($resource) {
        return $resource('api/_search/serviceProviders/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
