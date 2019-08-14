
import UIKit

protocol AlbumViewModelDelegate: class {
    func didSelect(album: Album)
}

class AlbumViewModel: CollectionViewViewModel<AlbumCell, Album> {
    
    weak var delegate: AlbumViewModelDelegate?
    
    override func config(cell: AlbumCell, data: Album, indexPath: IndexPath, grid: Grid) {
        cell.textLabel.text = data.title
        let url = URL(string: data.cover!)
        let imgData = try? Data(contentsOf: url!)
        cell.imageView.image = UIImage(data: imgData!)
    }
    
    override func size(data: Album, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
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
    
    override func callback(data: Album, indexPath: IndexPath) {
        self.delegate?.didSelect(album: data)
    }
}

class HorizontalAlbumViewModel: AlbumViewModel {
    
    override func size(data: Album, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
        return CGSize(width: 140, height: 164)
    }
}


