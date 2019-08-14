//
//  TrackViewModel.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/5/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit


protocol TrackViewModelDelegate: class {
    func didSelect(track:Track)
}

class TrackViewModel:  CollectionViewViewModel<TrackCell, Track> {
    weak var delegate: TrackViewModelDelegate?
    
    override func config(cell: TrackCell, data: Track, indexPath: IndexPath, grid: Grid) {
        cell.nbLabel.text = "\(indexPath.row + 1)."
        cell.textLabel.text = data.title
        
    }
    
    override func size(data: Track, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
        return grid.size(for: view, height: 44, items: grid.columns)
    }
    
}
