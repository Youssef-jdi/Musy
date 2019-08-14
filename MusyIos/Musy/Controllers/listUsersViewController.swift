//
//  listUsersViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/13/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire

class listUsersViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    var TvShows:NSArray =  []
    
    let url = "http://192.168.1.16:3003/users"


    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       return TvShows.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell") as! btnTableViewCell
        let contentView = cell.viewWithTag(0)
        let movieName = contentView?.viewWithTag(1) as! UILabel
        let tvshow = TvShows[indexPath.row] as! Dictionary<String,Any>
        movieName.text = tvshow["nickname"] as? String
        return cell
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
        fetchdata()
        
        
    }

    func fetchdata()  {
        Alamofire.request(url).responseJSON{
            response in
            //print(response.result.value)
            self.TvShows = response.result.value as! NSArray
            self.tableView.reloadData()
        }
    }
}
