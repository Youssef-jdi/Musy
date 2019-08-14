//
//  HomeCollectionViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 11/27/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireObjectMapper
import AlamofireImage
import SwiftyJSON


class HomeViewController: CollectionViewController {
    var sections: [CollectionViewSection] = []
    let urlGenre:String = "https://api.deezer.com/genre"
    var genreobject: GenreObject?
    var GenreArray:[Genre]?
    var playlistObject: PlaylistObject?
    var PlaylistArray: [Playlist]?
    var artistObject: ArtistObject?
    var artistArray: [Artist]?
    var albumObject: AlbumObject?
    var albumArray:  [Album]?
    
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.title = "Home"
        print(users.connected_user)
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        
        
        let genreSection = CollectionViewSection(items: [self.createHorizontalGenreFromAPI()])
        genreSection.header = HeaderViewModel("Top genres")
        let playlistSection = CollectionViewSection(items: [self.createHorizontalPlaylistFromAPI()])
        playlistSection.header = HeaderViewModel("Playlist Picks")
        let albumSection = CollectionViewSection(items: [self.createHorizontalAlbumFromAPI()])
        albumSection.header = HeaderViewModel("Top Albums")
        let artistSection = CollectionViewSection(items: [self.createHorizontalArtistFromAPI()])
        artistSection.header = HeaderViewModel("Top Artists")
        self.source  = CollectionViewSource(grid: grid, sections: sections)
        self.collectionView.reloadData()
    }
    
    
    func createHorizontalGenreFromAPI() -> CollectionViewModel{
        var genres : [Genre] = []
        var source : CollectionViewSource = CollectionViewSource()
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        print(sections.count)
        
        Alamofire.request(urlGenre).responseObject { (response: DataResponse<GenreObject>) in
            self.genreobject = response.result.value
            self.GenreArray = self.genreobject?.genres
            
            for genre in self.GenreArray!{
                genres.append(genre)
            
            }
         //   genres.append(Genre(id: 100000 , name: "View All", image: "more"))
            
            let items = genres.map { genre -> HorizontalGenreViewModel in
                let viewModel = HorizontalGenreViewModel(genre)
                viewModel.delegate = self as? GenreViewModelDelegate
                return viewModel
            }
            
            let section = CollectionViewSection(items: items)
            source  = CollectionViewSource(grid: grid, sections: [section])
            
            let genreSection = CollectionViewSection(items: [CollectionViewModel(source)])
            genreSection.header = HeaderViewModel("Top genres")
            
            self.sections.append(genreSection)
            self.source  = CollectionViewSource(grid: grid, sections: self.sections)
            self.collectionView.reloadData()
        }
        return CollectionViewModel(source)
        
    }
    
    func createHorizontalPlaylistFromAPI() -> CollectionViewModel{
        var playlists : [Playlist] = []
        var source : CollectionViewSource = CollectionViewSource()
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        print(sections.count)
        
        Alamofire.request("https://api.deezer.com/chart/0/playlists").responseObject { (response: DataResponse<PlaylistObject>)in
            
            self.playlistObject = response.result.value
            self.PlaylistArray = self.playlistObject?.playlists
            
            for palylist in self.PlaylistArray!{
                playlists.append(palylist)
               
            }
            print("We have \(playlists.count) playlist" )
            
            let items = playlists.map { playlist -> HorizontalPlaylistViewModel in
                let viewModel = HorizontalPlaylistViewModel(playlist)
                viewModel.delegate = self as? PlaylistViewModelDelegate
                return viewModel
            }
            
            let section = CollectionViewSection(items: items)
            source  = CollectionViewSource(grid: grid, sections: [section])
            
            let playlistSection = CollectionViewSection(items: [CollectionViewModel(source)])
            playlistSection.header = HeaderViewModel("Playlist Picks")
            
            self.sections.append(playlistSection)
            self.source  = CollectionViewSource(grid: grid, sections: self.sections)
            self.collectionView.reloadData()
        }
        return CollectionViewModel(source)
        
    }
    
    func createHorizontalAlbumFromAPI() -> CollectionViewModel{
        var albums : [Album] = []
        var source : CollectionViewSource = CollectionViewSource()
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        print(sections.count)
        
        Alamofire.request("https://api.deezer.com/chart/0/albums").responseObject { (response: DataResponse<AlbumObject>)in
            
            self.albumObject = response.result.value
            self.albumArray = self.albumObject?.albums
            
            for album in self.albumArray!{
                albums.append(album)}
            print("We have \(albums.count) album" )
            
            let items = albums.map { album -> HorizontalAlbumViewModel in
                let viewModel = HorizontalAlbumViewModel(album)
                viewModel.delegate = self as? AlbumViewModelDelegate
                return viewModel
            }
            
            let section = CollectionViewSection(items: items)
            source  = CollectionViewSource(grid: grid, sections: [section])
            
            let albumSection = CollectionViewSection(items: [CollectionViewModel(source)])
            albumSection.header = HeaderViewModel("Top Albums")
            
            self.sections.append(albumSection)
            self.source  = CollectionViewSource(grid: grid, sections: self.sections)
            self.collectionView.reloadData()
        }
        return CollectionViewModel(source)
        
    }
    
    func createHorizontalArtistFromAPI() -> CollectionViewModel{
        var artistes : [Artist] = []
        var source : CollectionViewSource = CollectionViewSource()
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        print(sections.count)
        
        Alamofire.request("https://api.deezer.com/chart/0/artists").responseObject { (response: DataResponse<ArtistObject>)in
            
            self.artistObject = response.result.value
            self.artistArray = self.artistObject?.artists
            
            for artist in self.artistArray!{
                artistes.append(artist)
                
            }
            print("We have \(artistes.count) playlist" )
            
            let items = artistes.map { artist -> HorizontalArtistViewModel in
                let viewModel = HorizontalArtistViewModel(artist)
                viewModel.delegate = self as? ArtistViewModelDelegate
                return viewModel
            }
            
            let section = CollectionViewSection(items: items)
            source  = CollectionViewSource(grid: grid, sections: [section])
            
            let artistSection = CollectionViewSection(items: [CollectionViewModel(source)])
            artistSection.header = HeaderViewModel("Popluar Artists")
            
            self.sections.append(artistSection)
            self.source  = CollectionViewSource(grid: grid, sections: self.sections)
            self.collectionView.reloadData()
        }
        return CollectionViewModel(source)
        
    }
    
   
}

extension HomeViewController: ArtistViewModelDelegate {
    
    func didSelect(artist: Artist) {
        let viewController = ArtistViewController(nibName: nil, bundle: nil)
        viewController.artist = artist
        print("mel home "+artist.id!)
        self.show(viewController, sender: nil)
       // self.present(viewController, animated: true, completion: nil)
       // viewController = navigationController?.popViewController(animated: true) as! ArtistViewController
        
    }
}


extension HomeViewController:AlbumViewModelDelegate {
    
    func didSelect(album: Album){
        //let viewController = MusicPlayerViewController()
        let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let secondVC = storyboard.instantiateViewController(withIdentifier: "MusicPlayerViewController") as! MusicPlayerViewController
        
        secondVC.playlist = album.tracklist!
        secondVC.taswiraAlbum = album.cover!
        self.present(secondVC, animated: true)
    }
}

extension HomeViewController:PlaylistViewModelDelegate {
    
    func didSelect(playlist: Playlist){
        //let viewController = MusicPlayerViewController()
        let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let secondVC = storyboard.instantiateViewController(withIdentifier: "MusicPlayerViewController") as! MusicPlayerViewController
        
        secondVC.playlist = playlist.tracklist!
        
        self.present(secondVC, animated: true)
    }
}

extension HomeViewController:GenreViewModelDelegate{
    func didSelect(genre: Genre){
       /* if(genre.id == 100000){
            let viewController = GenreViewController(nibName: nil, bundle: nil)
            self.show(viewController, sender: nil)
            
        }
        else {*/
            let viewController = ChartByGenreViewController(nibName: nil, bundle: nil)
            viewController.genre = genre
        
            self.show(viewController, sender: nil)
            
      //  }
    }
}


