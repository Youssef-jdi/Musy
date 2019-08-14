//
//  ArtistViewController.swift
//  Musy
//
//  Created by Yasmine Naouar on 12/5/18.
//  Copyright © 2018 Yasmine Naouar. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class ArtistViewController: CollectionViewController  {
     var sections: [CollectionViewSection] = []
    var artist: Artist?
    
    
    var urlTracks:String!
    var urlAlbums:String!
    var urlRelated:String!
    var urlPlaylists:String!
    
    var artistObject: ArtistObject?
    var artistArray: [Artist]?
    
    var playlistObject: PlaylistObject?
    var PlaylistArray: [Playlist]?
    
    var albumObject: AlbumObject?
    var albumArray:  [Album]?
    override func viewDidLoad() {
        super.viewDidLoad()
        print(artist!.id!)
       
        
        
        
        urlAlbums = "https://api.deezer.com/artist/\(artist!.id!)/albums"
        urlPlaylists = "https://api.deezer.com/artist/\(artist!.id!)/playlists"
        urlRelated = "https://api.deezer.com/artist/\(artist!.id!)/related"
        urlTracks = "https://api.deezer.com/artist/\(artist!.id!)/top"
        
        let headSection = CollectionViewSection(items: [ArtistViewModel(self.artist!)])
        headSection.header = HeaderViewModel(self.artist!.name!)
        self.sections.append(headSection)
        let albumSection = CollectionViewSection(items: [self.createHorizontalAlbumFromAPI()])
        let artistSection = CollectionViewSection(items: [self.createHorizontalArtistFromAPI()])
        let playlistSection = CollectionViewSection(items: [self.createHorizontalPlaylistFromAPI()])

        
        
      /*  let tracks = Array<Track>(repeating: Track(id: 1, title: "Lorem ipsum dolor sit amet", preview: "", artisName: "" ), count: 5)
        let items = tracks.map { TrackViewModel($0) }
        let newItems: [CollectionViewViewModelProtocol] = Array(items.map { [$0] }
            .joined(separator: [SeparatorViewModel(2)]))
        
        let trackSection = CollectionViewSection(items: newItems)
        trackSection.header = HeaderViewModel("Top tracks")
        */
        
      //  getTopTracksFromAPI()
        
        let grid = Grid(columns: 1, margin: UIEdgeInsets(all: 8))
        self.source = CollectionViewSource(grid: grid, sections:sections)
        self.collectionView.reloadData()
    }
    
    
  /*  func getTopTracksFromAPI(){
        var tracks : [Track] = []
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        var source : CollectionViewSource = CollectionViewSource()
        print(sections.count)
        
        Alamofire.request("https://api.deezer.com/chart/0/albums").responseJSON { (res) in
            guard res.result.isSuccess,
                let value = res.result.value else {
                    print("Error while fetching tags: \(String(describing: res.result.error))")
                    return
            }
            let tracksJSON = JSON(value)["data"].array
            
            for track in tracksJSON! {
                let track = Track(id: track["id"].int! , title: track["title"].rawString()!, preview: track["preview"].rawString()!, artisName: track["artist"]["name"].rawString()!)
                    tracks.append(track)
            }
            print("We have \(tracks.count) tracks" )
            
            let items = tracks.map { track ->TrackViewModel in
                let viewModel = TrackViewModel(track)
                viewModel.delegate = self as? TrackViewModelDelegate
                return viewModel
            }
            
            let trackSection = CollectionViewSection(items: items)
            trackSection.header = HeaderViewModel("Top Tracks")
            self.sections.append(trackSection)
            print("reloading data")
            self.source  = CollectionViewSource(grid: grid, sections: self.sections)
            self.collectionView.reloadData()
        }
    }*/
    
    func createHorizontalPlaylistFromAPI() -> CollectionViewModel{
        var playlists : [Playlist] = []
        var source : CollectionViewSource = CollectionViewSource()
        let grid = Grid(columns: 4, margin: UIEdgeInsets(all: 8))
        print(sections.count)
        
        Alamofire.request(urlPlaylists).responseObject { (response: DataResponse<PlaylistObject>)in
            
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
        
        Alamofire.request(urlAlbums).responseObject { (response: DataResponse<AlbumObject>)in
            
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
        Alamofire.request(urlRelated).responseObject { (response: DataResponse<ArtistObject>)in
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
            artistSection.header = HeaderViewModel("Similar Artists")
            
            self.sections.append(artistSection)
            self.source  = CollectionViewSource(grid: grid, sections: self.sections)
            self.collectionView.reloadData()
        }
        return CollectionViewModel(source)
        
    }
   
}

extension ArtistViewController: ArtistViewModelDelegate {
    
    func didSelect(artist: Artist) {
        let viewController = ArtistViewController(nibName: nil, bundle: nil)
        viewController.artist = artist
        self.show(viewController, sender: nil)
    }
}


extension ArtistViewController:AlbumViewModelDelegate {
    
    func didSelect(album: Album){
        //let viewController = MusicPlayerViewController()
        let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let secondVC = storyboard.instantiateViewController(withIdentifier: "MusicPlayerViewController") as! MusicPlayerViewController
        
        secondVC.playlist = album.tracklist!
        secondVC.taswiraAlbum = album.cover!
        self.present(secondVC, animated: true)
    }
}


extension ArtistViewController:PlaylistViewModelDelegate {
    
    func didSelect(playlist: Playlist){
        //let viewController = MusicPlayerViewController()
        let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let secondVC = storyboard.instantiateViewController(withIdentifier: "MusicPlayerViewController") as! MusicPlayerViewController
        
        secondVC.playlist = playlist.tracklist!
        
        self.present(secondVC, animated: true)
    }
}

