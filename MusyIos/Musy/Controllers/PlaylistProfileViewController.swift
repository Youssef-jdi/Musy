//
//  PlaylistProfileViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 1/13/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire

class PlaylistProfileViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    
    var list: NSArray = []

    @IBOutlet weak var tableView: UITableView!
    
    @IBAction func Back(_ sender: Any) {
         dismiss(animated: true, completion: nil)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fetchdata()
          
        
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return list.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "playlistCell")
        let contentView = cell!.viewWithTag(0)
        let playlistName = contentView?.viewWithTag(1) as! UILabel
        let playlist = list[indexPath.row] as! Dictionary<String,Any>
        playlistName.text = playlist["name_playlist"] as? String
        
        return cell!
        
    }
    
    
    func fetchdata()  {
        
        Alamofire.request(connexion.url+"/getPlay/"+users.connected_user).responseJSON{
            response in
            self.list = response.result.value as! NSArray
            print(self.list)
            self.tableView.reloadData()
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "playlistToTracks", sender: indexPath)
        
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "playlistToTracks"{
            let indice = sender as! IndexPath
            let playlist = list[indice.row] as! Dictionary<String,Any>
            let playlistId = playlist["id"] as! Int
            let playlistName = playlist["name_playlist"] as! String
            let profile = segue.destination as! PlaylistTracksViewController
            profile.playlistId = playlistId
            profile.playlistName = playlistName
        }
        
        
    }
    

 

}
