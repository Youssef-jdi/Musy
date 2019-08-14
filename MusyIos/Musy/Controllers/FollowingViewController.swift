//
//  FollowingViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 1/14/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import UIKit
import AlamofireImage
import Alamofire

class FollowingViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    var TvShows:NSArray =  []
    
    var userArray: User?
    let url = "\(connexion.url)/userfollowing/\(users.connected_user)"
    
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return TvShows.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "followingCell")
        let contentView = cell!.viewWithTag(0)
        let movieImg = contentView?.viewWithTag(1) as! UIImageView
        movieImg.layer.cornerRadius = movieImg.frame.height/2;
        movieImg.layer.masksToBounds = true
        let movieName = contentView?.viewWithTag(2) as! UILabel
        let tvshow = TvShows[indexPath.row] as! Dictionary<String,Any>
        movieName.text = tvshow["nickname"] as? String
        var imagorig = tvshow["picture"] as! String
        movieImg.af_setImage(withURL: URL(string: imagorig)!)
        return cell!
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        performSegue(withIdentifier: "followingToProfile", sender: indexPath)
        
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "followingToProfile"{
            let indice = sender as! IndexPath
            let user = TvShows[indice.row] as! Dictionary<String,Any>
            let id = user["id"] as! String
            print("cc" + id)
            let profile = segue.destination as! MusicOthersViewController
            profile.idUser = id
        }
        
        
    }
    
    
    @IBAction func back(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print(url)
        //self.pic.layer.cornerRadius = self.smallImage.frame.height/2;
        // self.smallImage.layer.masksToBounds = true
        fetchdata()
        // Do any additional setup after loading the view.
    }
    
    func fetchdata()  {
        Alamofire.request(url).responseJSON{
            response in
            self.TvShows = response.result.value as! NSArray
            self.tableView.reloadData()
        }
    }
    

}
