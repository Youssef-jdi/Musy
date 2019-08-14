//
//  MusicPlayerViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 1/13/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import UIKit
import AlamofireObjectMapper
import Alamofire
import AVFoundation
import AlamofireImage
import PullUpController
import Toast_Swift
class MusicPlayerViewController: UIViewController {
    
    var album: Album!
    var playlist: String!
    private var originalPullUpControllerViewSize: CGSize = .zero

   
    
    @IBOutlet weak var playbtn: UIButton!
    @IBOutlet weak var smallImage: UIImageView!
    
    @IBOutlet weak var imgblur: UIImageView!
    @IBOutlet weak var durationSlider: UISlider!
    
    @IBOutlet weak var nomTrack: UILabel!
    @IBOutlet weak var nomArtist: UILabel!
    @IBOutlet weak var firstTime: UILabel!
    
    //let url:String = "https://api.deezer.com/playlist/1362525375/tracks?fbclid=IwAR3pBYv_HqB4gEwV7VBKifR1tYvPSQr64aPxJ592jYh2y7N-NbTOD8G1T18"
    var trackobject:TrackObject?
    var TrackArray : [Track]?
    var player:AVPlayer?
    var playerItem:AVPlayerItem?
    var i:Int = 0
    var taswiraAlbum:String?
    override func viewDidLoad() {
        super.viewDidLoad()
        print(playlist)
        fetchData()
        NotificationCenter.default.addObserver(self, selector: #selector(playfrom(notification:)),name: Notification.Name(rawValue: "play"), object: nil)
    }
    
    
    @IBAction func back(_ sender: Any) {
        player!.pause()
        dismiss(animated: true, completion: nil)
        
        
    }
    
    
    func setup()  {
        self.smallImage.layer.cornerRadius = self.smallImage.frame.height/2;
        self.smallImage.layer.masksToBounds = true
    }
    
    @objc public func playfrom(notification : Notification){
        print("jiit")
        print(notification.userInfo ?? "")
        if let dict = notification.userInfo as NSDictionary? {
            if let id = dict["index"] as? Int{
                player?.pause()
                play(i: id)
            }
        }
    }
    

    @IBAction func PlayOrPause(_ sender: Any) {
        if player?.rate == 0
        {
            player!.play()
            //playButton!.setImage(UIImage(named: "player_control_pause_50px.png"), forState: UIControlState.Normal)
            playbtn!.setImage(UIImage(named: "pause (1)"), for: .normal)
        } else {
            player!.pause()
            //playButton!.setImage(UIImage(named: "player_control_play_50px.png"), forState: UIControlState.Normal)
            playbtn!.setImage(UIImage(named: "play-button (1)"), for: .normal)
        }
        
    }
    @IBAction func Next(_ sender: Any) {
           playnext()
    }
    
    func playnext()  {
        
        i = i + 1
        if i  < TrackArray!.count {
            player!.pause()
            play(i: i)
            player!.play()
            
            
        }
        else {
            i = 0
            player!.pause()
            play(i: i)
            player!.play()
            
        }
    }
    
    
    
    @IBAction func Previous(_ sender: Any) {
        i = i - 1
        if i >= 0 {
            player!.pause()
            play(i: i)
            player!.play()
        }
        else {
            i = TrackArray!.endIndex - 1
            player!.pause()
            play(i: i)
            player!.play()
        }
    }
    
    func fetchData(){
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        
        Alamofire.request(playlist!).responseObject { (response: DataResponse<TrackObject>) in
            UIApplication.shared.isNetworkActivityIndicatorVisible = false
            switch response.result {
            case .success:
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + .seconds(2)){
                    self.trackobject = response.result.value
                    self.TrackArray = self.trackobject?.tracks
                    self.setup()
                    
                    
                    self.addPullUpController()
                    self.play(i: 0)
                }
                
                
                
            case .failure(let error):
                print(error)
            }
        }
    }
    
    public func play(i : Int)  {
        
        print(i)
        print(self.TrackArray![i].preview!)
        let url = URL(string: (self.TrackArray![i].preview! ))
        let playerItem:AVPlayerItem = AVPlayerItem(url: url! )
        self.player = AVPlayer(playerItem: playerItem)
        let playerLayer=AVPlayerLayer(player: self.player!)
        playerLayer.frame=CGRect(x:0, y:0, width:10, height:50)
        self.view.layer.addSublayer(playerLayer)
        if(playlist.contains("album")) {
            let urlimage = URL(string: taswiraAlbum! )
            self.smallImage.af_setImage(withURL: urlimage!)
        }
        else{
            let urlimage = URL(string: self.TrackArray![i].album!.cover! )
            print(urlimage!)
            self.smallImage.af_setImage(withURL: urlimage! )
        }
       
        
        self.nomArtist.text = self.TrackArray![i].artist!.name!
        ///////////////////
        
        
        DispatchQueue.main.async {
            let blureffect = UIBlurEffect(style: .dark)
            let blurview =  UIVisualEffectView(effect: blureffect)
            if(self.playlist.contains("album")){
                let urlImageBlur = URL(string: self.taswiraAlbum!)
                self.imgblur.af_setImage(withURL: urlImageBlur!)
            }
            else {
                let urlImageBlur = URL(string: self.TrackArray![i].album!.cover!)
                self.imgblur.af_setImage(withURL: urlImageBlur!)
            }
            
            blurview.frame = self.imgblur.bounds
            self.imgblur.addSubview(blurview)
            self.durationSlider.minimumValue = 0
            let duration : CMTime = playerItem.asset.duration
            let seconds : Float64 = CMTimeGetSeconds(duration)
            self.durationSlider.maximumValue = Float(seconds)
            self.player?.play()
            
            self.nomTrack.text = (self.TrackArray![i].title)
            
            self.durationSlider.isContinuous = false
            self.durationSlider.addTarget(self, action: #selector(self.playbackSliderValueChanged), for: .valueChanged)
            
            self.player!.addPeriodicTimeObserver(forInterval: CMTimeMakeWithSeconds(1, preferredTimescale: 1), queue: DispatchQueue.main) { (CMTime) -> Void in
                if self.player!.currentItem?.status == .readyToPlay {
                    let time : Float64 = CMTimeGetSeconds(self.player!.currentTime());
                    self.durationSlider!.value = Float ( time );
                    
                    let b : Int = Int(time)
                    self.firstTime.text = String(format: "%02d", b)
                    if b == 30 {
                        self.playnext()
                    }
                }
            }
        }
        
        
        
    }
    
    
    @objc func playbackSliderValueChanged(_ playbackSlider:UISlider){
        
        let seconds : Int64 = Int64(playbackSlider.value)
        let targetTime:CMTime = CMTimeMake(value: seconds, timescale: 1)
        
        player!.seek(to: targetTime)
        
        if player!.rate == 0
        {
            player?.play()
        }
        
        
    }
    
    
    private func khra() -> TableViewPlayListViewController{
        let currentPullUpController = children
            .filter({ $0 is TableViewPlayListViewController }).first as? TableViewPlayListViewController
        let pullUpController: TableViewPlayListViewController = currentPullUpController ?? UIStoryboard(name: "Main",bundle: nil).instantiateViewController(withIdentifier: "TableViewPlayListViewController") as! TableViewPlayListViewController
        if originalPullUpControllerViewSize == .zero {
            originalPullUpControllerViewSize = pullUpController.view.bounds.size
        }
        //   pullUpController.portraitSize = CGSize(width: self.view.frame.width ,height: self.view.frame.height)
        //  pullUpController.updatePreferredFrameIfNeeded(animated: true)
        
        return pullUpController
    }
    
    private func addPullUpController() {
        let pullUpController = khra()
        _ = pullUpController.view
        pullUpController.TrackArray = self.TrackArray
        
        addPullUpController(pullUpController, initialStickyPointOffset: pullUpController.initialPointOffset, animated: true)
        
    }
    
    @IBAction func lipSync(_ sender: Any) {
         player!.pause()
         performSegue(withIdentifier: "toCamera", sender: sender)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "toCamera" {
            let camera = segue.destination as! CameraFoundationViewController
            camera.preview = self.TrackArray![i].preview
        }
        if segue.identifier == "toPlayList" {
            let playlist = segue.destination as! PlayListViewController
            
            playlist.trackId = self.TrackArray![i]
        }
    }
    
    @IBAction func addToPlaylist(_ sender: Any) {
         player!.pause()
         performSegue(withIdentifier: "toPlayList", sender: nil)
    }
    
}
