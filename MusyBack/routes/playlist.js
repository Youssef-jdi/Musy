const express = require('express')
const routerPlaylist = express.Router()
const mysql = require('mysql')
const morgan = require('morgan')
const bodyParser = require('body-parser')
routerPlaylist.use(express.static('./public'))
routerPlaylist.use(bodyParser.urlencoded({extended: false}))
routerPlaylist.use(morgan('short'))

const pool = mysql.createPool({
    connectionLimit: 10 ,
    host: 'localhost',
    user: 'root',
    database: 'musydbb'
})
function getConnection(){
    return pool
}

routerPlaylist.post('/addplaylist',(req,res)=>{
   const idUser = req.body.id_user
   const name = req.body.name
   const idTrack = req.body.idTrack
   const nameTrack = req.body.nameTrack
   const urlTrack = req.body.urlTrack
   const albumTrack = req.body.albumTrack
   const artistTrack = req.body.artistTrack
   
   const TrackVerif = "select * from Track where id = ? "
   getConnection().query(TrackVerif,[idTrack],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("errour",err)
            return
        }
        
        if(isEmpty(rows[0])){
            const TrackString = "insert into Track (id,name,artist,url,album) values(?,?,?,?,?)"
            getConnection().query(TrackString,[idTrack,nameTrack,artistTrack,urlTrack,albumTrack],(err,rows,fields)=>{
            if(err){
           res.sendStatus(500)
           console.log("Track error"+err)
           return
                   }
         })

         const PlayListString = "insert into playlist (id_user,name_playlist) values (?,?)"
         getConnection().query(PlayListString, [idUser,name],(err,rows,fields)=>{
             if(err){
                 res.sendStatus(500)
                 console.log("playlist error"+err)
                 return
             }
             const playlist_id = rows.insertId

             const playTrackString = "insert into track_playlist(track_id,playlist_id) values(?,?)"
             getConnection().query(playTrackString,[idTrack,playlist_id],(err,rows,fields)=>{
                 if(err){
                     res.sendStatus(500)
                     console.log("trackplaylist",err)
                     return
                 }
                 res.end()
             })
      
         })

        


        }
        else if(!isEmpty(rows[0])){
            const PlayListString = "insert into playlist (id_user,name_playlist) values (?,?)"
            getConnection().query(PlayListString, [idUser,name],(err,rows,fields)=>{
                if(err){
                    res.sendStatus(500)
                    console.log("playlist error"+err)
                    return
                }
               
                
               const playlist_id = rows.insertId

                const playTrackString = "insert into track_playlist(track_id,playlist_id) values(?,?)"
                getConnection().query(playTrackString,[idTrack,playlist_id],(err,rows,fields)=>{
                    if(err){
                        res.sendStatus(500)
                        console.log("trackplaylist",err)
                        return
                    }
                    res.end()
                })


            })

           


        }
   })

})

routerPlaylist.get('/getplaylist/:idUser',(req,res)=>{
   const idUser = req.params.idUser
   const StringQuery = "SELECT * FROM playlist p INNER JOIN track_playlist tp on tp.playlist_id=p.id INNER JOIN track t on t.id=tp.track_id where p.id_user= ?"
   getConnection().query(StringQuery,[idUser],(err,rows,fields)=>{
       if(err){
           res.sendStatus(500)
           console.log("errourr",err)
           return
       }
       res.json(rows)
   })
})

routerPlaylist.get('/getPlay/:idUser',(req,res)=>{
    const idUser = req.params.idUser
    const StringQuery = "select * from playlist where id_user = ?"
    getConnection().query(StringQuery,[idUser],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log(err)
            return
        }
        res.json(rows)
    })
})

routerPlaylist.get('/getTracks/:playlist_id',(req,res)=>{
    const idplaylist = req.params.playlist_id
    const StringQuery = "select * from track t inner join track_playlist tp on t.id = tp.track_id where tp.playlist_id = ? "
    getConnection().query(StringQuery,[idplaylist],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("errouuuur",err)
            return
        }
        res.json(rows)
    })
})


routerPlaylist.get('/addPlaylist/:id_user/:name',(req,res)=>{
    const id_user = req.params.id_user
    const name = req.params.name
    const StringQuery = "insert into playlist(id_user,name_playlist) values (?,?)"
    getConnection().query(StringQuery,[id_user,name],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("ajout playlist",err)
            return
        }
        res.end()
    })
})

routerPlaylist.get('/addtracktoplaylist/:id/:name/:artist/:album/:playlistid',(req,res)=>{
    const id = req.params.id
    const name = req.params.name
    const artist = req.params.artist
    const url = req.params.url
    const album = req.params.album
    const playlist = req.params.playlistid
    const TrackVerif = "select * from track where id = ?"
    getConnection().query(TrackVerif,[id],(err,rows,fields)=>{
        if(err){
            res.sendStatus(500)
            console.log("errour",err)
            return
        }
        if(isEmpty(rows[0])){
            const ajoutTrack = "insert into track(id,name,artist,album) values(?,?,?,?)"
            getConnection().query(ajoutTrack,[id,name,artist,album],(err,rows,fields)=>{
                if(err){
                    res.sendStatus(500)
                    console.log(err)
                    return
                }
            })
            const playTrackString = "insert into track_playlist(track_id,playlist_id) values(?,?)"
            getConnection().query(playTrackString,[id,playlist],(err,rows,fields)=>{
                if(err){
                    res.sendStatus(500)
                    console.log("trackplaylist",err)
                    return
                }
                res.end()
            })
        }
        else if (!isEmpty(rows[0])){
            const playTrackString = "insert into track_playlist(track_id,playlist_id) values(?,?)"
            getConnection().query(playTrackString,[id,playlist],(err,rows,fields)=>{
                if(err){
                    res.sendStatus(500)
                    console.log("trackplaylist",err)
                    return
                }
                res.end()
            })
        }
    })    

})

routerPlaylist.post('/addtrackplaylist',(req,res)=>{
    const id = req.body.id
    const name = req.body.name
    const artist = req.body.artist
    const url = req.body.url
    const album = req.body.album
    const playlist = req.body.playlistid
    const cover = req.body.cover
    
    const TrackVerif = "select * from track where id = ?"
    getConnection().query(TrackVerif,[id],(err,rows,fields)=>{
        if(err){
          
            console.log("errour",err)
            res.sendStatus(500)
            return
        }
        if(isEmpty(rows[0])){
            console.log("id"+id)
            const ajoutTrack = "insert into track(id,name,artist,album,url,cover) values(?,?,?,?,?,?)"
            getConnection().query(ajoutTrack,[id,name,artist,album,url,cover],(err,rows,fields)=>{
                if(err){
                   
                    console.log("ici"+err)
                    res.sendStatus(500)
                    return
                }
            })
            const playTrackString = "insert into track_playlist(track_id,playlist_id) values(?,?)"
            getConnection().query(playTrackString,[id,playlist],(err,rows,fields)=>{
                if(err){
                    res.sendStatus(500)
                    console.log("trackplaylist",err)
                    return
                }
                res.end()
            })
        }
        else if (!isEmpty(rows[0])){
            const playTrackString = "insert into track_playlist(track_id,playlist_id) values(?,?)"
            getConnection().query(playTrackString,[id,playlist],(err,rows,fields)=>{
                if(err){
                    res.sendStatus(500)
                    console.log("trackplaylist",err)
                    return
                }
                res.end()
            })
        }
    })    

})



 function isEmpty(obj) {
    for(var key in obj) {
        if(obj.hasOwnProperty(key))
            return false;
    }
    return true;
}

module.exports = routerPlaylist