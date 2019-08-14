//
//  TrackCell.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/29/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit

class TrackCell: CollectionViewCell {
    
    @IBOutlet weak var nbLabel: UILabel!
    @IBOutlet weak var textLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.nbLabel.font = UIFont.systemFont(ofSize: 12, weight: .bold)
        self.nbLabel.textColor = .darkGray
        self.nbLabel.lineBreakMode = .byClipping
        
        self.textLabel.font = UIFont.systemFont(ofSize: 12, weight: .bold)
        self.textLabel.textColor = .black
        
    }
    
    override func reset() {
        super.reset()
        
        self.nbLabel.text = nil
        self.textLabel.text = nil
    }


    
}
