const express = require('express')
const routerUser = express.Router()
const mysql = require('mysql')
const morgan = require('morgan')
const bodyParser = require('body-parser')
const multer = require('multer')
routerUser.use(express.static('./public'))
routerUser.use(bodyParser.urlencoded({extended: false}))
routerUser.use(morgan('short'))

const pool = mysql.createPool({
    connectionLimit: 10 ,
    host: 'localhost',
    user: 'root',
    database: 'musydbb'
})
function getConnection(){
    return pool
}

routerUser.post('/user_create',(req,res)=>{

    const id = req.body.id
    const email = req.body.eMail
    const nickName = req.body.nickName
    const picture = req.body.Picture
    const queryString = "Insert into user (id,email,nickname,picture) values (?,?,?,?)"
    getConnection().query(queryString,[id,email,nickName,picture],(err,results,fields)=>{
        if(err){
            console.log("failed"+err)
            res.sendStatus(500)
            return
        }
        res.end()

    })
   

})

routerUser.get('/checkuser/:id',(req,res)=>{
    const id = req.params.id
    const StringQuery = "select * from user where id = ?"
    getConnection().query(StringQuery,[id],(err,results,fields)=>{
        if(err){
            console.log('errour'+err)
            res.sendStatus(500)
            return
        }
        if(results.length > 0){
            res.json(results)
        }else{
           // res.json(results)
            res.send(results)
        }
        
    })
})

routerUser.get('/user/:id',(req,res)=>{
    const  userId = req.params.id
    const  queryString = "Select * from user WHERE id = ?"
    getConnection().query(queryString,[userId],(err,rows,fields)=>{
        if(err){
           console.log("failed to query"+err)
            res.sendStatus(500)
            return
        }
        res.json(rows)
    })    
})

routerUser.get('/users',(req,res)=>{
    const queryString = "Select * from user"
    getConnection().query(queryString,(err,rows,fields)=>{
        if(err){
            res.sendStatus(50)
            return
        }
        
        res.json(rows)
    })
})

routerUser.delete('/userdelete/:id',(req,res)=>{
   const Userid = req.params.id
   const queryString = "Delete from user where id = ?"
   getConnection().query(queryString,[Userid],(err,rows,fields)=>{
       if(err){
           res.sendStatus(500)
           return
       }
       console.log("success deleting")
       res.end()
   }) 
})

routerUser.put('/updateuser/:id/:email/:nickname/:password/:country/:picture',(req,res)=>{
    const userId = req.params.id
    const picture = req.params.picture
    const email = req.params.email
    const nickname = req.params.nickname
    const password = req.params.password
    const country = req.params.country
    const queryString = "Update user set email = ? nickname = ? , password = ?  , country = ? ,  picture = ? where id = ?  "                 
    getConnection().query(queryString,[email,nickname,password,country,picture,userId],(err,rows,fields)=>{
        if(err){
            console.log("failed",err)
            res.sendStatus(500)
            return
        }
        console.log("succesfull")
        res.end()

    })
})


/*routerUser.get('/user/follow/:id_user/:id_following',(req,res)=>{
    const id_user = req.params.id_user
   const id_following = req.params.id_following
  
   const queryString = 'insert into following(user_id,following_id) values (?,?)'
   getConnection().query(queryString,[id_following,id_user],(err,rows,field)=>{
       if(err){
           console.log("errour"+err)
           res.sendStatus(500)
           return
       }
       
      res.end()
   })
   const queryString2 = 'insert into followers(user_id,follower_id) values (?,?)'
        getConnection().query(queryString2,[id_user,id_following],(err,rows,fields)=>{
            if(err){
                res.sendStatus(500)
                console.log("errour"+err)
                return
            }
            res.end()
        })
  
})*/

routerUser.post('/user/follow',(req,res)=>{
    const id_user = req.body.id_user
   const id_following = req.body.id_following
   const queryString = 'insert into following(user_id,following_id) values (?,?)'
   getConnection().query(queryString,[id_following,id_user],(err,rows,field)=>{
       if(err){
           console.log("errour"+err)
           res.sendStatus(500)
           return
       }
       
     // res.end()
   })
   const queryString2 = 'insert into followers(user_id,follower_id) values (?,?)'
        getConnection().query(queryString2,[id_user,id_following],(err,rows,fields)=>{
            if(err){
                res.sendStatus(500)
                console.log("errour2"+err)
                return
            }
            res.end()
        })
})



routerUser.get('/user/deletefollow/:id_user/:id_following',(req,res)=>{
    const id_user = req.params.id_user
    const id_following = req.params.id_following
      console.log(id_user)
      console.log(id_following)
  
   const queryString = 'delete from following where user_id = ? and following_id = ?'
   getConnection().query(queryString,[id_following,id_user],(err,rows,field)=>{
       if(err){
           console.log("errour"+err)
           res.sendStatus(500)
           return
       }
       //res.end()
       
     
   })
   const queryString2 = 'delete from followers where user_id = ? and follower_id = ? '
        getConnection().query(queryString2,[id_user,id_following],(err,rows,fields)=>{
            if(err){
                res.sendStatus(500)
                console.log("errour"+err)
                return
            }
            res.end()
        })
  
})


routerUser.get('/user/getfollowers/:user_id',(req,res)=>{
    id_user = req.params.user_id
    const queryString = "select * from followers where user_id = ?"
    getConnection().query(queryString,[id_user],(err,rows,fields)=>{
        if(err){
            sendStatus(500)
            return
        }
        res.json(rows)
    })
})

routerUser.get('/user/getfollowing/:user_id',(req,res)=>{
    id_user = req.params.user_id
    const queryString = "select * from following where user_id = ? "
    getConnection().query(queryString,[id_user],(err,row,fields)=>{
        if(err){
            sendStatus(500)
            return
        }
        res.json(rows)
    })
})


routerUser.get('/numberfollowers/:id_user',(req,res)=>{
    const id_user = req.params.id_user
    const StringQuery = "SELECT COUNT(*) AS followers FROM user u inner join followers f on f.user_id = u.id WHERE user_id = ?"
    getConnection().query(StringQuery,[id_user],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("erreur number"+err)
            return
        }
        res.json(rows[0].followers)
    })
})

routerUser.get('/numberfollowing/:id_user',(req,res)=>{
    const id_user = req.params.id_user
    const StringQuery = "select count(*) as following from user u inner join following f on f.user_id=u.id where user_id = ?"
    getConnection().query(StringQuery,[id_user],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("erreur number following"+err)
            return
        }
        res.json(rows[0].following)
    })
})


routerUser.get('/userfollowers/:id_user',(req,res)=>{
    const id_user = req.params.id_user
    const StringQuery = "SELECT u.* FROM user u inner join followers f on f.follower_id = u.id WHERE f.user_id = ?"
    getConnection().query(StringQuery,[id_user],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("erreur number"+err)
            return
        }
        res.json(rows)
    })
})

routerUser.get('/userfollowing/:id_user',(req,res)=>{
    const id_user = req.params.id_user
    const StringQuery = "select u.* from user u inner join following f on f.following_id = u.id where user_id = ?"
    getConnection().query(StringQuery,[id_user],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("erreur number following"+err)
            return
        }
        res.json(rows)
    })
})

routerUser.get('/search/:name',(req,res)=>{
   const name = req.params.name
   console.log(name)
   const StringQuery = "select * from user where nickname like '%"+name+"%'"
   getConnection().query(StringQuery,[name],(err,rows,fields)=>{
       if(err){
           res.sendStatus(500)
           console.log("erreur"+err)
           return
       }
       res.json(rows)
   })
})

routerUser.get('/checkuserfollow/:id_user/:id_following',(req,res)=>{
    const id_user = req.params.id_user
    const id_following = req.params.id_following
    const StringQuery = "select * from following where user_id = ? and following_id = ? "
    getConnection().query(StringQuery,[id_user,id_following],(err,rows,fields)=>{
        if(err){
            console.log("erreur sds"+err)
            res.sendStatus(500)
            return
        }
        if(isEmpty(rows)){
            res.json("false")
        }
        else {
            res.json("true")
        }
    })
})


routerUser.use(express.static('./Images/'))
routerUser.use(express.static('./'))
const storage = multer.diskStorage({
    destination : function(req,file,cb){
        cb(null,'./Images/');
    },
    filename : function(req,file,cb){
        cb(null, new Date().toISOString().replace(/:/g, '-') + file.originalname);
    }
});



const upload = multer({storage : storage});
routerUser.post('/upload/',upload.single('image'),(req,res)=>{
 // console.log(req.file.filename)
  res.end()
  const userId = req.body.userId
  const picture = "http://192.168.43.70:3003/Images/"+req.file.filename
  const queryString = "Update user set  picture = ? where id = ?"              
  getConnection().query(queryString,[picture,userId],(err,rows,fields)=>{
      console.log(userId)
      console.log(picture)
      if(err){
          console.log("failedssss",err)
          res.sendStatus(500)
          return
      }
      console.log("succesfull")
      res.end()

  })

})




function isEmpty(obj) {
    for(var key in obj) {
        if(obj.hasOwnProperty(key))
            return false;
    }
    return true;
}





module.exports = routerUser
