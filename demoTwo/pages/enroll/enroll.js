const app=getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    proid: 0,
    projectName: '',
    data1: null,   //需要提供的信息
    data2: null,
    data3: null,
    data4: null,
    data5: null,
    key1: false,
    key2: false,
    key3: false,
    key4: false,
    key5: false,
    ad:"adname",  //广告样式
    adflag:false,  //广告位的显示
    adObj:{}, //广告本身
  },

  //广告点击事件
  adDetial(e){
    wx.request({
        url: app.globalData.host+"advert/updAdNum/"+e.currentTarget.dataset.id+"/"+app.globalData.userid+"/2",
        method:"PUT",
        success:function(res){
            if(res.data===2){
                console.log("一号广告位广告点击数量加1")
            }
        }
      })
  },
  //提交按钮
  submit:function(e){
        var that=this
        let data1 = e.detail.value.value1;
        let data2 = e.detail.value.value2;
        let data3 = e.detail.value.value3;
        let data4 = e.detail.value.value4;
        let data5 = e.detail.value.value5;
        if (data1 === undefined) {
            data1 = null;
        }
        if (data2 === undefined) {
            data2 = null;
        }
        if (data3 === undefined) {
            data3 = null;
        }
        if (data4 === undefined) {
            data4 = null;
        }
        if (data5 === undefined) {
            data5 = null;
        }
        if (data2!=undefined && data2.length != 11) {
          wx.showToast({
              title: '请输入正确的手机号',
              duration: 1500
          })
        }
        wx.request({
          url: app.globalData.host+"ppt/addOne",
          data: {
              pro_id: that.data.proid,
              userId: app.globalData.userid,
              pname: data1,
              phone: data2,
              diy3: data3,
              diy4: data4,
              diy5: data5,
          },
          method: 'POST',
          header: {
              'content-type': 'application/json' // 默认值
          },
          success: function (res) {
              if(res.data==-2){
                wx.showToast({
                  title: '您已报名，不能重复报名。',
                  icon:"none",
                  duration: 1500,
              })
              }
              if(res.data==1){
                wx.showToast({
                  title: '报名成功。',
                  duration: 1500,
                })
                setTimeout(function(){
                  wx.redirectTo({
                    url: '../join/join',
                  })
                },1000)
              }
              
          }
      })




  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    var pro_id = options.proid;
    this.setData({
        proid: pro_id
    })
    //查询单个活动
    wx.request({
        url: app.globalData.host+"pro/findOne/" + pro_id,
        success: function (res) {
            let a = false, b = false, c = false, d = false, e = false;
            let A = null, B = null, C = null, D = null, E = null;
            if (res.data.diy1 == '姓名') {
                A = res.data.diy1;
                a = true;
            }
            if (res.data.diy2 == '电话') {
                B = res.data.diy2;
                b = true;
            }
            if (res.data.diy3 != '未定义' && res.data.diy3 != null) {
                C = res.data.diy3;
                c = true;
            }
            if (res.data.diy4 != '未定义' && res.data.diy4 != null) {
                D = res.data.diy4;
                d = true;
            }
            if (res.data.diy5 != '未定义' && res.data.diy5 != null) {
                E = res.data.diy5;
                e = true;
            }
            that.setData({
                projectName: res.data.projectName,
                data1: A,
                data2: B,
                data3: C,
                data4: D,
                data5: E,
                key1: a,
                key2: b,
                key3: c,
                key4: d,
                key5: e
            })
        }
    })
    //加载一号广告位所上线的广告内容
    wx.request({
      url: app.globalData.host+"advert/getAdone/" + 1,
      success:function(res){
          console.log(res.data);
          if(res.data){
            that.setData({
                adflag:true,
                adObj:res.data
            })
            //统计展示数量
            wx.request({
              url: app.globalData.host+"advert/updAdNum/"+res.data.ad_id+"/"+app.globalData.userid+"/1",
              method:"PUT",
              success:function(res){
                  if(res.data===1){
                      console.log("一号广告位广告展示数量加1")
                  }
              }
            })
          }
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
        setInterval(() => {
            var cl=this.data.ad;
            var clstr=""
            if(cl=="adname"){
                clstr="adname1"
            }else{
                clstr="adname"
            }
            this.setData({
                ad:clstr
            })
        }, 1500);
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