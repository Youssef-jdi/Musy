
//
//  FollowStatus.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/13/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import Foundation

enum FollowStatus: Togglable {
    case follow, unfollow
    
    mutating func toggle() {
        switch self {
        case .follow:
            self = .unfollow
        case .unfollow:
            self = .follow
        }
    }
}
