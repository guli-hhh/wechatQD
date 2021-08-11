const app=getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    enroll:0,
    finish:0,
    projectName:"",
    proid:0,
    flag:true
  },

  //查看所有已到成员
  reto:function(){
    wx.navigateTo({
      url: '../simpledata/simpledata?proid=' + this.data.proid+'&enroll='+this.data.enroll,
  })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      enroll:options.enroll,
      finish:options.finish,
      projectName:options.projectName,
      proid:options.proid
    })
    if((options.enroll-options.finish)==0){
      this.setData({
        flag:false
      })
    }else{
      var that=this
      wx.request({
        url: app.globalData.host+"ppt/findByproid/"+options.proid+"/0",
        success:function(res){
          console.log(res.data)
          var rd=res.data
          var list=[]
          for(var i=0;i<rd.length;i++){
            var title;
            if(rd[i].pname!=null){
              title=rd[i].pname
            }else{
              if(rd[i].phone!=null){
                title=rd[i].phone
              }else{
                if(rd[i].diy3!=null){
                  title=rd[i].diy3
                }else{
                  if(rd[i].diy4!=null){
                    title=rd[i].diy4
                  }else{
                    if(rd[i].diy5!=null){
                      title=rd[i].diy5
                    }
                  }
                }
              }
            }
            list.push(title)
          }
          that.setData({
            titlearr:list
          })
        }
      })

    }


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