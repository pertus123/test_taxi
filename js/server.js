"use strict";
const express = require('express');
const app = express();
var mysql      = require('mysql');
var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : 'taerin',
  database : 'taerin'
});
connection.connect();

app.post('/post', (req, res) => {
  var inputData;
  var pass;

req.on('error',(err)=>{
  console.error(err);
  }).on('data', (data) => {
  inputData = JSON.parse(data);
  pass = String(inputData.db_test);
}).on('end', () => {
  console.log("user_id : "+pass);
  });

setTimeout(function(){
connection.query('SELECT * from tb01 where nu = '+pass, function(err, rows, fields) {
    if (!err)  console.log('The solution is: ', rows);
    else  console.log('Error while performing Query.', err);
res.write(String(rows[0].name_01));
res.end();
  });
}, 0)
});

var server = app.listen(3000, function () {
   var host = server.address().address
   var port = server.address().port
   console.log("Example app listening at http://%s:%s", host, port)
});
