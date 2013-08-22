exports.errman = {
    err: function (code, msg) {                     
        switch(code) {        
            case 998:
              if(msg === '')
                msg = "인증받지 않은 요청";
              break;
            case 999:
              if(msg === '')
                msg = "DB Error";
              break;
            default:
              msg = "Unknown Error";              
        }
                    
        var result = {
            "code":code,
            "msg":msg
        }        
        
        return result;      
   }
}

exports.jsonman = {
    success: function (msg) {
        var result = {
            "code":100,
            "msg":"정상"
        }
                
        return result;
    }    
}

exports.authman = {
    auth: function (user) {
      var identities = user.getIdentities();
      var user_id = '';
      
      if(identities.facebook) {
        user_id = user.userId;
      }
     
      return user_id;                     
    },    
    fbtoken: function (user) {
      var identities = user.getIdentities();
      var fbtoken = '';
      
      if(identities.facebook) {
        fbtoken = identities.facebook.accessToken;
      }
     
      return fbtoken;           
    }
}