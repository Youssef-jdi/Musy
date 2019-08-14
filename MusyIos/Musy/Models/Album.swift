//
//  album.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/27/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import Foundation
import ObjectMapper


class AlbumObject: Mappable {
    var albums: [Album]?
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        albums <- map["data"]
    }
}

class Album:Mappable{
    var id:Int?
    var title:String?
    var cover:String?
    var tracklist:String?
    var nb_albums: String?
    var artist: Artist?
    var tracks: TrackObject?
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
         title <- map["title"]
        tracklist <- map["tracklist"]
        cover <- map["cover_medium"]
        nb_albums <- map["nb_albums"]
        tracks <- map["tracks"]
        
    }
    
    
}

