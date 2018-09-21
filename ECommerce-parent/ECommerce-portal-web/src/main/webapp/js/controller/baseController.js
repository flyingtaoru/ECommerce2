app.controller("baseController",function ($scope) {
    //分页控件配置currentPage:当前页   totalItems :总记录数  itemsPerPage:每页记录数  perPageOptions :分页选项  onChange:当页码变更后自动触发的方法
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();
        }
    };

    // 调用方法刷新列表
    $scope.reloadList=function () {
        // $scope.findByPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        $scope.selectIds=[];   // 每次刷新都重置该数组
    };

    $scope.selectIds=[];  // 保存勾选的id集合
    $scope.updateSelectIds=function ($event,id) {
        if ($event.target.checked) {  // 判断是否勾选
            $scope.selectIds.push(id);   // 勾选添加至集合
        } else {
            var index = $scope.selectIds.indexOf(id);   // 查找当前id在当前集合中的位置
            $scope.selectIds.splice(index,1);  // 移除  移除的位置  移除的个数
        }
    };


    $scope.jsonToString=function(jsonString,key){
        var json=JSON.parse(jsonString);//将 json 字符串转换为 json 对象
        var value="";
        for(var i=0;i<json.length;i++){
            if(i>0){
                value+=","
            }
            value+=json[i][key];
        }
        return value;
    }


    $scope.searchObjectByKey=function (list, key, keyValue) {
        for (var i = 0; i<list.length;i++) {
            if (list[i][key]==keyValue){
                return list[i];
            }
        }
        return null;
    }



});