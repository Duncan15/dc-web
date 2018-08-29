var fs = require('fs');
var createError = require('http-errors');
var express = require('express');
var path = require('path');
var logger = require('morgan');
var rfs = require('rotating-file-stream');


var app = express();

//for log
var logDirectory = path.join(__dirname, "log");
fs.existsSync(logDirectory) || fs.mkdirSync(logDirectory);// ensure log directory exists
var accessLogStream = rfs('access.log', {// create a rotating write stream
    interval: '1d', // rotate daily
    path: logDirectory
})
app.use(logger('dev',{
  stream:accessLogStream,
}));
app.use(express.static(path.join(__dirname, "public")));
app.all("/*",function (req,res,next) {
    res.sendFile(path.join(__dirname,"public/html/home.html"));
});

//以下为默认的错误处理中间件
// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
