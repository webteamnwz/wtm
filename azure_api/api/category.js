function getCategory(req, res) {
  var errman = require('./common').errman; 
  var jsonman = require('./common').jsonman;

  var category_no = req.query.category_no;
  var params = [category_no];      
  var mssql = req.service.mssql;
    
  var sql = "SELECT " + 
            "category_no, category_name, " +
            "( " +
            "  SELECT COUNT(*) " + 
            "  FROM wtm_room_category " +
            "  WHERE category_no = cat.category_no " +
            ") AS room_cnt " +
            "FROM wtm_category AS cat " +
            "WHERE category_no = ?"
              
   mssql.query(sql, params, {
     success: function(results) { 
       var rtnJson = jsonman['success']();
       rtnJson["categories"] = results; // record set 자체가 객체 배열로 넘어온다.              
       res.send(statusCodes.OK, rtnJson);
     },
     error: function(err) {        
       res.send(statusCodes.OK, errman['err'](999, err)); 
     }   
   });      
}

function getCategories(req, res) {
  var errman = require('./common').errman; 
  var jsonman = require('./common').jsonman;
      
  var mssql = req.service.mssql;
  var sql = "SELECT " + 
            "category_no, category_name, " +
            "( " +
            "  SELECT COUNT(*) " + 
            "  FROM wtm_room_category " +
            "  WHERE category_no = cat.category_no " +
            ") AS room_cnt " +
            "FROM wtm_category AS cat";
              
  mssql.query(sql, {
    success: function(results) { 
      var rtnJson = jsonman['success']();
      rtnJson["categories"] = results; // record set 자체가 객체 배열로 넘어온다.
      /*
      rtnJson["categories"] = [];
      if(results.length > 0) {
        for(var idx=0; idx<results.length; idx++) {
          rtnJson["categories"][idx] = {
            "category_no":results[idx].category_no, 
            "category_name": results[idx].category_name
          }  
        }   
      }
      */             
      res.send(statusCodes.OK, rtnJson);
    },
    error: function(err) {        
      res.send(statusCodes.OK, errman['err'](999, err)); 
    }   
  });     
}

exports.get = function(req, res) { 
  var errman = require('./common').errman;     

  // 인증
  var authman = require('./common').authman;
  var user_id = authman['auth'](req.user);
  if(user_id === '') {
    res.send(statusCodes.OK, errman['err'](998, ''));  
  }   
  
  var category_no = req.query.category_no;
                          
  if(typeof category_no == 'undefined')
    getCategories(req, res);
  else
    getCategory(req, res);      
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
  
  var category_name = req.body.category_name;   
  var params =  [category_name];                       
  var mssql = req.service.mssql;
  var sql = "INSERT INTO " + 
  "wtm_category(category_name) " +
  "VALUES(?)"; 
    
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
  
  var category_no = req.query.category_no;
  var category_name = req.body.category_name;    
  var params =  [category_name, category_no];                       
   
  var mssql = req.service.mssql;
  var sql = "UPDATE " + 
  "wtm_category " +
  "SET category_name = ? " +
  "WHERE category_no = ?" 
    
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
/*
exports.delete = function(req, res) {
  var errman = require('./common').errman; 
  var jsonman = require('./common').jsonman;
  
  // 인증
  var authman = require('./common').authman;
  var user_id = authman['auth'](req.user);
  if(user_id === '') {
    res.send(statusCodes.OK, errman['err'](998, ''));  
  }   
  
  var category_no = req.query.category_no;   
  var params =  [category_no];                       
   
  var mssql = req.service.mssql;
  var sql = "DELETE wtm_category WHERE category_no = ?"; 
      
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
*/
