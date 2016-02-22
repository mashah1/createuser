'use strict';

angular.module('traqtionApp')
    .factory('UserCategorySearch', function ($resource) {
        return $resource('api/_search/userCategorys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
