
const BASE_API='https://api.it120.cc/';
const domain="ckfruit"
function req(path,method,param){
  return new Promise((resolve,reject)=>{
    wx.request({
      url: BASE_API+domain+path,
      method: method,
      data: param,
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      success: (res)=>resolve(res.data),
      fail: (error)=>reject(error),
      complete:(data)=>{
        //加载完成
      }
    })
  })
}
export default req;
