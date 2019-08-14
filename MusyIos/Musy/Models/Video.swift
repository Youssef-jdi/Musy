//
//  Video.swift
//  Musy
//
//  Created by Yasmine Naouar on 1/14/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import Foundation
import ObjectMapper

class Video:Mappable{
    var id:String?
    var video_Path:String?
    var date:String?
    var user_id:String?
    var track:String?
    
    
    
    required init?(map: Map) {
        
    }
    
    
    
    func mapping(map: Map) {
        id <- (map["id"],StringTransform())
        video_Path <- map["video_path"]
        user_id <- map["id_user"]
        date <- map["date"]
        track <- map["track"]
        
}
}
