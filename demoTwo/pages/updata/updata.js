const app=getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    pmark:"",
    proid:0
  },


  //发送按钮
  formsubmit:function(e){
    //获取导出哪种数据和接收邮件
    var pmark=this.data.pmark;
    var to=e.detail.value.email;

     var mark;
    if(pmark==1){
        mark=0
    }else if(pmark==2){
        mark=1
    }
    console.log(this.data.proid)
    //发请求准备导出数据
    var that=this
    wx.request({
      url: app.globalData.host+"user/updata",
      method:"GET",
      data:{
        proid:that.data.proid,
        mark:mark,
        to:to
      },
      success:function(res){
          console.log(res.data)
          if(res.data==1){
            wx.showToast({
              title: '发送成功',
              duration:1500,
              icon:"success"
            })
          }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    

    this.setData({
      pmark:options.pmark,
      proid:options.proid
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