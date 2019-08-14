//
//  File.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/27/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import Foundation
import ObjectMapper


/*struct Genre {
    let id: Int
    let name: String
    let image: String
 
}*/


class GenreObject: Mappable {
    var genres: [Genre]?
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        genres <- map["data"]
    }
    
    
}

class Genre:Mappable{
    var id:String?
    var name:String?
    var picture:String?
    required init?(map: Map) {
        
    }
    
    func mapping(map: Map) {
        id <- (map["id"],StringTransform())
        name <- map["name"]
        picture <- map["picture_medium"]
    }
    
    
}


