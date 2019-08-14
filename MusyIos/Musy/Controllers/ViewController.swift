//
//  ViewController.swift
//  Musyy
//
//  Created by Youssef Jdidi on 11/4/18.
//  Copyright © 2018 Youssef Jdidi. All rights reserved.
//

import UIKit
import GoogleSignIn
class ViewController: UIViewController,GIDSignInUIDelegate {
    
    
    @IBOutlet weak var backImage: UIImageView!
    @IBOutlet weak var SignInBtn: UIButton!
    @IBOutlet weak var SignUpBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // GIDSignIn.sharedInstance()?.uiDelegate = self
        // let signInBtn = GIDSignInButton(frame: CGRect.init(x: 0, y: 0, width: 100, height: 50))
        // signInBtn.center = view.center
        // view.addSubview(signInBtn)
        setBtn()
        
    }
    
    func setBtn()  {
        SignInBtn.layer.cornerRadius = SignInBtn.frame.height/2
        SignUpBtn.layer.cornerRadius = SignUpBtn.frame.height/2
    }
    
    
    
    
    
}




extension UIView {
    
    func fillSuperview() {
        anchor(top: superview?.topAnchor, leading: superview?.leadingAnchor, bottom: superview?.bottomAnchor, trailing: superview?.trailingAnchor)
    }
    
    func anchorSize(to view: UIView) {
        widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
        heightAnchor.constraint(equalTo: view.heightAnchor).isActive = true
    }
    
    func anchor(top: NSLayoutYAxisAnchor?, leading: NSLayoutXAxisAnchor?, bottom: NSLayoutYAxisAnchor?, trailing: NSLayoutXAxisAnchor?, padding: UIEdgeInsets = .zero, size: CGSize = .zero) {
        translatesAutoresizingMaskIntoConstraints = false
        
        if let top = top {
            topAnchor.constraint(equalTo: top, constant: padding.top).isActive = true
        }
        
        if let leading = leading {
            leadingAnchor.constraint(equalTo: leading, constant: padding.left).isActive = true
        }
        
        if let bottom = bottom {
            bottomAnchor.constraint(equalTo: bottom, constant: -padding.bottom).isActive = true
        }
        
        if let trailing = trailing {
            trailingAnchor.constraint(equalTo: trailing, constant: -padding.right).isActive = true
        }
        
        if size.width != 0 {
            widthAnchor.constraint(equalToConstant: size.width).isActive = true
        }
        
        if size.height != 0 {
            heightAnchor.constraint(equalToConstant: size.height).isActive = true
        }
    }
}

