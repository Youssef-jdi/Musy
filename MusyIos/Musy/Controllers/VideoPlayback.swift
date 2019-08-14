//
//  VideoPlayback.swift
//  Musyy
//
//  Created by Youssef Jdidi on 12/8/18.
//  Copyright © 2018 Youssef Jdidi. All rights reserved.
//

import UIKit
import AVFoundation
import Photos
import Alamofire
class VideoPlayback: UIViewController {
    let avPlayer = AVPlayer()
    var avPlayerLayer: AVPlayerLayer!
    var videoURL: URL!
    var preview: String?
    var Musicplayer = AVPlayer()
    override var prefersStatusBarHidden: Bool {
        return true
    }
    @IBOutlet weak var videoView: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        avPlayerLayer = AVPlayerLayer(player: avPlayer)
        avPlayerLayer.frame = view.bounds
        avPlayerLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        videoView.layer.insertSublayer(avPlayerLayer, at: 0)
        
        view.layoutIfNeeded()
        // let audioUrl = NSURL(string: preview!)
        let playerItemmusic:AVPlayerItem = AVPlayerItem(url: URL.init(string: preview!)!  )
        //  Musicplayer = AVPlayer(playerItem: playerItemmusic)
        Musicplayer.replaceCurrentItem(with: playerItemmusic)
        
        let playerItem = AVPlayerItem(url: videoURL as URL)
        avPlayer.replaceCurrentItem(with: playerItem)
        // avPlayer.play()
        let deadlineTime = DispatchTime.now() + .seconds(2)
        DispatchQueue.main.asyncAfter(deadline: deadlineTime) {
            self.playMedia()
        }
        
        // mergeFilesWithUrl(videoUrl: videoURL! as NSURL, audioUrl: audioUrl!)
        NotificationCenter.default.addObserver(self, selector: #selector(playerItemDidReachEnd), name: NSNotification.Name.AVPlayerItemDidPlayToEndTime, object: self.avPlayer.currentItem)
        // Do any additional setup after loading the view.
    }
    
    func playMedia()  {
        Musicplayer.play()
        avPlayer.play()
        
    }
    
    
    @objc fileprivate func playerItemDidReachEnd(_ notification: Notification) {
        if self.avPlayer != nil {
            self.avPlayer.seek(to: CMTime.zero)
            self.Musicplayer.seek(to: CMTime.zero)
            self.Musicplayer.play()
            self.avPlayer.play()
        }
    }
    
    
    @IBAction func back(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func saveVideo(_ sender: Any) {
        PHPhotoLibrary.shared().performChanges({
            PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: self.videoURL)
        }) { saved, error in
            if saved {
                let alertController = UIAlertController(title: "Your video was successfully saved", message: nil, preferredStyle: .alert)
                let defaultAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                alertController.addAction(defaultAction)
                self.present(alertController, animated: true, completion: nil)
            }
        }
    }
    
    
    @IBAction func share(_ sender: Any) {
        uploadVideo()
        /* Alamofire.upload(self.videoURL, to: "http://localhost:3003/video/post").responseJSON { response in
         debugPrint(response)
         print("teb3ath")
         }*/
    }
    
    func upload() {
        // let user_id:Int = 4
        //  Alamofire.upload(user_id, to: "http://192.168.1.13:3003/video/post", method: .post, headers: HTTPHeaders.init())
        print("teb3ath")
    }
    
    func uploadVideo(){
        let parameters = ["id_user":users.connected_user,
                          "url" : preview!]
        Alamofire.upload(multipartFormData: { (multipartFormData) in
            multipartFormData.append(self.videoURL, withName: "videoURL", fileName: "video.mp4", mimeType: "video/mp4")
            
            /*  for key in parameters.keys{
             let name = String(key)
             print(name)
             if let val = parameters[name] as? String{
             multipartFormData.append(val.data(using: .utf8)!, withName: name)
             }
             }*/
            
            for (key, value) in parameters {
                multipartFormData.append("\(value)".data(using: String.Encoding.utf8)!, withName: key as String)
                print(key+" "+value)
            }
            
        }, to:connexion.url+"/video/post")
        { (result) in
            switch result {
            case .success(let upload, _, _):
                
                upload.uploadProgress(closure: { (Progress) in
                    print("Upload Progress: \(Progress.fractionCompleted)")
                })
                
                upload.responseJSON { response in
                    //self.delegate?.showSuccessAlert()
                    print(response.request!)  // original URL request
                    print(response.response!) // URL response
                    print(response.data!)     // server data
                    print(response.result)   // result of response serialization
                    //                        self.showSuccesAlert()
                    //self.removeImage("frame", fileExtension: "txt")
                    if let JSON = response.result.value {
                        print("JSON: \(JSON)")
                    }
                }
                
            case .failure(let encodingError):
                //self.delegate?.showFailAlert()
                print("errourr")
                print(encodingError)
            }
            
        }
        
    }
    
    
    
    /* func mergeFilesWithUrl(videoUrl:NSURL, audioUrl:NSURL)
     {
     let mixComposition : AVMutableComposition = AVMutableComposition()
     var mutableCompositionVideoTrack : [AVMutableCompositionTrack] = []
     var mutableCompositionAudioTrack : [AVMutableCompositionTrack] = []
     let totalVideoCompositionInstruction : AVMutableVideoCompositionInstruction = AVMutableVideoCompositionInstruction()
     
     
     //start merge
     
     let aVideoAsset : AVAsset = AVAsset(url: videoUrl as URL)
     let aAudioAsset : AVAsset = AVAsset(url: audioUrl as URL)
     
     mutableCompositionVideoTrack.append(mixComposition.addMutableTrack(withMediaType: AVMediaType.video, preferredTrackID: kCMPersistentTrackID_Invalid)!)
     mutableCompositionAudioTrack.append( mixComposition.addMutableTrack(withMediaType: AVMediaType.audio, preferredTrackID: kCMPersistentTrackID_Invalid)!)
     
     let aVideoAssetTrack : AVAssetTrack = aVideoAsset.tracks(withMediaType: AVMediaType.video)[0]
     let aAudioAssetTrack : AVAssetTrack = aAudioAsset.tracks(withMediaType: AVMediaType.audio)[0]
     
     
     
     do{
     try mutableCompositionVideoTrack[0].insertTimeRange(CMTimeRangeMake(start: CMTime.zero, duration: aVideoAssetTrack.timeRange.duration), of: aVideoAssetTrack, at: CMTime.zero)
     
     //In my case my audio file is longer then video file so i took videoAsset duration
     //instead of audioAsset duration
     
     try mutableCompositionAudioTrack[0].insertTimeRange(CMTimeRangeMake(start: CMTime.zero, duration: aVideoAssetTrack.timeRange.duration), of: aAudioAssetTrack, at: CMTime.zero)
     
     //Use this instead above line if your audiofile and video file's playing durations are same
     
     //            try mutableCompositionAudioTrack[0].insertTimeRange(CMTimeRangeMake(kCMTimeZero, aVideoAssetTrack.timeRange.duration), ofTrack: aAudioAssetTrack, atTime: kCMTimeZero)
     
     }catch{
     
     }
     
     totalVideoCompositionInstruction.timeRange = CMTimeRangeMake(start: CMTime.zero,duration: aVideoAssetTrack.timeRange.duration )
     
     let mutableVideoComposition : AVMutableVideoComposition = AVMutableVideoComposition()
     mutableVideoComposition.frameDuration = CMTimeMake(value: 1, timescale: 30)
     
     mutableVideoComposition.renderSize = CGSize(width: 1280, height: 720)
     
     
     //find your video on this URl
     print("i am here")
     let date = Date()
     let dateFormater = DateFormatter()
     dateFormater.dateFormat = "dd.MM.yyyy"
     let result = dateFormater.string(from: date)
     let savePathUrl : NSURL = NSURL(fileURLWithPath: NSHomeDirectory() + result + "/Documents/newVideo.mp4")
     let playerItem = AVPlayerItem(url: savePathUrl as URL)
     avPlayer.replaceCurrentItem(with: playerItem)
     
     avPlayer.play()
     
     let assetExport: AVAssetExportSession = AVAssetExportSession(asset: mixComposition, presetName: AVAssetExportPresetHighestQuality)!
     assetExport.outputFileType = AVFileType.mp4
     assetExport.outputURL = savePathUrl as URL
     assetExport.shouldOptimizeForNetworkUse = true
     
     assetExport.exportAsynchronously { () -> Void in
     switch assetExport.status {
     
     case AVAssetExportSession.Status.completed:
     
     //Uncomment this if u want to store your video in asset
     
     //let assetsLib = ALAssetsLibrary()
     //assetsLib.writeVideoAtPathToSavedPhotosAlbum(savePathUrl, completionBlock: nil)
     
     print("success")
     case  AVAssetExportSession.Status.failed:
     print("failed \(assetExport.error)")
     case AVAssetExportSession.Status.cancelled:
     print("cancelled \(assetExport.error)")
     default:
     print("complete")
     }
     }
     
     
     }*/
    
    
    
    
}
