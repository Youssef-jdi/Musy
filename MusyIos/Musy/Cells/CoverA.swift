//
//  CoverA.swift
//  Musy
//
//  Created by Youssef Jdidi on 1/22/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import UIKit
import MMPlayerView
import AVFoundation
class CoverA: UIView,  MMPlayerCoverViewProtocol  {
    weak var playLayer: MMPlayerLayer?
    fileprivate var isUpdateTime = false

    @IBOutlet weak var btnPlay: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        btnPlay.imageView?.tintColor = UIColor.white
    }
    
    @IBAction func btnAction(_ sender: Any) {
        self.playLayer?.delayHideCover()
        if playLayer?.player?.rate == 0{
            self.playLayer?.player?.play()
        } else {
            self.playLayer?.player?.pause()
        }
        
    }
    
    func currentPlayer(status: MMPlayerLayer.PlayStatus) {
        switch status {
        case .playing:
            self.btnPlay.setImage(#imageLiteral(resourceName: "ic_play_circle_filled"), for: .normal)
        default:
            self.btnPlay.setImage(#imageLiteral(resourceName: "ic_play_circle_filled"), for: .normal)
        }
    }
    
    
    
    
    
    func player(isMuted: Bool) {
        
    }
    
   

}
