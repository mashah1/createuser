'use strict';

angular.module('traqtionApp')
    .factory('SupplierSiteSearch', function ($resource) {
        return $resource('api/_search/supplierSites/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
