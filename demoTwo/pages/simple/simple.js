const app=getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
      proid:null,
      qrflag:false,
      array:["暂时不导数据","导出报名数据","导出签到数据"],
      morearray:["编辑签到","访问网页版","退出"],
      index:0,
      qrcodeimg:"",
      qrcodeflag:false
  },
  //报名数据按钮
  enroll:function(e){
    var proid=e.currentTarget.dataset.id;
    var enroll=e.currentTarget.dataset.enroll;
    wx.navigateTo({
        url: '../simpledata/simpledata?proid=' + proid+'&enroll='+enroll,
    })
  },
  //签到数据按钮
  signdata:function(e){
    var proid=e.currentTarget.dataset.id;
    var enroll=e.currentTarget.dataset.enroll;
    var finish=e.currentTarget.dataset.finish;
    var projectName=e.currentTarget.dataset.projectname;
    wx.navigateTo({
        url: '../simpledetial/simpledetial?proid=' + proid+'&enroll='+enroll+'&finish='+finish+'&projectName='+projectName,
    })
  },
  //导出数据按钮
  bindPickerChange:function(e){
    console.log(e.detail.value)
    if(e.detail.value!=0){
        wx.navigateTo({
            url: '../updata/updata?pmark=' + e.detail.value+"&proid="+this.data.proid,
        })
    }
    
  },
  //删除签到按钮
  del:function(e){
    var proid=e.currentTarget.dataset.id;
    console.log(proid)
    wx.showModal({
        title: '确认删除这个签到？',
        confirmColor: '#FF0002',
        success(e) {
            if (e.confirm) {
                wx.request({
                    url: app.globalData.host+"pro/delOne/"+proid+"/"+app.globalData.userid,
                    method:"DELETE",
                    success:function(res){
                        if(res.data){
                          wx.showToast({
                              title: '删除成功',
                              duration: 1000,
                              success: function () {
                                  wx.redirectTo({
                                      url: '/pages/publish/publish',
                                  })
                              }
                          })
                         
                        }
                    }
                })
            }
        }
    })
    
  },
  //更多功能按钮
  bindPickerChange1:function(e){
    console.log(e.detail.value)
    if(e.detail.value==0){
        wx.navigateTo({
            url: "/pages/sign/sign",
        })
    }
    if(e.detail.value==1){
        wx.navigateTo({
          url: '/pages/network/network',
        })
    }
    
  },
  //签到二维码按钮
  qrcode:function(e){
    app.globalData.btnmark=1;
    var proid=e.currentTarget.dataset.id;
    var that=this;
    //判断是否开启扫码模式
    wx.request({
      url: app.globalData.host+"pro/findOne/"+proid,
      method:"GET",
      success:function(res){
        console.log(res.data.qrcode)
        if(res.data.qrcode==0){
          wx.showToast({
            title: '该活动暂未开启扫码模式',
            duration:1500,
            icon:"none"
          })
        }else{
          //二维码的生成
          wx.showToast({
            title: '生成中...',
            duration:6000,
            icon:"loading"
          })
      
          
          wx.request({
            url: app.globalData.host+"pro/qrcode/"+proid,
            //设置响应回来的数据为字节流
            responseType: 'arraybuffer',
            success:function(res){
                //当获取到的是图片的base64编码的数据流时，需要将接口获取到的数据再解析base64编码
                //上面解析base64时，在数据流前面加上  data:image/png;base64,  (注意是有个“,”) 。 image/png改为对应的图片类型，如 image/gif, image/jpg
                let url ='data:image/png;base64,'+wx.arrayBufferToBase64(res.data)
                console.log(url+"返回二维码图片流数据");
                //设置二维码图片路径
                that.setData({
                      qrcodeimg:url,
                      qrcodeflag:true
                })     
            }
          })
          //二维码的隐藏
          setTimeout(function(){
              that.setData({
                  qrcodeflag:false
              })
          },1000*60*5)


        }
      }
    })
  
    
   
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var proid = options.proid
    this.setData({
        proid:proid
    });
    var that = this
    wx.request({
        url: app.globalData.host+"pro/findById/" + proid,
        success: function (res) {
            console.log(res.data);
            let flag;
            if(res.data.qrcode === 1){
                flag = true;
            }
            else{
                flag = false;
            }
            //把时间转化成能看的
            var begin = res.data.startTime;
            var time = new Date(parseInt(begin)).toLocaleString().replace(/:\d{1,2}$/, ' ');
            that.setData({
                vue:res.data,
                time:time,
                qrflag:flag
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
  onShareAppMessage: function (res) {

    if (res.from === 'button') {
        //不同分享按钮，页面分享，右上角分享
        console.log('来自页面内转发按钮');
        //展示模拟群
        wx.showShareMenu({
          withShareTicket: true
        })
        
      }
   

  }
})