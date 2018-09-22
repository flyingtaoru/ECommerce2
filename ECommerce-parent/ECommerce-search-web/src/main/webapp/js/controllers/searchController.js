app.controller("searchController", function ($scope, searchService) {




    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
            })
    }


    $scope.searchMap={"keywords":"","category":"","brand":"","spec":{}};

    // 添加查询条件
    $scope.addSearchMap=function (key, value) {
        if (key == "brand" || key == "category"){
            $scope.searchMap[key] = value;   //添加品牌或类目
        } else {
            $scope.searchMap.spec[key]=value;   // 添加规格
        }
        $scope.search()
    }

    // 移除查询条件
    $scope.rmSearchMap=function (key) {
        if (key == "brand" || key == "category"){
            $scope.searchMap[key] = "";   // 移除
        } else {
            delete $scope.searchMap.spec[key];  // 删除该规格
        }
        $scope.search()
    }

})

