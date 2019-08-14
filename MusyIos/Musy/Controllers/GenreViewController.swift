//
//  GenreViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/5/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
class GenreViewController: CollectionViewController {
       var sections: [CollectionViewSection] = []
    var genreobject: GenreObject?
    var GenreArray:[Genre]?
    
    let url:String = "https://api.deezer.com/genre"
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        getAllgenre()
        self.source  = CollectionViewSource(grid: grid, sections: sections)
        self.collectionView.reloadData()

    }
    
    
    
    
    func getAllgenre(){
       var genres : [Genre] = []
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
      //  var source : CollectionViewSource = CollectionViewSource()
        print(sections.count)
        
        Alamofire.request(url).responseObject { (response: DataResponse<GenreObject>) in
                self.genreobject = response.result.value
                self.GenreArray = self.genreobject?.genres

            for genre in self.GenreArray!{
                genres.append(genre)
            }
            let items = genres.map { genre -> GenreViewModel in
                let viewModel = GenreViewModel(genre)
                viewModel.delegate = self as? GenreViewModelDelegate
                return viewModel
            }
            let genreSection = CollectionViewSection(items: items)
            genreSection.header = HeaderViewModel("All genre")
            self.sections.append(genreSection)
            print("reloading data")
            self.source  = CollectionViewSource(grid: grid, sections: self.sections)
            self.collectionView.reloadData()
        }
    }
    
}




extension GenreViewController: GenreViewModelDelegate{
    func didSelect(genre: Genre) {
        let viewController = ChartByGenreViewController(nibName: nil, bundle: nil)
        viewController.genre = genre
        
        self.show(viewController, sender: nil)
    }
}
