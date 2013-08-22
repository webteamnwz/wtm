exports.get = function(req, res) {
  var errman = require('./common').errman;    
  var jsonman = require('./common').jsonman;   
  
  // 인증
  var authman = require('./common').authman;
  var user_id = authman['auth'](req.user);
  if(user_id === '') {
    res.send(statusCodes.OK, errman['err'](998, ''));  
  }
  
  //var user_id = req.query.user_id; // TEST
  var s_no = req.query.s_no;
  var e_no = req.query.e_no;

  // paging 
  if (s_no === undefined || e_no === undefined) {
    s_no = 0;
    e_no = 0;  
  }     

  var params = [user_id];     
  var sub_params = [user_id, s_no, e_no];
  var mssql = req.service.mssql;
  var sql = "EXEC wtm.sp_user_room_cnt ?";            
  
  mssql.query(sql, params, {       
    success: function(results) {
      var rtnJson = jsonman['success']();              
      rtnJson["create_room_cnt"] = results[0].create_room_cnt;
      rtnJson["join_room_cnt"] = results[0].join_room_cnt;                      
            
      sql = "EXEC wtm.sp_user_room ?, ?, ?";                  
      mssql.query(sql, sub_params, {
        success: function(results) {         
          rtnJson["roomList"] = results       
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

exports.post = function(req, res) {
  var errman = require('./common').errman;    
  var jsonman = require('./common').jsonman;   
  
  // 인증
  var authman = require('./common').authman;
  var user_id = authman['auth'](req.user);
  if(user_id === '') {
    res.send(statusCodes.OK, errman['err'](998, ''));  
  }  
  
  //var user_id = req.query.user_id; // TEST   
  var room_no = req.body.room_no;
  
  var params = [user_id, room_no]; 
  var mssql = req.service.mssql;
  var sql = "INSERT INTO wtm.wtm_user_room(user_id, room_no, reg_date) " +
  "VALUES(?, ?, CONVERT(NVARCHAR(10), wtm.GetLocalDate(DEFAULT), 112))";
        
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
  var room_no = req.query.room_no;
  
  /*
  console.log(user_id);
  console.log(room_no);
  */
  
  var params = [user_id, room_no]; 
  var mssql = req.service.mssql;
  var sql = "UPDATE wtm.wtm_user_room " +
  "SET chk_date = CONVERT(NVARCHAR(10), wtm.GetLocalDate(DEFAULT), 112) " +
  "WHERE user_id = ? AND room_no = ?";

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
  
  //var user_id = req.query.user_id; // TEST   
  var room_no = req.query.room_no;
  
  var params = [user_id, room_no]; 
  var mssql = req.service.mssql;
  var sql = "DELETE wtm.wtm_user_room WHERE user_id = ? AND room_no = ?";
        
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