//
//  ChartByGenreViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/5/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class ChartByGenreViewController: CollectionViewController {
     var sections: [CollectionViewSection] = []
   
    var genre:Genre!
    var genreId:String!
    var urlP : String!
    var urlAlbum:String!
    var urlArtist: String!
    
    var playlistObject: PlaylistObject?
    var PlaylistArray: [Playlist]?
    var artistObject: ArtistObject?
    var artistArray: [Artist]?
    var albumObject: AlbumObject?
    var albumArray: [Album]?
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        genreId = genre.id
        print(genreId)
        urlP = "https://api.deezer.com/chart/\(String(describing: genreId!)))/playlists"
        urlAlbum = "https://api.deezer.com/chart/\(String(describing: genreId!))/albums"
        urlArtist = "https://api.deezer.com/chart/\(String(describing: genreId!))/artists"
        
        
        
        
        
        
        
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        
       
   
        let playlistSection = CollectionViewSection(items: [self.createHorizontalPlaylistFromAPI()])
        playlistSection.header = HeaderViewModel("Playlist Picks")
        let albumSection = CollectionViewSection(items: [self.createHorizontalAlbumFromAPI()])
        albumSection.header = HeaderViewModel("Top Albums")
        let artistSection = CollectionViewSection(items: [self.createHorizontalArtistFromAPI()])
        artistSection.header = HeaderViewModel("Top Artists")
        self.source  = CollectionViewSource(grid: grid, sections: sections)
        self.collectionView.reloadData()

        
    }
    
    
    func createHorizontalPlaylistFromAPI() -> CollectionViewModel{
        var playlists : [Playlist] = []
        var source : CollectionViewSource = CollectionViewSource()
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        print(sections.count)
        
        Alamofire.request(urlP).responseObject { (response: DataResponse<PlaylistObject>)in
            
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
        
        Alamofire.request(urlAlbum).responseObject { (response: DataResponse<AlbumObject>)in
            
            self.albumObject = response.result.value
            self.albumArray = self.albumObject?.albums
            
            for album in self.albumArray!{
                albums.append(album)
            }
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
        
        Alamofire.request(urlArtist).responseObject { (response: DataResponse<ArtistObject>)in
            
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


extension ChartByGenreViewController: ArtistViewModelDelegate {
    
    func didSelect(artist: Artist) {
        let viewController = ArtistViewController(nibName: nil, bundle: nil)
        viewController.artist = artist
        self.show(viewController, sender: nil)
        // self.present(viewController, animated: true, completion: nil)
        // viewController = navigationController?.popViewController(animated: true) as! ArtistViewController
        
    }}
    
    
    extension ChartByGenreViewController:AlbumViewModelDelegate {
        
        func didSelect(album: Album){
            //let viewController = MusicPlayerViewController()
            let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
            let secondVC = storyboard.instantiateViewController(withIdentifier: "MusicPlayerViewController") as! MusicPlayerViewController
            
            secondVC.playlist = album.tracklist!
            secondVC.taswiraAlbum = album.cover!
            self.present(secondVC, animated: true)
        }
    }
    
    extension ChartByGenreViewController:PlaylistViewModelDelegate {
        func didSelect(playlist: Playlist){
            //let viewController = MusicPlayerViewController()
            let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
            let secondVC = storyboard.instantiateViewController(withIdentifier: "MusicPlayerViewController") as! MusicPlayerViewController
           
            secondVC.playlist = playlist.tracklist!
        
            self.present(secondVC, animated: true)
        }
    }



