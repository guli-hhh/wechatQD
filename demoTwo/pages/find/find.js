const app=getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
      tips:false,
      datalist:[],
      idId:""
  },
  //扫描图标
  scancode: function () {
    // console.log(1)
    // wx.scanCode({
    //     onlyFromCamera: false,
    //     success: (res) => {
    //         if(res.result == '123'){
    //             wx.showToast({
    //                 title: '签到成功！',
    //             })
    //         }
    //     }
    // })
},
  //通过活动邀请码id查询单个
  formsubmit(e){
    var inid=e.detail.value.id;
    wx.request({
      url: app.globalData.host+"pro/findByinId/" + inid,
      success:function(res){
          if(res.data.pro_id != null&&res.data.status!=0)
          {
              wx.navigateTo({
               url: '../enroll/enroll?proid=' + res.data.pro_id,
               })
          }
          else{
              wx.showToast({
                  title: '暂未找到相关签到活动',
                  duration:1500,
                  image:"/images/error.png"
              })
          }
      }
    })

  },

  //活动链接跳转签到管理页面
  shock: function (event) {
    var pro_id = event.currentTarget.dataset.id
    wx.navigateTo({
      url: '../simple/simple?proid=' + pro_id,
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that = this;
    //渲染创建的签到列表
    wx.request({
        url: app.globalData.host+"pro/findAll/" + app.globalData.userid,
        method:"GET",
        success: function (res) {
            console.log(res)
            //判断是否有创建签到活动
            if (res.data.length === 0) {
                that.setData({
                    tips: true
                })
            }
            else {
                //签到活动的展示
                // var dataList = res.data
                var list=[]
                var statussrc=""
                var a = res.data
                var j = a.length
                var timestamp = new Date();
                timestamp=timestamp.getTime()
                for (let i = 0; i < j; i++) {
                    if (timestamp > a[i].endTime) {
                      statussrc = '/images/gry.png'
                    }
                    else {
                      statussrc = '/images/green.png'
                    }
                    var one={proid:a[i].pro_id,projectName:a[i].projectName,statussrc:statussrc}
                    list.push(one)
                }
                that.setData({
                    datalist: list
                })
            }
        }
    })






  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})