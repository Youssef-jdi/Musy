//
//  CollectionCell.swift
//  CVVM
//
//  Created by Tibor Bödecs on 2018. 04. 13..
//  Copyright © 2018. Tibor Bödecs. All rights reserved.
//

import UIKit

class CollectionCell: CollectionViewCell {
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    open var source: CollectionViewSource? = nil {
        didSet {
            self.source?.register(itemsFor: self.collectionView)
            
            self.collectionView.dataSource = self.source
            self.collectionView.delegate = self.source
        }
    }

    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.collectionView.showsHorizontalScrollIndicator = false
    }
    
    override func reset() {
        super.reset()
        
        self.source = nil
    }
}
