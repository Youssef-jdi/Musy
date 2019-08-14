//
//  AlbumCell.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/29/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit

class AlbumCell: CollectionViewCell {
    
    @IBOutlet weak var textLabel: UILabel!
    @IBOutlet weak var imageView: UIImageView!
    override var bounds: CGRect {
        didSet {
            self.layoutIfNeeded()
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.textLabel.font = UIFont.systemFont(ofSize: 12, weight: .bold)
        self.textLabel.textColor = .black
        self.textLabel.textAlignment = .center
        self.imageView.layer.cornerRadius = 8
        self.imageView.layer.masksToBounds = true
    }
    
    override func reset() {
        super.reset()
        
        self.textLabel.text = nil
        self.imageView.image = nil
    }
    
  
    
}
