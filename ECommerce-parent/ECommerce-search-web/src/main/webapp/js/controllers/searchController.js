app.controller("searchController", function ($scope, $location, searchService) {


    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                buildPageLabel();
            })

    }


    $scope.searchMap = {
        "keywords": "",
        "category": "",
        "brand": "",
        "spec": {},
        "price": "",
        "pageNo": 1,
        "pageSize": 20,
        "sort": "",
        "sortField": ""
    }

    // 添加查询条件
    $scope.addSearchMap = function (key, value) {
        $scope.searchMap.pageNo=1;
        if (key == "brand" || key == "category" || key == "price") {
            $scope.searchMap[key] = value;   //添加品牌或类目
        } else {
            $scope.searchMap.spec[key] = value;   // 添加规格
        }
        $scope.search();
    }

    // 移除查询条件
    $scope.rmSearchMap = function (key) {
        $scope.searchMap.pageNo=1;
        if (key == "brand" || key == "category" || key == "price") {
            $scope.searchMap[key] = "";   // 移除
        } else {
            delete $scope.searchMap.spec[key];  // 删除该规格
        }
        $scope.search();
    }

    // 创建分页条
    buildPageLabel = function () {
        $scope.pageLabel = [];  // 初始化分页拦
        var maxPageNo = $scope.resultMap.totalPages;
        var lastPage = maxPageNo;
        var firstpage = 1;
        $scope.firstDot = true;//前面有点
        $scope.lastDot = true;//后边有点

        if (maxPageNo > 5) {
            if ($scope.searchMap.pageNo <= 3) {
                lastPage = 5;
                $scope.firstDot = false;
            } else if ($scope.searchMap.pageNo >= maxPageNo - 2) {
                firstpage = maxPageNo - 4;
                $scope.lastDot = false;
            } else {
                lastPage = $scope.searchMap.pageNo + 2;
                firstpage = $scope.searchMap.pageNo - 2;
            }
        } else {
            $scope.firstDot = false;//前面无点
            $scope.lastDot = false;//后边无点
        }

        // 循环添加分页栏
        for (var i = firstpage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    }

    // 查询第几页
    $scope.findByPageNo = function (pageNo) {
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = Math.floor(pageNo);
        $scope.search();
    }

    //判断当前页为第一页
    $scope.isTopPage = function () {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    }

    //判断当前页是否未最后一页
    $scope.isEndPage=function(){
        if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
            return true;
        }else{
            return false;
        }
    }

    //设置排序规则
    $scope.sortSearch = function (sortField, sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        $scope.search();
    }

    // 判断关键词是否有品牌
    $scope.keywordsIsBrand = function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            var index = $scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text);
            if (index >= 0) {
                return false;
            }
        }
        return true;
    }


    //加载查询字符串
    $scope.loadkeywords = function () {
        if ($location.search()['keywords'] == null) {
            return;
        }
        $scope.searchMap.keywords = $location.search()['keywords'];
        $scope.search();
    }

});

