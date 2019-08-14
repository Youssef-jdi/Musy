//
//  Track.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/28/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import Foundation

/*struct Track {
    let id:Int
    let title:String
    let preview:String
    let artisName:String
}*/

import ObjectMapper


class TrackObject: Mappable {
    var tracks: [Track]?
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        tracks <- map["data"]
    }
}

class Track:Mappable{
    var id:String?
    var title:String?
    var preview:String?
    var artist: Artist?
    var album: Album?
    var url:String?
    
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- (map["id"],StringTransform())
        title <- map["title"]
        preview <- map["preview"]
        artist <- map["artist"]
        album <- map["album"]
        url <- map["url"]
       
        
    }
    
    
}

struct StringTransform: TransformType {
    
    func transformFromJSON(_ value: Any?) -> String? {
        return value.flatMap(String.init(describing:))
    }
    
    func transformToJSON(_ value: String?) -> Any? {
        return value
    }
    
}


