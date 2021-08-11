// pages/sign/sign.js
// 获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    date:"",
    time:"00:00",
    date1:"",
    time1:"00:00",
    place_name:"点我选择地点（必选）",
    latitude:0.0,
    longitude:0.0,
    place_address:'',
    rad:"/images/check1.png",
    qrcode:0,
    css1:"add",
    count1:0,
    css2:"add",
    count2:0,
    css3:"add",
    count3:0,
    css4:"add",
    count4:0,
    css5:"add",
    count5:0,
    //参与者信息第三个块块
    diy1:"",
    diy2:"",
    diy3:"",
    diy4:"",
    diy5:"",
    aa:"before",
    flag:true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var now=new Date();
    var year=now.getFullYear();
    var month=now.getMonth()+1;
    var hour=now.getHours();
    var minut=now.getMinutes();
    var minut1=minut+10;
    var hour1=hour;
    if(minut<10){
      minut="0"+minut
    }
    if(minut1>60){
      hour1++;
      minut1=minut1-60
      if(minut1<10){
        minut1="0"+minut1
      }
    }
    if(month<10){
      month="0"+month
    }
    var day=now.getDate();
    this.setData({
      date:year+"-"+month+"-"+day,
      date1:year+"-"+month+"-"+day,
      time:hour+":"+minut,
      time1:hour1+":"+minut1,
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

  },

  dateChange(e){
    //设置开始日期
    this.setData({
      date:e.detail.value
    })
  },
  dateChange1(e){
    //设置结束日期
    this.setData({
      date1:e.detail.value
    })
  },
  timeChange(e){
    //设置起始时分
    this.setData({
      time:e.detail.value
    })
  },
  timeChange1(e){
    //设置结束时分
    this.setData({
      time1:e.detail.value
    })
  },
  
  chooseLocation(){
    var that=this;
    //选签到定位地点
    wx.chooseLocation({
      success(res){
        if(res.name.length>15){
          res.name=res.name.substring(0,15)+"..."
        }
        console.log(res.latitude+"sign")
        console.log(res.longitude+"sign")
        console.log(res.name+"sign")
        console.log(res.address+"sign")
        that.setData({
          place_name:res.name,
          place_address:res.address,
          latitude:res.latitude,
          longitude:res.longitude
        })
      }
    })
  },
  shuoming(){
    wx.showModal({
      title: "扫码签到说明",
      content: "启用后需在签到现场放置签到二维码，参与者扫码后方可完成签到。精度高，速度慢，适用于对签到准确性要求很高的活动。不启用则使用默认的定位签到。存在一定的误差但速度极快，无需任何配置，适用于人数较多场面混乱的户外签到。",
      showCancel: false
    })
  },
  //是否开启扫码模式
  check(){
    if(this.data.qrcode==0){
      this.setData({
        rad:"/images/check2.png",
        qrcode:1
      })
      return;
    }
    if(this.data.qrcode==1){
      this.setData({
        rad:"/images/check1.png",
        qrcode:0
      })
    }
  },
  //参与者按钮事件
  changeCss1(){
    if(this.data.css1=="chosed"&&this.data.css2=="add"&&this.data.css3=="add"&&this.data.css4=="add"&&this.data.css5=="add"){
      this.setData({
        flag:true
      })
    }
    if(this.data.css1=="add"){
      this.setData({
        css1:"chosed",
        count1:1
      })
      return;
    }
    if(this.data.css1=="chosed"){
      this.setData({
        css1:"add",
        count1:0
      })
      return;
    }
    
  },
  changeCss2(){
    if(this.data.css1=="add"&&this.data.css2=="chosed"&&this.data.css3=="add"&&this.data.css4=="add"&&this.data.css5=="add"){
      this.setData({
        flag:true
      })
    }
    if(this.data.css2=="add"){
      this.setData({
        css2:"chosed",
        count2:1
      })
      return;
    }
    if(this.data.css2=="chosed"){
      this.setData({
        css2:"add",
        count2:0
      })
      return;
    }
    
  },
  changeCss3(){
    if(this.data.css1=="add"&&this.data.css2=="add"&&this.data.css3=="chosed"&&this.data.css4=="add"&&this.data.css5=="add"){
      this.setData({
        flag:true
      })
    }
    if(this.data.css3=="add"){
      this.setData({
        css3:"chosed",
        count3:1
      })
      return;
    }
    if(this.data.css3=="chosed"){
      this.setData({
        css3:"add",
        count3:0
      })
      return;
    }
    
  },
  //第三个块块失焦事件
  change3(e){
    if(e.detail.value.length>0){
      this.setData({
        diy3:e.detail.value
      })
    }else{
      this.setData({
        count3:0
      })
    }
  },
  changeCss4(){
    if(this.data.css1=="add"&&this.data.css2=="add"&&this.data.css3=="add"&&this.data.css4=="chosed"&&this.data.css5=="add"){
      this.setData({
        flag:true
      })
    }
    if(this.data.css4=="add"){
      this.setData({
        css4:"chosed",
        count4:1
      })
      return;
    }
    if(this.data.css4=="chosed"){
      this.setData({
        css4:"add",
        count4:0
      })
      return;
    }
    
  },
  //第四个块块失焦事件
  change4(e){
    if(e.detail.value.length>0){
      this.setData({
        diy4:e.detail.value
      })
    }else{
      this.setData({
        count4:0
      })
    }
  },
  changeCss5(){
    if(this.data.css1=="add"&&this.data.css2=="add"&&this.data.css3=="add"&&this.data.css4=="add"&&this.data.css5=="chosed"){
      this.setData({
        flag:true
      })
    }
    if(this.data.css5=="add"){
      this.setData({
        css5:"chosed",
        count5:1
      })
      return;
    }
    if(this.data.css5=="chosed"){
      this.setData({
        css5:"add",
        count5:0
      })
      return;
    }
   
  },
  //第五个块块失焦事件
  change5(e){
    if(e.detail.value.length>0){
      this.setData({
        diy5:e.detail.value
      })
    }else{
      this.setData({
        count5:0
      })
    }
  },
  //参与者确定按钮事件
  confirm(){
    if((this.data.count1+this.data.count2+this.data.count3+this.data.count4+this.data.count5)==0){
      wx.showToast({
        title: '请至少选择一项。',
        duration:2000,
        image:"/images/error.png"
      })
    }else{
      this.setData({
        flag:false,
        aa:"confirm"
      })
    }
  },
  //表单提交事件
  formsubmit(e){
    //e.detial---表单提交所有数据
    console.log(e.detail)
    //做表单检查
    if(this.data.place_name=="点我选择地点（必选）"){
      wx.showToast({
        title: '活动地址为必填项。',
        duration:2000,
        image:"/images/error.png"
      })
      return;
    }

    if(e.detail.value.projectName==""){
      console.log(11)
      wx.showToast({
        title: '活动名称为必填项。',
        duration:2000,
        image:"/images/error.png"
      })
      return;
    }

    //处理起始时间  将字符串"2021-05-21 15:34"转换为js中日期时间型对象
    //replace(a,b)  a:正则表达式 /原字符串字符/g(g:代表全局替换)  b:新字符
    var startStr=this.data.date+" "+this.data.time;
    var startTime=new Date(Date.parse(startStr.replace(/-/g,'/')));
    //将选中的时间转换为时间戳，毫秒数
    startTime=startTime.getTime();
    // console.log(startTime)
    //处理结束时间   timestamp数据类型存储
    var endStr=this.data.date1+" "+this.data.time1;
    var endTime=new Date(Date.parse(endStr.replace(/-/g,'/')));
    //将选中的时间转换为时间戳，毫秒数
    endTime=endTime.getTime();
    // console.log(endTime)

   
   
    //自定义报名数据
    if(this.data.css1=="chosed"){
      this.setData({
        diy1:"姓名"
      })
    }
    if(this.data.css2=="chosed"){
      this.setData({
        diy2:"电话"
      })
    }
    if(this.data.css3!="chosed"){
      this.setData({
        diy3:"未定义"
      })
    }
    if(this.data.css4!="chosed"){
      this.setData({
        diy4:"未定义"
      })
    }
    if(this.data.css5!="chosed"){
      this.setData({
        diy5:"未定义"
      })
    } 
  

    var that=this;
   
    wx.request({
      url: app.globalData.host+'pro/addOne',
      method:"POST",
      data:{
        userId:app.globalData.userid,
        projectName:e.detail.value.projectName,
        startTime:startTime,
        endTime:endTime,
        place_name:that.data.place_name,
        place_address:that.data.place_address,
        latitude:that.data.latitude,
        longitude:that.data.longitude,
        qrcode:that.data.qrcode,
        diy1:that.data.diy1,
        diy2:that.data.diy2,
        diy3:that.data.diy3,
        diy4:that.data.diy4,
        diy5:that.data.diy5,
        add:e.detail.value.add
      },
      success(res){
        console.log(res.data)
        wx.showToast({
          title: '创建成功',
          icon:"success",
          duration:1500
        })
      }
    })

  }

})