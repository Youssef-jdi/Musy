import UIKit
import SwiftyJSON
import Alamofire
import SafariServices
import ObjectMapper
final class SearchViewController: UITableViewController {
    
    var artist: Artist!
    var album: Album!
    var user: User!
    private var searchResults = [JSON]() {
        didSet {
            tableView.reloadData()
        }
    }
    
    private let searchController = UISearchController(searchResultsController: nil)
    private let apiFetcher = APIRequestFetcher()
    private let apiFetcher1 = APIRequestFetcher1()
    private let apiFetcher2 = APIRequestFetcher2()
    private var previousRun = Date()
    private let minInterval = 0.05
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = UIView()
        setupTableViewBackgroundView()
        setupSearchBar()
    }
    
    private func setupTableViewBackgroundView() {
        let backgroundViewLabel = UILabel(frame: .zero)
        backgroundViewLabel.textColor = .darkGray
        backgroundViewLabel.numberOfLines = 0
        backgroundViewLabel.text = "No results to show "
        backgroundViewLabel.textAlignment = NSTextAlignment.center
        backgroundViewLabel.font.withSize(20)
        tableView.backgroundView = backgroundViewLabel
    }
    
    private func setupSearchBar() {
        searchController.searchBar.delegate = self
        searchController.dimsBackgroundDuringPresentation = false
        searchController.hidesNavigationBarDuringPresentation = false
        searchController.searchBar.placeholder = "Search"
        definesPresentationContext = true
        searchController.searchBar.scopeButtonTitles = ["artists", "Albums", "Profile"]
        searchController.searchBar.showsScopeBar = true
        tableView.tableHeaderView = searchController.searchBar
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return searchResults.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CustomSearchCell",
                                                 for: indexPath) as! CustomSearchCell
        
        
        if(searchController.searchBar.selectedScopeButtonIndex == 0){
            cell.lblName.text = searchResults[indexPath.row]["name"].stringValue
            if let url = searchResults[indexPath.row]["picture_medium"].string {
                apiFetcher.fetchImage(url: url, completionHandler: { image, _ in
                    cell.img.image = image
                })
            }
        }else
            if(searchController.searchBar.selectedScopeButtonIndex == 1){
                cell.lblName.text = searchResults[indexPath.row]["title"].stringValue
                if let url = searchResults[indexPath.row]["cover_medium"].string {
                    apiFetcher1.fetchImage1(url: url, completionHandler: { image, _ in
                        cell.img.image = image
                    })
                }
            }else
                    if(searchController.searchBar.selectedScopeButtonIndex == 2){
                        cell.lblName.text = searchResults[indexPath.row]["nickname"].stringValue
                        if let url = searchResults[indexPath.row]["picture"].string {
                            
                        }
        }
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if(searchController.searchBar.selectedScopeButtonIndex == 0){
           let viewController = ArtistViewController()
            let id = searchResults[indexPath.row]["id"].stringValue
            
            Alamofire.request("https://api.deezer.com/artist/\(id)").responseObject { (response: DataResponse<Artist>) in
                 self.artist = response.result.value
                print(self.artist)
                 viewController.artist = response.result.value
                 self.show(viewController, sender: nil)
            }
        }
        else if(searchController.searchBar.selectedScopeButtonIndex == 2){
                let id = searchResults[indexPath.row]["id"].stringValue
                    let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
                    let secondVC = storyboard.instantiateViewController(withIdentifier: "MusicOthersViewController") as! MusicOthersViewController
                    secondVC.idUser = id
                self.present(secondVC, animated: true)
               // }
            }
            
            else if(searchController.searchBar.selectedScopeButtonIndex == 1){
                    let id = searchResults[indexPath.row]["id"].stringValue
                    Alamofire.request("https://api.deezer.com/album/\(id)").responseObject { (response: DataResponse<Album>) in
                        self.album = response.result.value
                        print(self.album.tracklist!)
                        let storyboard = UIStoryboard(name: "Main", bundle: Bundle.main)
                        let secondVC = storyboard.instantiateViewController(withIdentifier: "MusicPlayerViewController") as! MusicPlayerViewController
                        secondVC.playlist = self.album.tracklist!
                        secondVC.taswiraAlbum = self.album.cover!
                        self.present(secondVC, animated: true)
                    }
    }
    }
}
        
       /*let title = searchResults[indexPath.row]["title"].stringValue
        guard let url = URL.init(string: "https://en.wikipedia.org/wiki/\(title)")
            else { return }*/
        
        /*let safariVC = SFSafariViewController(url: url)
        present(safariVC, animated: true, completion: nil)
        tableView.deselectRow(at: indexPath, animated: true)*/
    
    


extension SearchViewController: UISearchBarDelegate {
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        searchResults.removeAll()
        guard let textToSearch = searchBar.text, !textToSearch.isEmpty else {
            return
        }
        
        if Date().timeIntervalSince(previousRun) > minInterval {
            previousRun = Date()
            fetchResults(for: textToSearch)
        }
    }
    func searchBar(_ searchBar: UISearchBar, selectedScopeButtonIndexDidChange selectedScope: Int) {
      
    
    }
    
    
    
    func fetchResults(for text: String) {
        print("Text Searched: \(text)")
        if(searchController.searchBar.selectedScopeButtonIndex == 0 ){
            apiFetcher.search(searchText: text, completionHandler: {
            [weak self] results, error in
            if case .failure = error {
                return
            }
            
            guard let results = results, !results.isEmpty else {
                return
            }
            
            self?.searchResults = results
        })
    }else
        if(searchController.searchBar.selectedScopeButtonIndex == 1 ){
        
            apiFetcher1.search1(searchText: text, completionHandler: {
                [weak self] results, error in
                if case .failure = error {
                    return
                }
                
                guard let results = results, !results.isEmpty else {
                    return
                }
                
                self?.searchResults = results
            })
        }else
            if(searchController.searchBar.selectedScopeButtonIndex == 2 ){
                apiFetcher2.search2(searchText: text, completionHandler: {
                    [weak self] results, error in
                    if case .failure = error {
                        return
                    }
                    
                    guard let results = results, !results.isEmpty else {
                        return
                    }
                    
                    self?.searchResults = results
                })
        }
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchResults.removeAll()
    }
    
}

extension SearchViewController: ArtistViewModelDelegate {
    
    func didSelect(artist: Artist) {
        let viewController = ArtistViewController(nibName: nil, bundle: nil)
        viewController.artist = artist
        self.show(viewController, sender: nil)
        // self.present(viewController, animated: true, completion: nil)
        // viewController = navigationController?.popViewController(animated: true) as! ArtistViewController
        
    }
}

