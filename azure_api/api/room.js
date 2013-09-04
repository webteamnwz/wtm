exports.get = function(req, res) {
  var errman = require('./common').errman;    
  var jsonman = require('./common').jsonman;   
  
  // 인증  
  var authman = require('./common').authman;
  var user_id = authman['auth'](req.user);
  if(user_id === '') {
    res.send(statusCodes.OK, errman['err'](998, ''));  
  } 
   
  var room_no = req.query.room_no;
  var s_no = req.query.s_no;
  var e_no = req.query.e_no;
  var sub_query = '';
  var params = [room_no];
  var sub_params = [room_no];
  
  // paging 
  if (s_no !== undefined || e_no !== undefined) {
    sub_query = 'WHERE rownum BETWEEN ? AND ?';
    sub_params = [room_no, s_no, e_no];   
  } 
  var mssql = req.service.mssql;
  var sql = "EXEC wtm.sp_room ?";      
  mssql.query(sql, params, {    
    success: function(results) {
      console.log(results);
      var rtnJson = jsonman['success']();      
      if(results.length === 0)
        rtnJson["room"] = [];
      else {               
        rtnJson["room"] = {                    
              "room_title":results[0].room_title,
              "room_desc":results[0].room_desc,
              "start_date":results[0].start_date,
              "end_date":results[0].end_date,
              "room_manager_id":results[0].room_manager_id,
              "cnt_join":results[0].cnt_join,
              "cnt_chk":results[0].cnt_chk,
              "category_ids":results[0].category_ids                                
            };
      }            
                  
       // room에 check한 멤버정보 가져오기
       sql = "SELECT user_id, user_img " +
       "FROM " +
       "( " +
       "SELECT ROW_NUMBER() OVER (ORDER BY regdate DESC) AS rownum, u.user_id, u.user_img " + 
       "FROM wtm.wtm_user u " +
       "INNER JOIN wtm.wtm_user_room r " + 
       "ON u.user_id = r.user_id " + 
       "WHERE r.room_no = ? " +
       "AND chk_date = CONVERT(NVARCHAR(10), wtm.GetLocalDate(DEFAULT), 112) " +
       ") T1 " + sub_query;
       
       mssql.query(sql, sub_params, {
          success: function(results) {
              rtnJson["checked_user"] = results;
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
  var room_title = req.body.room_title;
  var room_desc = req.body.room_desc;
  var start_date = req.body.start_date;
  var end_date = req.body.end_date;
      
  var params =  [user_id, room_title, room_desc, start_date, end_date];     
  var mssql = req.service.mssql;
  var sql = "EXEC wtm.sp_room_create ?, ?, ?, ?, ?";
    
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
  var room_title = req.body.room_title;
  var room_desc = req.body.room_desc;
  var start_date = req.body.start_date;
  var end_date = req.body.end_date;
      
  var params =  [user_id, room_no, room_title, room_desc, start_date, end_date];     
  var mssql = req.service.mssql;
  var sql = "EXEC wtm.sp_room_modify ?, ?, ?, ?, ?, ?";
    
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

// delete query는 sp로 만들어야 될 것 같음, 연관 테이블에서 전부 삭제 필요
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
  var sql = "EXEC wtm.sp_room_del ?, ?";   
  
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