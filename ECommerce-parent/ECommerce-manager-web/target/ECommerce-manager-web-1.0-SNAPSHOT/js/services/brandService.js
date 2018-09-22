app.service("brandService",function ($http) {
    this.findAll=function () {
        return $http.get("../brand/findAll");
    };

    this.findByPage=function (page, size) {
        return $http.get('../brand/findByPage?page=' + page + '&size=' + size);
    };

    this.add=function (entity) {
        var methodName = "save";
        if (entity.id != null) {
            methodName = "update";
        }
        return $http.post("../brand/"+methodName,entity);
    };

    this.findOne=function (id) {
        return $http.get("../brand/findById?id="+id);
    };

    this.delete=function (selectIds) {
        return $http.get("../brand/delete?ids=" + selectIds);
    };

    this.search=function (page,size,searchEntity) {
        return $http.post("../brand/search?page="+page+"&size="+size,searchEntity );
    };

    this.selectOptionList=function () {
        return $http.get("../brand/selectOptionList");
    };
});