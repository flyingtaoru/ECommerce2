app.controller("contentController",function ($scope,contentService) {
    $controller('baseController',{$scope:$scope});//继承 共享$scope


    $scope.contentList=[];//广告集合
    $scope.findByCategoryId=function (categoryId) {

        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId]=response;
        });

    }

})