//
//  VideoProfileViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 1/13/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire
import AVFoundation
import AlamofireImage

class VideoProfileViewController: UIViewController, UITableViewDataSource {
    
    var list: NSArray = []
    var avPlayer = AVPlayer()
    var avPlayerLayer: AVPlayerLayer!
    var Musicplayer = AVPlayer()

    @IBOutlet weak var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
         fetchData()

      
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
         return list.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "vidCell") as! VideoProfileCell
        
        
        let video = list[indexPath.row] as! Dictionary<String,Any>
        let path = video["video_path"] as! String
        let fileUrl = URL(string: path)
        
        avPlayer = AVPlayer(url: fileUrl!)
        
        // let playerItem = AVPlayerItem(url: fileUrl!)
        // avPlayer.replaceCurrentItem(with: playerItem)
        avPlayerLayer = AVPlayerLayer(player: avPlayer)
        avPlayerLayer.frame = view.bounds
        avPlayerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        cell.videoView.layer.insertSublayer(avPlayerLayer, at: 0)
        avPlayer.isMuted = true
        
        
        
        
        
        let userid = video["id_user"] as! String
        /*Alamofire.request(connexion.url+"/user/"+userid).responseJSON{
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
   
    @IBAction func Back(_ sender: Any) {
         dismiss(animated: true, completion: nil)
    }
}
