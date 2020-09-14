/** 
 * @部分参数说明 
 */  
(function($){  
    $.fn.extend({  
    	
    	closeLoading:function(){
    		var crust = this.children(".x-loading-wanghe"); 
    		if(crust.length>0){  
                if(crust.is(":visible")){  
                    crust.fadeOut(500);  
                }else{  
                    crust.fadeIn(500);  
                }  
                return this;  
            }  
    	},
        //主函数  
        toggleLoading: function(options){  
            // 找到遮罩层  
            var crust = this.children(".x-loading-wanghe");  
            // 当前操作的元素  
            var thisjQuery = this;  
            // 实现toogle(切换遮罩层出现与消失)效果的判断方法  
            if(crust.length>0){  
                if(crust.is(":visible")){  
                    crust.fadeOut(500);  
                }else{  
                    crust.fadeIn(500);  
                }  
                return this;  
            }  
            // 扩展参数  
            var op = $.extend({  
                z: 9999,  
                msg:'城市切换中...',  
                msgWidth:80,
                iconUrl:'image/loading.gif',  
                width:18,  
                height:18,  
                borderColor:'#2054B2',  
                opacity:0.3,  
                agentW:thisjQuery.outerWidth(),  
                agentH:thisjQuery.outerHeight()  
            }, options || {}); 
            
            if(thisjQuery.css("position")=="static")  
                thisjQuery.css("position","relative");  
            //var w = thisjQuery.outerWidth(),h = thisjQuery.outerHeight();  
              
            var w = op.agentW,h = op.agentH;                              
            crust = $("<div></div>").css({//外壳  
                'position': 'absolute',  
                'z-index': op.z,  
                'display':'none',  
                'width':w+'px',  
                'height':h+'px',  
                'text-align':'center',  
                'top': '0px',  
                'left': '0px',  
                'font-family':'arial',  
                'font-size':'12px',  
                'font-weight':'500'  
            }).attr("class","x-loading-wanghe");  
              
            var mask = $("<div></div>").css({//蒙版  
                'position': 'absolute',  
                'z-index': op.z+1,  
                'width':'100%',  
                'height':'100%',  
                'background-color':'#333333',  
                'top': '0px',  
                'left': '0px',  
                'opacity':op.opacity  
            });  
            //71abc6,89d3f8,6bc4f5  
            var msgCrust = $("<span></span>").css({//消息外壳  
                    'position': 'fixed',  
                    'top': 45+'%',
                    'left': (w-op.msgWidth-50)/2+"px",
                    'z-index': op.z+2,  
                    'display':'inline-block',  
                    'background-color':'#2054B2',  
                    'padding':'2px',  
                    'color':'#000000',  
                    'border':'1px solid '+op.borderColor,  
                    'text-align':'left',  
                    'opacity':0.9,
                    'filter':'alpha(opacity=90)', 
            		'-moz-opacity':0.9 
                });  
            var msg = $("<span>"+op.msg+"</span>").css({//消息主体  
                    'position': 'relative',  
                    'margin': '0px',  
                    'z-index': op.z+3,  
                    'line-height':'22px',  
                    'height':'22px',
                    'width':op.msgWidth+"px",
                    'display':'inline-block',  
                    'color':'#fff',
                    'background-color':'#2054B2',  
                    'padding':'10px 10px 10px 35px',  
                    'border':'1px solid '+op.borderColor,  
                    'text-align':'left',  
                    'text-indent':'0'  
                });  
            var msgIcon =  $("<img src="+op.iconUrl+" />").css({//图标  
                    'position': 'absolute',  
                    'top': '35%',  
                    'left':'10px', 
                    'z-index': op.z+4  
                });   
            // 拼装遮罩层  
            msg.prepend(msgIcon);  
            msgCrust.prepend(msg); 
            crust.prepend(mask);  
            crust.prepend(msgCrust);  
            thisjQuery.prepend(crust);
            // alert(thisjQuery.html());  
            crust.fadeIn(500);  
            //模态设置  
            return this;  
        }  
    });  
})(jQuery); 