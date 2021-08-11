// app.js
const app = getApp()
App({
  globalData: {
    userInfo: null,
    //后端自定义用户id
    userid:null,
    host:"http://localhost:8080/wx/",
    //首页按钮标记
    btnmark:0
  },
  
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    this.globalData.btnmark=0;
    var that=this;
     // 登录
     wx.login({
      success(res1){
        console.log(res1.code)
        //2.将code作为参数发送到后端服务器
        wx.request({
          url: that.globalData.host+'user/change/'+res1.code,
          method:"GET",
          success:function(response){
            //通过全局变量判断后台是否有数据
            // console.log(response.data.userid)
            var id=response.data.userid
            that.globalData.userid=id
            
          }
        })
        
      }   
    })

    // console.log(this.globalData.userid)
  },
  
})
