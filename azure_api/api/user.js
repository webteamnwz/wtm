exports.get = function(req, res) {  
  var errman = require('./common').errman; 
  var jsonman = require('./common').jsonman;   
  
  // 인증
  var authman = require('./common').authman;
  var user_id = authman['auth'](req.user);
  var fb_token = authman['fbtoken'](req.user);
  if(user_id === '') {
    res.send(statusCodes.OK, errman['err'](998, ''));  
  }   
    
  if(typeof req.query.user_id != 'undefined')
    user_id = req.query.user_id;       

  var mssql = req.service.mssql;
  var sql = "EXEC wtm.sp_user ?"; 
  var params = [user_id];   
  
  mssql.query(sql, params, {    
    success: function(results) { 
      var rtnJson = jsonman['success']();  
      if(results.length === 0)
         rtnJson["user"] = [];
      else {                 
        rtnJson["user"] = {
            "user_id":results[0].user_id,
            "user_name":results[0].user_name,
            "user_img":results[0].user_img,
            "user_profile":results[0].user_profile,
            "user_group":results[0].user_group,
            "is_first":results[0].is_first,
            "fb_token":fb_token
        };
      }        
      // user category 정보 가져오기
      sql = "SELECT ca.category_no, ca.category_name " +
      "FROM wtm.wtm_category ca " +
      "INNER JOIN wtm.wtm_user_category cr " +
      "ON ca.category_no = cr.category_no " +
      "WHERE cr.user_id = ?";
      mssql.query(sql, params, {
        success: function(results) {
            rtnJson["user_category"] = results;
            res.send(statusCodes.OK, rtnJson);
        },
        error: function(err) {
          res.send(statusCodes.OK, errman['err'](999, err));     
        }
      });                       
    },
    error: function(err) {        
      res.send(statusCodes.OK, errman['err'](999, err)); 
    }   
  });           
};

exports.put = function(req, res) {
  var errman = require('./common').errman; 
  var jsonman = require('./common').jsonman;  
  
  // 인증  
  var authman = require('./common').authman;
  var user_id = authman['auth'](req.user);
  if(user_id === '') {
    res.send(statusCodes.OK, errman['err'](998, ''));  
  } 
      
  //var user_id = req.query.user_id; // TEST 
  var user_name = req.body.user_name;
  var user_img = req.body.user_img; 
  var user_email = req.body.user_email
  var user_profile = req.body.user_profile;
  var user_group = req.body.user_group;        
  var category_no_1 = req.body.category_no_1;
  var category_no_2 = req.body.category_no_2;
  var category_no_3 = req.body.category_no_3;
  
  if(typeof category_no_1 == 'undefined')
    category_no_1 = 0;
    
  if(typeof category_no_2 == 'undefined')
    category_no_2 = 0;
    
  if(typeof category_no_3 == 'undefined')
    category_no_3 = 0;        
  
  /*
  console.log('user_id:' + user_id);    
  console.log('user_name:' + user_name);    
  console.log('user_img:' + user_img);
  console.log('user_email:' + user_email);    
  console.log('user_profile:' + user_profile);    
  console.log('user_group:' + user_group);    
  console.log('category_no_1:' + category_no_1);           
  console.log('category_no_2:' + category_no_2);    
  console.log('category_no_3:' + category_no_3);
  */        
    
  var params =  [user_id, user_name, user_img, user_email, 
  user_profile, user_group, category_no_1, category_no_2, category_no_3];                       
  var mssql = req.service.mssql;
  var sql = "EXEC wtm.sp_user_modify ?, ?, ?, ?, ?, ?, ?, ?, ?"
  mssql.query(sql, params, {
    success: function(results) {       
      var rtnJson = jsonman['success']();           
      res.send(statusCodes.OK, rtnJson);
    },
    error: function(err) {        
      res.send(statusCodes.OK, errman['err'](999, err)); 
    }   
  });
};

exports.delete = function(req, res) {
  var errman = require('./common').errman; 
  var jsonman = require('./common').jsonman;  
  
  // 인증
  var authman = require('./common').authman;
  var user_id = authman['auth'](req.user);
  if(user_id === '') {
    res.send(statusCodes.OK, errman['err'](998, ''));  
  }   
     
  //var user_id = req.query.user_id; // TEST // identify replace    
  var params = [user_id];  
  var mssql = req.service.mssql;
  var sql = "EXEC wtm.sp_user_del ?";   
  
  mssql.query(sql, params, {
    success: function(results) {       
      var rtnJson = jsonman['success']();          
      res.send(statusCodes.OK, rtnJson);
    },
    error: function(err) {        
      res.send(statusCodes.OK, errman['err'](999, err)); 
    }   
  });       
}