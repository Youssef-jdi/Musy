//
//  ArtistViewModel.swift
//  CVVM
//
//  Created by Tibor Bödecs on 2018. 04. 13..
//  Copyright © 2018. Tibor Bödecs. All rights reserved.
//

import UIKit

protocol ArtistViewModelDelegate: class {
    func didSelect(artist: Artist)
}

class ArtistViewModel: CollectionViewViewModel<ArtistCell, Artist> {
    
    weak var delegate: ArtistViewModelDelegate?
    
    override func config(cell: ArtistCell, data: Artist, indexPath: IndexPath, grid: Grid) {
        cell.textLabel.text = data.name
        let url = URL(string: data.picture!)
        let imgData = try? Data(contentsOf: url!)
        cell.imageView.image = UIImage(data: imgData!)
    }
    
    override func size(data: Artist, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
        // note: this is a "complex" displaying the same cell in multiple grids, never done this like before haha
        if grid.columns == 1 {
            return grid.size(for: view, ratio: 1.2)
        }
        if
            (view.traitCollection.userInterfaceIdiom == .phone &&
                view.traitCollection.verticalSizeClass == .compact) ||
            view.traitCollection.userInterfaceIdiom == .pad
        {
            return grid.size(for: view, ratio: 1.2, items: 1, gaps: 3)
        }
        return grid.size(for: view, ratio: 1.2, items: 2, gaps: 1)
    }
    
    override func callback(data: Artist, indexPath: IndexPath) {
        self.delegate?.didSelect(artist: data)
    }
}

class HorizontalArtistViewModel: ArtistViewModel {

    override func size(data: Artist, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
        return CGSize(width: 140, height: 164)
    }
}
