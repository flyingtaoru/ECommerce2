 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	
	//注册用户
	$scope.reg=function(){

		//比较两次输入的密码是否一致
		if ($scope.password != $scope.entity.password) {
			alert("密码不一致");
			return;
		}
        if($scope.smsCode==null){
            alert("请输入验证码!");
            return ;
        }

		//新增
		userService.add($scope.entity,$scope.smsCode).success(function (response) {
			alert(response.message);
        })

	}

	//发送验证码
	$scope.sendCode=function(){
        if($scope.entity.phone==null){
        	alert("请输入手机号!");
        	return ;
        }
		userService.sendCode($scope.entity.phone).success(function (response) {
            alert(response.message);
        })

	}
	
});	
