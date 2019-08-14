//
//  TopPlaylists.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/27/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import Foundation

import ObjectMapper


class PlaylistObject: Mappable {
    var playlists: [Playlist]?
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        playlists <- map["data"]
    }
    
    
}

class Playlist:Mappable{
    var id:String?
    var title:String?
    var picture:String?
    var tracklist:String?
    
    
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- map["id"]
        title <- map["title"]
        tracklist <- map["tracklist"]
        picture <- map["picture_medium"]
    }
    
    
}


