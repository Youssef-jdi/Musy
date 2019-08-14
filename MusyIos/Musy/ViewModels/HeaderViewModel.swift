

import UIKit

class HeaderViewModel: CollectionViewViewModel<HeaderCell, String> {
    
    override func config(cell: HeaderCell, data: String, indexPath: IndexPath, grid: Grid) {
        cell.textLabel.text = data
    }
    
    override func size(data: String, indexPath: IndexPath, grid: Grid, view: UIView) -> CGSize {
        return grid.size(for: view, height: 48)
    }
    
}
