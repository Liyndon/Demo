$.fn.serializeObject = function() {//序列化表单
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

var Login = function() {

    var handleLogin = function() {
    	$("[name='userName']").focus();
    	//得到焦点
		$("#password").focus(function() {
			$("#left_hand").animate({
				left : "150",
				top : " -38"
			}, {
				step : function() {
					if (parseInt($("#left_hand").css("left")) > 140) {
						$("#left_hand").attr("class", "left_hand");
					}
				}
			}, 2000);
			$("#right_hand").animate({
				right : "-64",
				top : "-38px"
			}, {
				step : function() {
					if (parseInt($("#right_hand").css("right")) > -70) {
						$("#right_hand").attr("class", "right_hand");
					}
				}
			}, 2000);
		});

		//失去焦点
		$("#password").blur(function() {
			$("#left_hand").attr("class", "initial_left_hand");
			$("#left_hand").attr("style", "left:100px;top:-12px;");
			$("#right_hand").attr("class", "initial_right_hand");
			$("#right_hand").attr("style", "right:-112px;top:-12px");
		});
        $('#loginForm input').keypress(function(e) {
            if (e.which == 13) {
                login();
                return false;
            }
        });
    };
     
    var login = function(){
    	if(loginValidate()){
    		debugger;
    		var data = $("#loginForm").serializeObject();
    		data.validateType = "admin";//设置为管理员访问页面
			$.ajax({
				url:ctx+"/login",
				data : data,
				type : "post",
				success : function(res) {
					$("#error-tip").html(res.msg);
					if(res.target == "0"){
						window.location.replace(ctx + '/home');
					}else{
						if($("#"+res.target).length == 1){
							$("#"+res.target).focus().val("");
							$(".validate_code").attr("src", ctx+"/checkcode?width=140&height=36"+ "&id=" + Math.random());
						}
					}
				}
			});
		};
    };
    
    var loginValidate = function(){
		if($.trim($("[name='userName']").val()) == ""){
			$("#error-tip").html("用户名不能为空");
			$("[name='userName']").focus();
			return false;
		}else if($.trim($("#password").val()) == ""){
			$("#error-tip").html("密码不能为空");
			$("#password").focus();
			return false;
		}else if($.trim($("[name='vcode']").val()) == ""){
			$("#error-tip").html("验证码不能为空");
			$("[name='vcode']").focus();
			return false;
		}else if($.trim($("[name='vcode']").val()).length < 5){
			$("#error-tip").html("验证码位数不对");
			$("[name='vcode']").focus();
			$("[name='vcode']").val("");
			return false;
		}
		$("[name='userPassword']").val(sha256_digest($.trim($("#password").val())));//加密传输
		return true;
    };
    
    var reloadImage = function(fn,imgurl){
	    fn.src= imgurl + "&id=" + Math.random();
	};
    
    return {
        init : function() {
            handleLogin();
            $(".login_btn").bind("click", function(){
            	login();
            });
        },
    	reloadImage : reloadImage
    };

}();