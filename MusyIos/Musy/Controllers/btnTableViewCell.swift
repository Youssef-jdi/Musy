//
//  btnTableViewCell.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/13/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire

protocol Togglable {
    mutating func toggle()
}

class btnTableViewCell: UITableViewCell {
    
    var followStatus: FollowStatus = .follow
    @IBOutlet weak var btn: UIButton!
    @IBOutlet weak var lblName: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

  
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    @IBAction func btnFollow(_ sender: Any) {
        
        followStatus.toggle()
        if followStatus == .unfollow{
            btn?.setTitle("unfollow",for: .normal)
             Follow()
        }
        else if followStatus == .follow{
            btn?.setTitle("follow",for: .normal)
            Unfollow()
            
        }
    }
    
    
 func Follow(){
        let postsURLEndPoint: String = "http://192.168.1.13:3003/user/follow/"
        let newPost: Parameters = ["id_user" : "9", "id_following": "8"]
    let url = URL(string: postsURLEndPoint)
    
    Alamofire.request(url!, method: .post, parameters: newPost )
        .responseString { response in
            
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
    func Unfollow(){
        let postsURLEndPoint: String = "http://192.168.1.13:3003/user/deletefollow/9/8"
       // let newPost: Parameters = ["id_user" : "8", "id_following": "9"]
        let url = URL(string: postsURLEndPoint)
        
        Alamofire.request(url!, method: .get, parameters: nil )
            .responseString { response in
                
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
            

        /*Alamofire.request(postsURLEndPoint, method: .post, parameters: newPost,
                          encoding: JSONEncoding.default)
            .responseJSON{ response in
                guard response.result.error == nil else {
                    // got an error in getting the data, need to handle it
                    print("error")
                    print(response.result.error!)
                    return
                }*/
                // unwrap JSON
              /*  guard let json = response.result.value as? [String: Any] else {
                    print("No JSON")
                    // Could not get JSON
                    return
                }*/
                // use json
              /*  guard let postTitle = json["title"] as? String else {
                    // Could not get title from json
                    return
                }
                print("Post title: " + postTitle)
        }
    }*/
    
   /* func postFollow(){
        var url = URL(string: "http://192.168.1.12:3003/user/follow")!
        var request = URLRequest(url: url)
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        request.httpMethod = "POST"
        let postString = "id_user=\(4)&id_follower=\(6)" // which is your parameters
        request.httpBody = postString.data(using: .utf8)
        
        // Getting response for POST Method
        DispatchQueue.main.async {
            let task = URLSession.shared.dataTask(with: request) { data, response, error in
                guard let data = data, error == nil else {
                    return // check for fundamental networking error
                }
                
                // Getting values from JSON Response
                let responseString = String(data: data, encoding: .utf8)
                print("responseString = \(String(describing: responseString))")
                do {
                    let jsonResponse = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions()) as? NSDictionary
                }catch _ {
                    print ("OOps not good JSON formatted response")
                }
            }
            task.resume()
        }
    
    }*/
    

}
