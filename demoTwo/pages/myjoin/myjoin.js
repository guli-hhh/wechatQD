const app=getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
      proObj:{}
  },


  //删除签到
  del: function (event) {
      var id = event.currentTarget.dataset.id
      wx.showModal({
          title: '确认删除这个签到？',
          confirmColor: '#FF0002',
          success(e) {
              if (e.confirm) {
                  wx.request({
                      url: app.globalData.host+"ppt/delAndBack/"+id,
                      method:"DELETE",
                      success: function (res) {
                          wx.showToast({
                              title: '删除成功',
                              duration: 1000,
                              success: function () {
                                wx.redirectTo({
                                  url: '/pages/join/join'
                                })
                              }
                          })
                      }
                  })
              }
          }
      })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var proid = options.proid
    var pstatus=options.pstatus
    var pid=options.pid
    var that = this
    wx.request({
        url: app.globalData.host+'pro/findById/' + proid,
        success: function (res) {
            var one=res.data
            var pro={
              pid:pid,
              proid:proid,
              pstatus:pstatus,
              projectName:one.projectName,
              startTime:"",
              place_name:one.place_name,
              place_address:one.place_address,
              status:-1
            } 
             //long转换日期类型
             var dateTypeDate = "";  
             var now = new Date();  
             now.setTime(one.startTime);  
             var year=now.getFullYear();
             var month=now.getMonth()+1;
             var day=now.getDate();
             var hour=now.getHours();
             var minut=now.getMinutes();
             if(minut<10){
               minut="0"+minut
             }
             dateTypeDate+=year+"-"+month+"-"+day+" "+hour+":"+minut

             pro.startTime=dateTypeDate
             that.setData({
               proObj:pro
             })

        }
    })
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