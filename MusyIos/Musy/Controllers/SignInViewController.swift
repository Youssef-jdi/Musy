//
//  SignInViewController.swift
//  Musyy
//
//  Created by Youssef Jdidi on 11/12/18.
//  Copyright © 2018 Youssef Jdidi. All rights reserved.
//

import UIKit
import GoogleSignIn
import FacebookLogin
import FacebookCore
import CoreData
import Alamofire
class SignInViewController: UIViewController ,GIDSignInUIDelegate,GIDSignInDelegate {
    
    
    
    
    
    
    
    
  
    
    
    
   
    
    
    
    
    
    
    @IBOutlet weak var UserName: UITextField!
    @IBOutlet weak var Password: UITextField!
    var signInBtn = GIDSignInButton()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //deleteall()
       
       
       
        let id = UserDefaults.standard.string(forKey: "id") ?? ""
        print("l'id est"+id)
        if(id != ""){
            
            DispatchQueue.main.async {
                print("comparison")
                self.performSegue(withIdentifier: "main", sender: nil)
            }
           
          /*  let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
            let secondVC = storyboard.instantiateViewController(withIdentifier: "Home") as! HomeViewController
            self.present(secondVC, animated: true)*/
        }
        blur(textfield: UserName)
        blur(textfield: Password)
        setTextfield()
        GIDSignIn.sharedInstance()?.uiDelegate = self
        GIDSignIn.sharedInstance()?.delegate = self
        signInBtn.center = view.center
        signInBtn.sizeThatFits(CGSize.init(width: 300, height: 50))
        view.addSubview(signInBtn)
        
    }
    
    
    func imageWithView(view:UIView)->UIImage {
        UIGraphicsBeginImageContextWithOptions(view.bounds.size, false, 0.0)
        view.layer.render(in: UIGraphicsGetCurrentContext()!)
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return image!
    }
    
    
    
    
    func blur(textfield : UITextField) {
        if !UIAccessibility.isReduceTransparencyEnabled {
            let blurEffect = UIBlurEffect(style: UIBlurEffect.Style.dark)
            let blurEffectView = UIVisualEffectView(effect: blurEffect)
            blurEffectView.frame = textfield.bounds
            blurEffectView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
            textfield.backgroundColor = UIColor.clear
            textfield.background = imageWithView(view: blurEffectView)
        }
    }
    
    
    func setTextfield()  {
        UserName.attributedPlaceholder = NSAttributedString(string: "User Name",attributes: [NSAttributedString.Key.foregroundColor: UIColor.lightGray])
        Password.attributedPlaceholder = NSAttributedString(string: "Password",attributes: [NSAttributedString.Key.foregroundColor: UIColor.lightGray])
        Password.isSecureTextEntry = true
        
        
    }
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
       
        UserDefaults.standard.set(user.userID!, forKey: "id")
      //  users.connected_user = user.userID!
        let image = user.profile.imageURL(withDimension: 50)!.absoluteString
        adduser(id: user.userID!, name: user.profile.name!, pic: image, mail: user.profile.email!)
        let home = HomeViewController()
        self.present(home,animated: false)
    }
    
    func setlayout()  {
        signInBtn.anchor(top: nil, leading: nil, bottom: nil, trailing: nil, padding: .init(top: 0, left: 0, bottom: 30, right: 0)  , size: .init(width: 400, height: 49))
        signInBtn.centerXAnchor.constraint(equalTo: view.safeAreaLayoutGuide.centerXAnchor).isActive = true
      
    }
    
    func sign(_ signIn: GIDSignIn!, didDisconnectWith user: GIDGoogleUser!, withError error: Error!) {
        
    }
    
    func sign(_ signIn: GIDSignIn!, present viewController: UIViewController!) {
        print("sddsds")
        self.present(viewController, animated: true, completion: nil)
    }
    
    
    func adduser(id :String , name : String , pic : String , mail : String)  {
        let param:Parameters = ["id":id,"nickName":name, "Picture": pic, "eMail":mail]
        Alamofire.request(connexion.url+"/user_create", method: .post, parameters: param).responseString { response in
            
            switch response.result
            {
            case .failure(let error):
                if let data = response.data {
                    print("Print Server Error: " + String(data: data, encoding: String.Encoding.utf8)!)
                }
                print(error)
                
            case .success(let value):
                
                print(value)
            }
        }
        
    }
    
    
   
    
    
    
    
    
    
}









