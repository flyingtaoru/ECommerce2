app.controller('brandController', function ($scope,$controller,brandService) {

    $controller('baseController',{$scope:$scope});//继承 共享$scope

    //查询品牌列表
    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };


    // 发送分页查询请求
    $scope.findByPage = function (page, size) {
        brandService.findByPage(page, size).success(function (response) {
            $scope.list=response.rows;
            $scope.paginationConf.totalItems=response.total;
        });
    };

    // 保存和修改
    $scope.save=function () {
        brandService.add($scope.entity).success(function (response) {
            if (response.flag) {
                $scope.reloadList();
            } else {
                alert("添加失败");
            }
        })
    };

    // 查询一个
    $scope.findOne = function (id) {
        brandService.findOne(id).success(function (data) {
            $scope.entity=data;
        })
    };


    // 删除
    $scope.delete=function () {
        if ($scope.selectIds.length>0) {
            if (confirm("确定删除?")) {
                brandService.delete($scope.selectIds).success(function (data) {
                    if (data.flag) {
                        $scope.reloadList();
                    } else {
                        alert("删除失败");
                    }
                })
            }
        } else {
            alert("数据异常");
        }
    };


    $scope.searchEntity={};    // 初始化
    // 模糊查询
    $scope.search=function (page, size) {
        brandService.search(page, size,$scope.searchEntity).success(function (data) {
            $scope.list=data.rows;
            $scope.paginationConf.totalItems=data.total;
        })
    };
});