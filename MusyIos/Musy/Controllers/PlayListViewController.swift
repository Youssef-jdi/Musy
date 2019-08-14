//
//  PlayListViewController.swift
//  Musyy
//
//  Created by Youssef Jdidi on 12/24/18.
//  Copyright © 2018 Youssef Jdidi. All rights reserved.
//

import UIKit
import Alamofire
class PlayListViewController: UIViewController,UITableViewDataSource,UITableViewDelegate {
    
    @IBAction func btnBack(_ sender: Any) {
          dismiss(animated: true, completion: nil)
        
    }
    var list:NSArray = []
    let url:String = connexion.url+"/getplay/"+users.connected_user
    var trackId:Track?
    @IBOutlet weak var table: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        fetchdata()
        // Do any additional setup after loading the view.
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return list.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell")
        let content = cell?.viewWithTag(0)
        let label = content?.viewWithTag(1) as! UILabel
        let playlist = list[indexPath.row] as! Dictionary<String,Any>
        label.text = playlist["name_playlist"] as! String
        return cell!
    }
    
    
    func fetchdata()  {
        Alamofire.request(url).responseJSON{
            response in
            //  print(response)
            self.list = response.result.value as! NSArray
            self.table.reloadData()
        }
        
        
    }
    
    @IBAction func addNewPlaylist(_ sender: Any) {
        let alert = UIAlertController(title: "Add new Playlist", message: "Enter a name", preferredStyle: .alert)
        alert.addTextField { (textfield) in
            textfield.placeholder = "name"
            
        }
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { [ weak alert ] (_) in
            let textfield = alert?.textFields![0]
            self.addPlaylist(name: (textfield?.text!)!)
            
            }
        ))
        self.present(alert , animated: true , completion: nil)
        
    }
    
    func addPlaylist(name : String)  {
        Alamofire.request(connexion.url+"/addplaylist/"+users.connected_user+"/"+name)
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2, execute: {
            self.fetchdata()
        })
        
        
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        /* let id = NSNumber(value: trackId!.id!).intValue
         
         let request = "http://192.168.1.13:3003/addtracktoplaylist/"
         let requestid = request+String(id)+"/"+trackId!.title!+"/"
         let requests = requestid+trackId!.artiste!.name! + "/album/" + String(idplaylist)
         Alamofire.request(requests)*/
        let playlist = list[indexPath.row] as! Dictionary<String,Any>
        let idplaylist = playlist["id"] as! Int
        
        let idtrack = Int((trackId?.id)!)
        let param:Parameters = ["id":idtrack,"name":(trackId?.title!)!,"artist":(trackId?.artist?.name!)!,"album":(trackId?.artist?.name!)!,"playlistid":idplaylist,"cover":(trackId?.album?.cover!)!,"url":(trackId?.preview!)!]
        Alamofire.request(connexion.url+"/addtrackplaylist", method: .post, parameters: param).responseString { response in
            
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
    
    
  
    
    
    
    
}
