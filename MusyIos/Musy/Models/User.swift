//
//  User.swift
//  Musy
//
//  Created by Yasmine Naouar on 1/14/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import Foundation
import ObjectMapper



class User:Mappable{
    var id:String?
    var nickname:String?
    var picture:String?
    var email:String?
    
    
    
    required init?(map: Map) {
        
    }
    
    
    
    func mapping(map: Map) {
        id <- (map["id"],StringTransform())
        nickname <- map["nickname"]
        picture <- map["picture"]
        email <- map["email"]
        
    }
    
    
}
