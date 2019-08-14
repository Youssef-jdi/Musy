//
//  MusicOthersViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/5/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire
import AVFoundation
class MusicOthersViewController: UIViewController, UITableViewDataSource {

    var list: NSArray = []
    var avPlayer = AVPlayer()
    var avPlayerLayer: AVPlayerLayer!
    var Musicplayer = AVPlayer()

    var followStatus: FollowStatus = .follow
    let connetedUser = users.connected_user
    var cnt:String?
    @IBOutlet weak var nbFollowers: UILabel!
    @IBOutlet weak var profilepic: UIImageView!
    
    @IBOutlet weak var nbFollowing: UILabel!
    
    @IBOutlet weak var userName: UILabel!
    @IBOutlet weak var btnFollow: UIButton!
     var TvShows:NSArray =  []
    

    @IBOutlet weak var tableView: UITableView!
    var idUser: String?
   
    override func viewDidLoad() {
        super.viewDidLoad()
        print(idUser!)
        check()
        getuser()
        getnbreFollowers()
        getnbreFollowing()
        setup()
        fetchData()
    }
    
    
    func setup()  {
        self.profilepic.layer.cornerRadius = self.profilepic.frame.height/2;
        self.profilepic.layer.masksToBounds = true
    }
    
    func check() {
        Alamofire.request(connexion.url+"/checkuserfollow"+"/"+users.connected_user+"/"+idUser!).responseJSON{
            response in
            self.cnt = response.result.value as! String
            print(self.cnt)
            if(self.cnt! == "true"){
                print("true")
                DispatchQueue.main.async {
                     self.followStatus = .unfollow
                    self.btnFollow?.setTitle("unfollow",for: .normal)
                }
               
            }
            else{
                DispatchQueue.main.async {
                    self.followStatus = .follow
                }
            }
        }
    }
    
    @IBAction func back(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    
    func getuser(){
        Alamofire.request(connexion.url+"/user/"+idUser!).responseJSON{
            response in
            print(response.result.value)
            let users = response.result.value as! [[String:Any]]
            self.userName.text = users[0]["nickname"]! as! String
            let a = users[0]["picture"]! as! String
            let b = URL(string: a)
            self.profilepic.af_setImage(withURL: b!)
            
            
            
        }
    }
    
    func getnbreFollowers(){
        Alamofire.request(connexion.url+"/numberfollowers/"+idUser!).responseJSON{
            response in
            let a = response.result.value! as! Int
            let b = String(a)
            self.nbFollowers.text = b
            
            
            
        }
    }
    
    func getnbreFollowing(){
        
        Alamofire.request(connexion.url+"/numberfollowing/"+idUser!).responseJSON{
            response in
            let a = response.result.value! as! Int
            let b = String(a)
            self.nbFollowing.text = b
            
            
            
        }
        
    }
    
    
    
    
    
    
    @IBAction func bntFollow(_ sender: Any) {
        
        followStatus.toggle()
        if followStatus == .unfollow{
            btnFollow?.setTitle("unfollow",for: .normal)
            Follow()
        }
        else if followStatus == .follow{
            btnFollow?.setTitle("follow",for: .normal)
            Unfollow()
            
        }
        
    }
    

    func Follow(){
        let postsURLEndPoint: String = "\(connexion.url)/user/follow/"
        let newPost: Parameters = ["id_user" : idUser! , "id_following": connetedUser]
        let url = URL(string: postsURLEndPoint)
        
        Alamofire.request(url!, method: .post, parameters: newPost )
            .responseString { response in
                
                switch response.result
                {
                case .failure(let error):
                    if let data = response.data {
                        print("Print Server Error: " + String(data: data, encoding: String.Encoding.utf8)!)
                    }
                    print(error)
                    
                case .success(let value):
                    
                    print(value)
                }
        }
        
    }
    func Unfollow(){
        let postsURLEndPoint: String = "\(connexion.url)/user/deletefollow/\(idUser!)/\(connetedUser)"
        // let newPost: Parameters = ["id_user" : "8", "id_following": "9"]
        let url = URL(string: postsURLEndPoint)
        
        Alamofire.request(postsURLEndPoint, method: .get, parameters: nil )
            .responseString { response in
                
                switch response.result
                {
                case .failure(let error):
                    if let data = response.data {
                        print("Print Server Error: " + String(data: data, encoding: String.Encoding.utf8)!)
                    }
                    print(error)
                    
                case .success(let value):
                    
                    print(value)
                }
        }
        
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
         return list.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "musicotherCell") as! VideoProfileCell
        
        
        let video = list[indexPath.row] as! Dictionary<String,Any>
        let path = video["video_path"] as! String
        let fileUrl = URL(string: path)
        
        avPlayer = AVPlayer(url: fileUrl!)
        
        // let playerItem = AVPlayerItem(url: fileUrl!)
        // avPlayer.replaceCurrentItem(with: playerItem)
        avPlayerLayer = AVPlayerLayer(player: avPlayer)
        avPlayerLayer.frame = view.bounds
        avPlayerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
       // cell.videoView.layer.insertSublayer(avPlayerLayer, at: 0)
        avPlayer.isMuted = true
        
        
        
        
        
        let userid = video["id_user"] as! String
       /* Alamofire.request(connexion.url+"/user/"+userid).responseJSON{
            response in
            let users = response.result.value as! [[String:Any]]
            let nickname = users[0]["nickname"] as! String
            let picture = users[0]["picture"] as! String
            let img = cell.viewWithTag(6) as! UIImageView
            img.layer.cornerRadius = img.frame.height/2;
            img.layer.masksToBounds = true
            let label = cell.viewWithTag(5) as! UILabel
            let u = URL(string: picture)
            img.af_setImage(withURL: u!)
            label.text = nickname
            
        }*/
        let track = video["track"] as! String
        let playerItemmusic:AVPlayerItem = AVPlayerItem(url: URL.init(string: track)!  )
        self.Musicplayer.replaceCurrentItem(with: playerItemmusic)
        let deadlineTime = DispatchTime.now() + .seconds(2)
        DispatchQueue.main.asyncAfter(deadline: deadlineTime) {
            self.avPlayer.play()
            self.Musicplayer.play()
        }
        
        
        
        
        return cell
    }

    func fetchData()  {
        Alamofire.request(connexion.url+"/getvideo/").responseJSON{
            response in
            self.list = response.result.value as! NSArray
            self.tableView.reloadData()
        }
    }
    
    
    
}
