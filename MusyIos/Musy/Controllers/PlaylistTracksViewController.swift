//
//  PlaylistTracksViewController.swift
//  Musy
//
//  Created by Youssef Jdidi on 1/16/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import UIKit
import AlamofireObjectMapper
import Alamofire
class PlaylistTracksViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
   
    
    @IBAction func btnBack(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    var playlistId: Int?
    var playlistName: String?
    
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var playName: UILabel!
    @IBOutlet weak var tableView: UITableView!
    var TrackArray:NSArray = []
    var tracks:[Track]?
    override func viewDidLoad() {
        super.viewDidLoad()
        playName.text = playlistName
        fetchdata()
        fetch()
       
       
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return TrackArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "playlistTrackPlayer")
        let contentView = cell!.viewWithTag(0)
        let trackname = cell!.viewWithTag(1) as! UILabel
        let trackartist = cell!.viewWithTag(2) as! UILabel
        
        let track = TrackArray[indexPath.row] as! Dictionary<String,Any>
        
        
        trackname.text = track["name"] as! String
        trackartist.text = track["artist"] as! String
        
        return cell!
    }
    
    

    
    func fetch()  {
        Alamofire.request(connexion.url+"/getTracks/"+String(playlistId!)).responseArray { (response: DataResponse<[Track]>) in
            let forecastArray = response.result.value
            self.tracks = forecastArray
            
            for tr in self.tracks! {
                print(tr.url)
            }
            
        }
    }
    
    
    func fetchdata(){
        Alamofire.request(connexion.url+"/getTracks/"+String(playlistId!)).responseJSON{
            response in
            self.TrackArray = response.result.value as! NSArray
            self.tableView.reloadData()
        }
    }
    
    

   

}
