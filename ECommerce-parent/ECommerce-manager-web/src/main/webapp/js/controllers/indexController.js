app.controller("indexController", function ($scope, loginService) {


    $scope.getName = function () {
        loginService.getName().success(function (data) {
            $scope.name = data.loginName;
        });
    }


});