//
//  Utils.swift
//  Musy
//
//  Created by Yasmine Naouar on 1/14/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import Foundation

struct connexion {
    static  var url = "http://192.168.43.70:3003"
}

struct users {
  static  let connected_user = UserDefaults.standard.string(forKey: "id") ?? ""
}
