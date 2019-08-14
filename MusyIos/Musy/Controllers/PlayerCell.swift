//
//  PlayerCell.swift
//  Musy
//
//  Created by Youssef Jdidi on 1/22/19.
//  Copyright © 2019 Yasmine Naouar. All rights reserved.
//

import UIKit

class PlayerCell: UICollectionViewCell {
    @IBOutlet weak var imgView: UIImageView!
    
    @IBOutlet weak var labTitle: UILabel!
    var data:DataObj? {
        didSet {
            self.imgView.image = data?.image
            self.labTitle.text = data?.title
        }
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        self.imgView.isHidden = false
        data = nil
    }
}
