//
//  UserProfileViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/28/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class MusicViewController: UIViewController , UIImagePickerControllerDelegate,  UINavigationControllerDelegate{

    
var currentImage: UIImage!
    @IBOutlet weak var nbFollowing: UILabel!
    @IBOutlet weak var nbFollowers: UILabel!
    @IBOutlet weak var profilePic: UIImageView!
    @IBOutlet weak var UserName: UILabel!
    var UserArray:[NSArray]?
    var documentsUrl: URL {
        return FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    }
  
  let url = "\(connexion.url)/user/\(users.connected_user)"
    
    var conUser : User?
    override func viewDidLoad() {
        super.viewDidLoad()
        print(url)
        getuser()
        getnbreFollowers()
        getnbreFollowing()
        setup()
    }
    
    func setup()  {
        self.profilePic.layer.cornerRadius = self.profilePic.frame.height/2;
        self.profilePic.layer.masksToBounds = true
    }
    
    func getuser(){
        Alamofire.request(connexion.url+"/user/"+users.connected_user).responseJSON{
            response in
            print(response.result.value)
            let users = response.result.value as! [[String:Any]]
            self.UserName.text = users[0]["nickname"]! as! String
            let a = users[0]["picture"]! as! String
            let b = URL(string: a)
            self.profilePic.af_setImage(withURL: b!)
            
            
            
        }
    }
    
    func getnbreFollowers(){
        Alamofire.request(connexion.url+"/numberfollowers/"+users.connected_user).responseJSON{
            response in
            let a = response.result.value! as! Int
            let b = String(a)
            self.nbFollowers.text = b
            
            
            
        }
    }
    
    func getnbreFollowing(){
        
        Alamofire.request(connexion.url+"/numberfollowing/"+users.connected_user).responseJSON{
            response in
            let a = response.result.value! as! Int
            let b = String(a)
            self.nbFollowing.text = b
            
            
            
        }
        
    }
    
        
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @IBAction func btnListFollowers(_ sender: Any) {
    }
    
    @IBAction func btnListFollowing(_ sender: Any) {
    }
    
    @IBAction func btnLogout(_ sender: Any) {
    }
    @IBAction func btnUpload(_ sender: Any) {
        importPicture()
    }
    
    
      func importPicture() {
        let picker = UIImagePickerController()
        picker.allowsEditing = true
        picker.delegate = self as! UIImagePickerControllerDelegate & UINavigationControllerDelegate
        present(picker, animated: true)
    }
    
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        guard let image = info[.editedImage] as? UIImage else { return }
        let esmha = save(image: image)
        let fileURL = documentsUrl.appendingPathComponent(esmha!)
        print(fileURL)
        uploadVideo(path: fileURL as URL)

        dismiss(animated: true)
       profilePic.image = image
        print("cc")
        print(image)

    }

    
    ////////////////////////////////// don't delete this 
    
    private func save(image: UIImage) -> String? {
        let fileName = "image.jpg"
        let fileURL = documentsUrl.appendingPathComponent(fileName)
        //let khrsssss = UIImage.jpegData(image)
        
        if let imageData = image.jpegData(compressionQuality: 1.0) {
            try? imageData.write(to: fileURL, options: .atomic)
            return fileName // ----> Save fileName
        }
        print("Error saving image")
        return nil
    }
    
    
    private func load(fileName: String) -> UIImage? {
        let fileURL = documentsUrl.appendingPathComponent(fileName)
        do {
            let imageData = try Data(contentsOf: fileURL)
            return UIImage(data: imageData)
        } catch {
            print("Error loading image : \(error)")
        }
        return nil
    }
    
    
    func uploadVideo(path : URL){
        let parameters = ["userId":users.connected_user]
        Alamofire.upload(multipartFormData: { (multipartFormData) in
            multipartFormData.append(path, withName: "image", fileName: "image.jpg", mimeType: "Image/jpg")
            
            
            for (key, value) in parameters {
                multipartFormData.append("\(value)".data(using: String.Encoding.utf8)!, withName: key as String)
                print(key+" "+value)
            }
            
        }, to:connexion.url+"/upload/")
        { (result) in
            switch result {
            case .success(let upload, _, _):
                
                upload.uploadProgress(closure: { (Progress) in
                    print("Upload Progress: \(Progress.fractionCompleted)")
                })
                
                upload.responseJSON { response in
                    //self.delegate?.showSuccessAlert()
                    print(response.request!)  // original URL request
                    print(response.response!) // URL response
                    print(response.data!)     // server data
                    print(response.result)   // result of response serialization
                    //                        self.showSuccesAlert()
                    //self.removeImage("frame", fileExtension: "txt")
                    if let JSON = response.result.value {
                        print("JSON: \(JSON)")
                    }
                }
                
            case .failure(let encodingError):
                //self.delegate?.showFailAlert()
                print("errourr")
                print(encodingError)
            }
            
        }
        
    }
    
   
    
    
    
    
    

}
