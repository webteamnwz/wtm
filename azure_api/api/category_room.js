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
  var category_no = req.query.category_no;
  var s_no = req.query.s_no;
  var e_no = req.query.e_no;
  var sub_query = '';
  var cnt_params = [category_no];
  var params = [user_id, user_id, category_no];
    
  // paging 
  if (s_no !== undefined || e_no !== undefined) {
    sub_query = 'WHERE rownum BETWEEN ? AND ?';
    params = [user_id, user_id, category_no, s_no, e_no];   
  }
  
  var mssql = req.service.mssql;
  var sql = "SELECT COUNT(*) AS room_cnt " +
            "FROM wtm.wtm_room rm " +
            "INNER JOIN wtm.wtm_room_category rc " +
            "ON rm.room_no = rc.room_no " +
            "WHERE  rc.category_no = ?"; 
  
  mssql.query(sql, cnt_params, {
      success: function(results) {    
        sql = "SELECT room_no, room_title, room_desc, start_date, end_date, room_manager_id, " +
              "( " +
              "SELECT CASE WHEN (COUNT(*) > 0) THEN 'true' ELSE 'false' END " +  
              "FROM wtm.wtm_user_room " +
              "WHERE room_no = T1.room_no " + 
              "AND user_id = ? " +
              ") AS is_joined, " +        
              "( " +
              "SELECT CASE WHEN (COUNT(*) > 0) THEN 'true' ELSE 'false' END " +  
              "FROM wtm.wtm_user_room " +
              "WHERE room_no = T1.room_no " + 
              "AND user_id = ? " +
              "AND chk_date = CONVERT(NVARCHAR(8), wtm.GetLocalDate(DEFAULT), 112) " +
              ") AS is_checked " +
              "FROM " +
              "( " +
              "SELECT ROW_NUMBER() OVER (ORDER BY rm.room_no DESC) AS rownum, rm.* " + 
              "FROM wtm.wtm_room rm " +
              "INNER JOIN wtm.wtm_room_category rc " + 
              "ON rm.room_no = rc.room_no " +
              "WHERE rc.category_no = ? " + 
              ") T1 " + sub_query;
         var rtnJson = jsonman['success']();
         rtnJson["room_cnt"] = results[0].room_cnt;
         
        mssql.query(sql, params, {    
          success: function(results) {
            rtnJson["room"] = results;               
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