const express = require('express')
const routerCommentaire = express.Router()
const mysql = require('mysql')
const morgan = require('morgan')
const bodyParser = require('body-parser')
routerCommentaire.use(express.static('./public'))
routerCommentaire.use(bodyParser.urlencoded({extended: false}))
routerCommentaire.use(morgan('short'))

const pool = mysql.createPool({
    connectionLimit: 10 ,
    host: 'localhost',
    user: 'root',
    database: 'musydbb'
})
function getConnection(){
    return pool
}

routerCommentaire.post('/addCommentaire',(req,res)=>{
      const  StringQuery = "insert into comment_album (comment_text,comment_date,album_id) values (?,?,?)"
       const commenttext = req.body.comment_text
      const  commentdate = req.body.comment_date
      const  albumid = req.body.album_id
        getConnection().query(StringQuery,[commenttext,commentdate,albumid],(err,rows,fields)=>{
            if(err){
                console.log("failed "+err)
                res.sendStatus(500)
                return
            }
            res.end()
        })

})

routerCommentaire.get('/commentaires',(req,res)=>{
  const  StringQuery = "Select * from comment_album"
    getConnection().query(StringQuery,(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("failed  "+err)
            return 
        }

        res.json(rows)
    })
})

routerCommentaire.get('/comment/:id',(req,res)=>{
   const StringQuery = "select * from comment_album where id = ?"
   const id = req.params.id 
   getConnection().query(StringQuery,id,(err,rows,fields)=>{
       if(err){
           res.sendStatus(500)
           console.log("failed "+err)
           return
        }
        res.json(rows)
   })
})

routerCommentaire.put('/updatecomment',(req,res)=>{
    const StringQuery = "update comment_album set comment_text = ? , comment_date = ? where id = ? "
    const comment = req.body.comment_text
    const date = req.body.comment_date
    const id = 1
    getConnection().query(StringQuery,[comment,date,id],(err,rows,fields)=>{
        if(err){
            console.log("failed "+err)
            res.sendStatus(500)
            return
        }
        res.end()
    })
    
})


module.exports = routerCommentaire