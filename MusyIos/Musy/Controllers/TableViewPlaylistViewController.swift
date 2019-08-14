//
//  TableViewPlayListViewController.swift
//  Musyy
//
//  Created by Youssef Jdidi on 11/27/18.
//  Copyright © 2018 Youssef Jdidi. All rights reserved.
//

import UIKit
import PullUpController
class TableViewPlayListViewController: PullUpController ,UITableViewDataSource,UITableViewDelegate{
    
    
    
    @IBOutlet weak var table: UITableView!
    var TrackArray : [Track]?
    public var portraitSize: CGSize = .zero
    public var landscapeFrame: CGRect = .zero
    enum InitialState {
        case contracted
        case expanded
    }
    
    var initialState: InitialState = .contracted
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        
        // Do any additional setup after loading the view.
        
    }
    
    
    override var pullUpControllerPreferredSize: CGSize {
        return CGSize.init(width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height)
    }
    var initialPointOffset: CGFloat {
        switch initialState {
        case .contracted:
            return 50
        case .expanded:
            return pullUpControllerPreferredSize.height
        }
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return  TrackArray!.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cellTableviewPlayList")
        let content = cell?.viewWithTag(0)
        let songname = content?.viewWithTag(1) as! UILabel
        let artistname = content?.viewWithTag(2) as! UILabel
        songname.text = TrackArray![indexPath.row].title
        artistname.text = TrackArray![indexPath.row].artist?.name
        cell?.layer.backgroundColor = UIColor.clear.cgColor
        
        
        
        
        
        return cell!
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        //notifcation.post(name: Notification.Name("stopSoundNotification"), object: nil,)
        print("b3atht")
        /* let userData = NSMutableDictionary()
         var data = userData as NSDictionary? as? [AnyHashable: Any] ?? [:]
         data["index"] = indexPath.row*/
        NotificationCenter.default.post(name: Notification.Name("play"), object: nil, userInfo: ["index":indexPath.row])
        
        
    }
    
    
    
    
    
    
}

