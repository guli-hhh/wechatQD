const app=getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    enroll:0,
    flag:false,
    data:[], //参与者的所有数据
    titlearr:[]  //标题信息数据
  },

  //个人信息
  participant:function(e){
    var ind=e.currentTarget.dataset.ind
    var str=""
    var a=this.data.data[ind]
    if(a.pname!=null){
      str+="姓名："+a.pname
    }
    if(a.phone!=null){
      str+=" 电话："+a.phone
    }
    if(a.diy3!=null){
      str+=" "+a.diy3
    }
    if(a.diy4!=null){
      str+=" "+a.diy4
    }
    if(a.diy5!=null){
      str+=" "+a.diy5
    }
    wx.showModal({
      title:"个人信息",
      content:str,
      confirmText:"关闭",
      confirmColor:"#ff3d0b",
      showCancel:false
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that=this
    this.setData({
      enroll:options.enroll
    })
    if(this.data.enroll>0){
      //有人参与，则查询
      this.setData({
        flag:true
      })
      wx.request({
        url: app.globalData.host+"ppt/findByproid/"+options.proid+"/1",
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
            var one={title:title,ind:i}
            list.push(one)
          }
          that.setData({
            data:rd,
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