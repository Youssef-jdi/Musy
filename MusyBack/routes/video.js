const express = require('express')
const routerVideo = express.Router()
const mysql = require('mysql')
const morgan = require('morgan')
const bodyParser = require('body-parser')
const multer = require('multer')
routerVideo.use(express.static('./public'))
routerVideo.use(express.static('./Videos/'))
routerVideo.use(express.static('./'))
routerVideo.use(bodyParser.json({limit: "200mb"}))
routerVideo.use(bodyParser.urlencoded({limit: "200mb",extended: true,parameterLimit:200000,type:'application/x-www-form-urlencoding'}))    
routerVideo.use(morgan('short'))
var path = require('path');

routerVideo.use(express.static(path.resolve('./videos')));

const storages = multer.diskStorage({
    destination : function(req,file,cb){
        cb(null,'./Videos/');
    },
    filename : function(req,file,cb){
        cb(null, new Date().toISOString().replace(/:/g, '-') + file.originalname);
    }
});

const storage = multer.diskStorage({
    destination : function(req,file,callback){
        callback('videos');
    },
    filename : function(req,file,callback){
        callback(null, file.originalname);
    }
})
const upload = multer({
    storage : storages,

})

const pool = mysql.createPool({
    connectionLimit: 10 ,
    host: 'localhost',
    user: 'root',
    database: 'musydbb'
})
function getConnection(){
    return pool
}

routerVideo.post('/video/post',upload.single('videoURL'),(req,res)=>{
const id = req.body.id_user    
const video = "http://192.168.43.70:3003/videos/"+req.file.filename
const track = req.body.url 
const StringQuery = "insert into videos(id_user,video_path,track) values(?,?,?)"
getConnection().query(StringQuery,[id,video,track],(err,rows,fields)=>{
    if(err){
        res.sendStatus(500)
        console.log("erreur"+err)
        return
    }
    res.end()
})
 
})

routerVideo.get('/getvideo/',(req,res)=>{
    const StringQuery = "select * from videos where id_user=111116293811278290397"
    getConnection().query(StringQuery,[],(err,rows,fields)=>{
        if(err){
            console.log("erreru"+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
    })
})

routerVideo.get('/getvideos/:id_user',(req,res)=>{
    const id_user = req.params.id_user
    const StringQuery = "select * from videos v where id_user in (select f.following_id from following f where f.user_id = "+id_user+" ) order by date";
    getConnection().query(StringQuery,[id_user],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("el erreur"+err)
            return
        }
        res.json(rows)
    })
})


module.exports = routerVideo
