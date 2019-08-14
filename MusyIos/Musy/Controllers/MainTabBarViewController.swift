//
//  ViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/11/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import SwipeableTabBarController

class MainTabBar: UITabBarController, UITabBarControllerDelegate {
  /*  let Home = HomeViewController()
    let Music = MusicViewController()
    let Video = VideosViewController()
    let Search = SearchViewController()
    
   */
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tabBar.barTintColor = UIColor.black
        
        
     
        
        
       /* viewControllers = [createController(title: "Home", imageName: "home", vc: Home ),createController(title: "Music", imageName: "mymusic", vc: Music ), createController(title: "Video", imageName: "flow", vc:  Video), createController(title: "Search", imageName: "search", vc:  Search)]*/
    }
    
    /*private func createController(title: String, imageName: String, vc: UIViewController) -> UINavigationController{
        let recentVC = UINavigationController(rootViewController: vc)
        recentVC.tabBarItem.title = title
        recentVC.tabBarItem.image = UIImage(named: imageName)
        return recentVC
    }*/
    
    
}

