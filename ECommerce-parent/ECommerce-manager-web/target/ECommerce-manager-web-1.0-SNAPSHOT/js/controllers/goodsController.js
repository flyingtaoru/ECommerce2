//控制层
app.controller('goodsController', function ($scope, $controller, $location,uploadService, goodsService, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function () {
        var id = $location.search()["id"];  // 获取地址中的id属性
        if (id == null) {
            return;
        }
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                editor.html($scope.entity.goodsDesc.introduction);

                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages);

                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);

                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

                var itemList = $scope.entity.itemList;

                for(var i =0 ; i < itemList.length; i ++ ) {
                    $scope.entity.itemList[i].spec = JSON.parse(itemList[i].spec);
                }

            }

        );
    }


    $scope.checkAttributeValue=function(specName,optionName) {
        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,"attributeName",specName);
        if (object == null) {
            return false;
        } else {
            if (object.attributeValue.indexOf(optionName) >= 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        $scope.entity.goodsDesc.introduction = editor.html();
        if ($scope.entity.goods.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    alert("保存成功");
                    $scope.entity = {};
                    editor.html("");//清空富文本编辑器
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    // 状态
    $scope.statusList=["未审核","已审核","审核通过","关闭"];

    // 封装所有的类目
    $scope.itemCatList=[];
    $scope.findItemCatList=function(){
        itemCatService.findAll().success(function (respones) {
            for (var i =0; i<respones.length;i++){
                $scope.itemCatList[respones[i].id]=respones[i].name;
            }
        })
    }


    // 添加
    $scope.add = function () {
        $scope.entity.goodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert("新增成功");
                    $scope.entity = {};
                    editor.html("");//清空富文本编辑器
                } else {
                    alert(response.message);
                }
            }
        );
    }



    //上传图片
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(
            function (response) {
                if (response.success) {
                    $scope.image_entity.url = response.message;
                } else {
                    alert(response.message);
                }
            }
        );


    }

    $scope.entity = {goodsDesc: {itemImages: [], specificationItems: []}};

    //将当前上传的图片实体存入图片列表
    $scope.add_image_entity = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //移除图片
    $scope.remove_image_entity = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }

    // 查找一级类目
    $scope.findItemCat1List = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.ItemCat1List = response;
        });
    }

    // 查找二级类目
    $scope.$watch("entity.goods.category1Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.ItemCat2List = response;
            $scope.ItemCat3List = {};
        });
    })

    // 查找三级类目
    $scope.$watch("entity.goods.category2Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.ItemCat3List = response;
        });
    })

    // 查找当前模板ID
    $scope.$watch("entity.goods.category3Id", function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(function (response) {
            $scope.entity.goods.typeTemplateId = response.typeId;
        })
    })

    // 查找品牌列表
    $scope.$watch("entity.goods.typeTemplateId", function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
            $scope.typeTemplate = response;//获取类型模板
            $scope.typeTemplate.brandIds =
                JSON.parse($scope.typeTemplate.brandIds);//品牌列表
            if ($location.search()["id"]==null) {  // 判断是否为添加
                $scope.entity.goodsDesc.customAttributeItems =
                    JSON.parse($scope.typeTemplate.customAttributeItems);//扩展属性
            }
        })
        typeTemplateService.findSpecList(newValue).success(function (response) {
            $scope.specList = response; // 通过模板Id查询规格
        })
    })


    $scope.updateSpecAttribute = function ($event, name, value) {

        var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,
            "attributeName", name);

        if (object != null) {  // 已经创建
            if ($event.target.checked) {  // 判断是否勾选
                object.attributeValue.push(value);
            } else {
                var index1 = object.attributeValue.indexOf(value);
                object.attributeValue.splice(index1, 1);  // 移除  移除的位置  移除的个数
            }
            if (object.attributeValue.length == 0) { // 当没有选项的时候删除该规格
                var index2 = $scope.entity.goodsDesc.specificationItems.indexOf(object);
                $scope.entity.goodsDesc.specificationItems.splice(index2, 1);
            }
        } else { // 未创建进行创建规格
            $scope.entity.goodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]});
        }

    }


    //创建 SKU 列表
    $scope.createItemList = function () {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];//初始
        var items=$scope.entity.goodsDesc.specificationItems;
        for(var i = 0;i<items.length;i++) {
            $scope.entity.itemList = fuckingConlumn($scope.entity.itemList,items[i].attributeName
            ,items[i].attributeValue);
        }
    }

    fuckingConlumn=function (list, conlumnName, conlumnValues) {
        var newList =  [];
        for (var i = 0 ; i<list.length; i++) {
            var oldRow= list[i];
            for (var j = 0 ; j< conlumnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));  // 深克隆
                newRow.spec[conlumnName]=conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }


    $scope.updateStatus=function(status){
        goodsService.updateStatus($scope.selectIds,status).success(function (response) {
            if(response.success){
                //重新查询
                $scope.reloadList();//重新加载
            }else{
                alert(response.message);
            }
        });
    }
});
