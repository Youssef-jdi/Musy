//
//  CustomSearchCell.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/8/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit

class CustomSearchCell: UITableViewCell  {
  
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
    }
   
}
