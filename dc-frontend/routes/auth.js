var crypto = require('crypto');
var conf=require('./common/conf');
var constant=require('./common/constant');
function auth(req,res,next) {
    var authStr=req.get('Authorization');
    if (authStr!=undefined){
        try{
            authStr=new Buffer(authStr,'base64').toString();
            var obj=JSON.parse(authStr);
            var header=obj.header;//alg:string:sha1  typ:string:jwt
            var payload=obj.payload;//iat:int:签发时间  exp:过期时间
            var signature=obj.signature;

            var sStr=JSON.stringify(header)+'.'+JSON.stringify(payload);
            var tStr=crypto.createHmac('sha1',conf.secrectKey).update(sStr).digest('hex');
            if(tStr===signature){
                if(Date.now()/1000>payload.exp){
                    next(createError(402));//用户认证过期
                }
                if(!req.session.userStatus||req.session.userStatus===constant.USERSTATUS.UNKNOWN){
                    req.session.userStatus=constant.USERSTATUS.STANDARDUSER;
                }
            }else {
                next(createError(401));//用户认证失败
            }

        }catch (err){
            console.error(err);
            next(createError(401));//用户认证失败
        }
    }
    next();//未通过用户认证
}