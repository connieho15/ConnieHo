//
//  ViewController.swift
//  MusicPlayer-iOS-Workshop
//
//  Created by Admin on 2016-09-09.
//  Copyright © 2016 LHL. All rights reserved.
//

import UIKit

class PlayerViewController: UIViewController, MediaPlayerProtocol {
    
    @IBOutlet var mediaPlayer: MediaPlayer!
    
    @IBOutlet weak var progressView: UIProgressView!
    @IBOutlet weak var imageView: UIImageView!

    @IBOutlet weak var artistName: UILabel!
    @IBOutlet weak var songTitle: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        mediaPlayer.setup()
    }
    
    func displaySong(song: Song) {
        imageView.image = song.image
        songTitle.text = song.title;
        artistName.text = song.artist;
    }
    
    func displayCompletionPercentage(percentComplete: Float) {
        progressView.progress = percentComplete
    }
}

