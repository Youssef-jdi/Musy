//
//  TopArtits.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/27/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import Foundation
import ObjectMapper


/*struct Artist {
    let id: Int
    let name: String
    let image: String
    let  nb_fan: Int
    
}*/


import ObjectMapper


class ArtistObject: Mappable {
    var artists: [Artist]?
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        artists <- map["data"]
    }
}

class Artist:Mappable{
    var id:String?
    var name:String?
    var picture:String?
    var tracklist:String?
    var nb_fan: String?
    var nb_album: String?
    
    
    
    required init?(map: Map) {
        
    }
    
    
    
    func mapping(map: Map) {
        id <- (map["id"],StringTransform())
        name <- map["name"]
        tracklist <- map["tracklist"]
        picture <- map["picture_medium"]
        nb_fan <- map["nb_fan"]
        nb_album <- map["nb_album"]
        
    }
    
    
}

