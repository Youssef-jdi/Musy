
import UIKit

protocol GenreViewModelDelegate: class {
    func didSelect(genre: Genre)
}

class GenreViewModel: CollectionViewViewModel<GenreCell, Genre> {
    
    weak var delegate: GenreViewModelDelegate?
    
    override func config(cell: GenreCell, data: Genre, indexPath: IndexPath, grid: Grid) {
        cell.textLabel.text = data.name
        if(data.picture == "more"){
            cell.textLabel.textColor = .black
            return
        }
        let url = URL(string: data.picture!)
        let imgData = try? Data(contentsOf: url!)
        cell.imageView.image = UIImage(data: imgData!)
    }
    
    override func size(data: Genre, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
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
    
    override func callback(data: Genre, indexPath: IndexPath) {
        self.delegate?.didSelect(genre: data)
    }
}

class HorizontalGenreViewModel: GenreViewModel {
    
    override func size(data: Genre, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
        return CGSize(width: 140, height: 130)
    }
}

