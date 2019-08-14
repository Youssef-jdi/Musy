
import UIKit

protocol PlaylistViewModelDelegate: class {
    func didSelect(playlist: Playlist)
}

class PlaylistViewModel: CollectionViewViewModel<PlaylistCell, Playlist> {
    
    weak var delegate: PlaylistViewModelDelegate?
    
    override func config(cell: PlaylistCell, data: Playlist, indexPath: IndexPath, grid: Grid) {
        cell.textLabel.text = data.title
        let url = URL(string: data.picture!)
        let imgData = try? Data(contentsOf: url!)
        cell.imageView.image = UIImage(data: imgData!)
    }
    
    override func size(data: Playlist, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
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
    
    override func callback(data: Playlist, indexPath: IndexPath) {
        self.delegate?.didSelect(playlist: data)
    }
}

class HorizontalPlaylistViewModel: PlaylistViewModel {
    
    override func size(data: Playlist, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
        return CGSize(width: 140, height: 164)
    }
}

