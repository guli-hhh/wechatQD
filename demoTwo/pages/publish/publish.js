// pages/publish/publish.js
const app=getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tips: false,  //判断是否需要有欢迎提示
    dataList:[]  //页面展示数据集合
  },

  //跳转创建活动页面
  jump: function () {
    wx.navigateTo({
        url: '/pages/sign/sign',
    })
  },
  //跳转签到管理页面
  simple:function(event){
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
            success: function (res) {
                console.log(res.statusCode)
                //报错或者没有创建
                if (res.data.length === 0 || res.statusCode != 200) {
                    that.setData({
                        tips: true
                    })
                }
                else {
                  //查询到了创建的活动
                    let arr = res.data;
                    //判断状态，活动是否结束
                    let timestamp =new Date(); 
                    timestamp=timestamp.getTime();
                    //遍历得到的所有活动，做数据  
                    var list=[]      
                    for (let i = 0; i < arr.length; i++){               
                        var obj={pro_id:"",stauts:"",flag:"",projectName:"",inviteId:"",creator:"",startTime:""}
                        var mark;
                        if (timestamp > arr[i].endTime){
                          //已结束
                            obj.stauts="已结束"
                            obj.flag="top-le"
                            obj.creator="/images/ent.png"
                            mark=2
                        }
                        else if(timestamp<=arr[i].endTime&&timestamp>=arr[i].startTime){
                          //进行中
                            obj.stauts="进行中"
                            obj.flag="top-left"
                            obj.creator="/images/enter.png"
                            mark=1
                        }else if(timestamp<arr[i].startTime){
                          //未开始
                          obj.stauts="未开始"
                          obj.flag="top-left"
                          obj.creator="/images/enter.png"
                          mark=0
                        }
                        //更新数据库活动状态
                        wx.request({
                          url:app.globalData.host +'pro/updStatus/'+arr[i].pro_id+"/"+mark,
                          header: {
                            "content-type": "application/json;charset=utf-8"
                            },
                          method:"PUT",
                          success:function(res){
                            console.log(res.data)
                          }
                        })


                        obj.projectName=arr[i].projectName
                        obj.inviteId=arr[i].inviteId
                        obj.pro_id=arr[i].pro_id
                        //long转换日期类型
                        var dateTypeDate = "";  
                        var now = new Date();  
                        now.setTime(arr[i].startTime);  
                        var year=now.getFullYear();
                        var month=now.getMonth()+1;
                        var day=now.getDate();
                        var hour=now.getHours();
                        var minut=now.getMinutes();
                        if(minut<10){
                          minut="0"+minut
                        }
                        dateTypeDate+=year+"-"+month+"-"+day+" "+hour+":"+minut

                        obj.startTime=dateTypeDate
                        list.push(obj)
                        
                    }
                    that.setData({
                        dataList:list
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