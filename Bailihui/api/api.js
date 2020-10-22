import req from 'request.js'
const app=getApp();
export default {
  getCategories:function (){
    req('/shop/goods/category/all','get','').then(res=>{
      if(res.code==0){
        const data=res.data;
        getApp().globalData.categories=data;
        return Promise.resolve(data);
      }
    })
    .then(data=>console.log(data))
    .catch(err=>console.log(err))
  }
}