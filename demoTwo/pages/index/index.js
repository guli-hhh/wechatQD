// index.js
// 获取应用实例
const app = getApp()

Page({
  data: {
    hasUserInfo: false,
    buttonImg:"/images/start.png",
    indflag:false,
    adsec:6,
    adUrl:"",
    adObj:{},
    adid:0
  },
  // 事件处理函数
  bindViewTap() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  //二号广告位点击事件
  adDetial(){
    if(this.data.adid>0){
      wx.request({
        url: app.globalData.host+"advert/updAdNum/"+this.data.adid+"/"+app.globalData.userid+"/2",
        method:"PUT",
        success:function(res){
            if(res.data===2){
                console.log("二号广告位广告点击数量加1")
            }
        }
      })
    }
  },

  onLoad: function (options) {
    app.globalData.btnmark=0;
    var th=this;
    //隐藏底部导航
    wx.hideTabBar({
      animation: true,
    })
    //获取已上线二号广告位广告内容
    wx.request({
      url: app.globalData.host+"advert/getAdone/" + 2,
      success:function(res){
          console.log(res.data);
          if(res.data!=""){
            // if(res.data.advertUrl)
            th.setData({
              adUrl:res.data.advertUrl,
              adid:res.data.ad_id
            })
            //有广告，进行展示
            setTimeout(function(){
              //防止广告未加载出来
              //统计展示数量
              wx.request({
                url: app.globalData.host+"advert/updAdNum/"+res.data.ad_id+"/"+app.globalData.userid+"/1",
                method:"PUT",
                success:function(res){
                    if(res.data===1){
                        console.log("二号广告位广告展示数量加1")
                    }
                }
              })
            },1000*5)

          }else{
              //显示底部导航栏
              wx.showTabBar({
                animation: true,
              })
              //显示首页，进行登录操作
              th.setData({
                indflag:true,
              })
          }
      }
    })
    //倒计时
    var sed=6
    var inter=setInterval(function(){
      th.setData({
        adsec:sed
      })
      sed--
      if(sed===-1){
        //显示底部导航栏
        wx.showTabBar({
          animation: true,
        })
        //显示首页，进行登录操作
        th.setData({
          indflag:true,
        })
        //清除定时器
        clearInterval(th.data.inter)
       
      }
    },1000);
    th.setData({
      inter:inter
    })
    
      //判断后台是否有用户信息
      if (app.globalData.userid!=null) {
        this.setData({
          hasUserInfo:true
        }) 

      }
      
      var that=this;
      wx.login({
        success(res1){
          //2.将code作为参数发送到后端服务器
          wx.request({
            url: app.globalData.host+'user/change/'+res1.code,
            method:"GET",
            success:function(response){   
              //用于判断后端是否有用户数据
              var flag =response.data.flag;
              if(!flag){
                //返回false的结果说明不需要进行post请求存数据则后台有用户数据
                that.setData({
                  hasUserInfo:true
                })
              }
              //发送请求进行登录时间和登录次数的记录，以及判断是否为禁用用户
              wx.request({
                url: app.globalData.host+"user/updUser/"+response.data.userid,
                method:"PUT",
                success:function(res){
                  console.log(res.data+"登录记录")
                  if(res.data==0){
                    wx.navigateTo({
                      url: '../forbidden/forbidden',
                    })
                  }
                }
              })
              
            
            }
          })
        }   
      })

    
  },
  //跳过广告
  cliadv(){
    //显示底部导航栏
    wx.showTabBar({
      animation: true,
    })
    //显示首页，进行登录操作
    this.setData({
      indflag:true,
    })
  },


  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，
    //开发者每次通过该接口获取用户个人信息均需用户确认，
    //开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.getUserProfile({
      desc: '完善用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        console.log(res.userInfo)
        //将用户信息存入全局变量，所有页面都要使用
        app.globalData.userInfo=res.userInfo
        this.setData({
          hasUserInfo: true
        })
        //处理用户在后端服务器登录和注册
        //1.调用微信登录接口，获取用户凭证code
        wx.login({
          success(res1){
            console.log(res1.code)
            //2.将code作为参数发送到后端服务器
            wx.request({
              url: app.globalData.host+'user/change/'+res1.code,
              method:"GET",
              success:function(response){
                console.log(response.data.flag)
                if(response.data.flag){
                  //需要发送post请求存数据到后端
                  wx.request({
                    url: app.globalData.host+'user/addUserInfo',
                    // 改变请求头实现传递对象参数
                    header: {
                      "content-type": "application/json;charset=utf-8"
                      },
                      method: "POST",
                      data:JSON.stringify({userId:response.data.userid,nickName:res.userInfo.nickName,avatarUrl:res.userInfo.avatarUrl,city:res.userInfo.city,country:res.userInfo.country,gender:res.userInfo.gender,language:res.userInfo.language,province:res.userInfo.province}),
                      success:function(res2){
                          console.log(res2)
                      } 
                  }) 
                }
                //给全局变量设置userid
                app.globalData.userid=response.data.userid;
                console.log(app.globalData.userid)
              }
            })
          }
        })
      }
    })
  },
 
//签到按钮按下开始
  start(){
    this.setData({
      buttonImg:"/images/end.png"
    })
    
  },
//签到按钮松开结束
  end(){
    this.setData({
      buttonImg:"/images/start.png"
    })
    console.log(app.globalData.btnmark);
    //控制二维码假签到
    if(app.globalData.btnmark===1){
      wx.showToast({
        title: '您暂无签到。',
        duration:1500
      })
    }else if(app.globalData.btnmark===0){
    // 引入SDK核心类
    var QQMapWX = require('../../libs/qqmap-wx-jssdk.js');
    // 实例化API核心类
    var qqmapsdk = new QQMapWX({
        key: 'J7WBZ-WTNCR-23DW5-W2FOU-ZZXB6-H4BCU'//申请的开发者秘钥key
    });

    wx.getLocation({
      type: 'wgs84', 
      success(res) {
         // 调用sdk接口
         qqmapsdk.reverseGeocoder({
          location: {
              latitude: res.latitude,
              longitude: res.longitude
          },
          success: function (res) {
            //获取当前地址成功
              console.log(res);
            //发送请求进行签到
            wx.request({
              url: app.globalData.host+"ppt/sign",
              method:"PUT",
              header:{
                "content-type": "application/json;charset=utf-8"
              },
              data:JSON.stringify({
                userId:app.globalData.userid,
                placeName:res.result.address_reference.landmark_l2.title,
                placeAdress:res.result.address,
                lat:res.result.location.lat,
                lng:res.result.location.lng
              }),
              success:function(suc){
                console.log(suc.data)
                var sd=suc.data
                if(sd.msg){
                  //没有签到活动
                  wx.showToast({
                    title: sd.msg,
                    duration:1500
                  })
                  return
                }
                if(sd.list){
                  //判断有几个签到活动
                  var total=0
                  var str=""
                  for(var i=0;i<sd.list.length;i++){
                    var one=sd.list[i];
                    if(one.num!=-1){
                      total+=one.num                     
                    }
                  }
                  if(total===0){
                    str="您未到指定位置或无签到活动。"
                  }else{
                    str="已签到"+total+"个。"
                  }
                  wx.showToast({
                    title: str,
                    duration:1500
                  })
                  
                }
              }
            })  
          
          },
          fail: function (res) {
              console.log(res);
          }
      });

      }
    })
    console.log("end方法：发请求，查询用户参与的签到项目，提交签到信息")
    }
    
  },

  
})
