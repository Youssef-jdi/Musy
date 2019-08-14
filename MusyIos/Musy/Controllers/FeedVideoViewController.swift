//
//  FeedVideoViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 1/15/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire
import AVFoundation
import AlamofireImage
import MMPlayerView
class FeedVideoViewController: UIViewController {
   
    @IBOutlet weak var playerCollect: UICollectionView!
    var offsetObservation: NSKeyValueObservation?
    lazy var mmPlayerLayer: MMPlayerLayer = {
        let l = MMPlayerLayer()
        
        l.cacheType = .memory(count: 5)
        l.coverFitType = .fitToPlayerView
        l.videoGravity = AVLayerVideoGravity.resizeAspect
        l.replace(cover: CoverA.instantiateFromNib())
        return l
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
       DemoSource.init()
        for a in  DemoSource.shared.demoData {
            print(a.content)
        }
        self.navigationController?.mmPlayerTransition.push.pass(setting: { (_) in
            
        })
            self.offsetObservation = self.playerCollect.observe(\.contentOffset, options: [.new]) { [weak self] (_, value) in
            guard let self = self, self.presentedViewController == nil else {return}
                self.updateByContentOffset()
            NSObject.cancelPreviousPerformRequests(withTarget: self)
            self.perform(#selector(self.startLoading), with: nil, afterDelay: 3.0)
        }
            playerCollect.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 200, right:0)
        DispatchQueue.main.asyncAfter(deadline: .now() + 3.0) { [weak self] in
            self?.updateByContentOffset()
            self?.startLoading()
        }
        
            mmPlayerLayer.getStatusBlock { [weak self] (status) in
            switch status {
            case .failed(let err):
                let alert = UIAlertController(title: "err", message: err.description, preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                self?.present(alert, animated: true, completion: nil)
            case .ready:
                print("Ready to Play")
            case .playing:
                print("Playing")
            case .pause:
                print("Pause")
            case .end:
                print("End")
            default: break
            }
        }
        
        let deadlineTime = DispatchTime.now() + .seconds(3)
        DispatchQueue.main.asyncAfter(deadline: deadlineTime) {
            self.playerCollect.reloadData()
        }
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        NotificationCenter.default.addObserver(forName: UIDevice.orientationDidChangeNotification, object: nil, queue: nil) { [unowned self] (_) in
            self.landscapeAction()
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        NotificationCenter.default.removeObserver(self)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
    }
    
    fileprivate func landscapeAction() {
        // just landscape when last result was finish
        if self.playerCollect.isDragging || self.playerCollect.isTracking || self.presentedViewController != nil {
            return
        }
        
    }
    
    deinit {
        offsetObservation?.invalidate()
        offsetObservation = nil
        print("ViewController deinit")
    }
    
  
}

// This protocol use to pass playerLayer to second UIViewcontroller
extension FeedVideoViewController: MMPlayerFromProtocol {
    // when second controller pop or dismiss, this help to put player back to where you want
    // original was player last view ex. it will be nil because of this view on reuse view
    func backReplaceSuperView(original: UIView?) -> UIView? {
        return original
    }
    
    // add layer to temp view and pass to another controller
    var passPlayer: MMPlayerLayer {
        return self.mmPlayerLayer
    }
    // current playview is cell.image hide prevent ui error
    func transitionWillStart() {
        self.mmPlayerLayer.playView?.isHidden = true
    }
    // show cell.image
    func transitionCompleted() {
        self.mmPlayerLayer.playView?.isHidden = false
    }
    
    func dismissViewFromGesture() {
        mmPlayerLayer.thumbImageView.image = nil
        self.updateByContentOffset()
        self.startLoading()
    }
    
    func presentedView(isShrinkVideo: Bool) {
        self.playerCollect.visibleCells.forEach {
            if ($0 as? PlayerCell)?.imgView.isHidden == true && isShrinkVideo {
                ($0 as? PlayerCell)?.imgView.isHidden = false
            }
        }
    }
}

extension FeedVideoViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let m = min(UIScreen.main.bounds.size.width, UIScreen.main.bounds.size.height)
        return CGSize.init(width: m, height: m*0.75)
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        DispatchQueue.main.async { [unowned self] in
            if self.presentedViewController != nil {
                self.playerCollect.scrollToItem(at: indexPath, at: .centeredVertically, animated: true)
                
            } else {
                
            }
        }
    }
    
    fileprivate func updateByContentOffset() {
        let p = CGPoint(x: playerCollect.frame.width/2, y: playerCollect.contentOffset.y + playerCollect.frame.width/2)
        
        if let path = playerCollect.indexPathForItem(at: p),
            self.presentedViewController == nil {
            self.updateCell(at: path)
        }
    }
    
    
    fileprivate func updateCell(at indexPath: IndexPath) {
        if let cell = playerCollect.cellForItem(at: indexPath) as? PlayerCell, let playURL = cell.data?.play_Url {
            // this thumb use when transition start and your video dosent start
            mmPlayerLayer.thumbImageView.image = cell.imgView.image
            // set video where to play
            if !MMLandscapeWindow.shared.isKeyWindow {
                mmPlayerLayer.playView = cell.imgView
            }
            
            mmPlayerLayer.set(url: playURL)
        }
    }
    
    @objc fileprivate func startLoading() {
        if self.presentedViewController != nil {
            return
        }
        // start loading video
        mmPlayerLayer.resume()
        self.landscapeAction()
    }
    
}

extension FeedVideoViewController: UICollectionViewDataSource{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        
        print("number of item in section \(DemoSource.shared.demoData.count)")
        
        return DemoSource.shared.demoData.count
    }
    
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "PlayerCell", for: indexPath) as? PlayerCell {
            cell.data = DemoSource.shared.demoData[indexPath.row]
            print("wost cell \(cell.data?.title)")
            return cell
        }
        return UICollectionViewCell()
    }
}
