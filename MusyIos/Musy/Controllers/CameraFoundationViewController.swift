//
//  CameraFoundationViewController.swift
//  Musyy
//
//  Created by Youssef Jdidi on 12/7/18.
//  Copyright © 2018 Youssef Jdidi. All rights reserved.
//

import UIKit
import AVFoundation
class CameraFoundationViewController: UIViewController, AVCaptureFileOutputRecordingDelegate {
    var preview:String?
    
    var player:AVPlayer?
    var playerItem:AVPlayerItem?
    
    func fileOutput(_ output: AVCaptureFileOutput, didFinishRecordingTo outputFileURL: URL, from connections: [AVCaptureConnection], error: Error?) {
        print("kamalt")
        if (error != nil) {
            print("Error recording movie: \(error!.localizedDescription)")
        } else {
            
            let videoRecorded = outputURL as URL
            let a = String(outputURL.absoluteString)
            print(a)
            performSegue(withIdentifier: "showVideo", sender: videoRecorded)
            //  let VC =  VideoViewController(videoURL: videoRecorded)
            // self.present(VC, animated: true, completion: nil)
            
        }
        outputURL = nil
    }
    
    
    
    @IBAction func Back(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    
    
    
    
    @IBOutlet weak var camPreview: UIView!
    let cameraButton = UIView()
    
    let captureSession = AVCaptureSession()
    
    let movieOutput = AVCaptureMovieFileOutput()
    
    var previewLayer: AVCaptureVideoPreviewLayer!
    
    var activeInput: AVCaptureDeviceInput!
    
    var outputURL: URL!
    override func viewDidLoad() {
        super.viewDidLoad()
        print(preview!)
        if  setupSession() {
            setupPreview()
            startSession()
        }
        let playerItem:AVPlayerItem = AVPlayerItem(url: URL.init(string: preview!)!  )
        self.player = AVPlayer(playerItem: playerItem)
        
        
        cameraButton.isUserInteractionEnabled = true
        
        let cameraButtonRecognizer = UITapGestureRecognizer(target: self, action: #selector(startCapture))
        
        cameraButton.addGestureRecognizer(cameraButtonRecognizer)
        
        cameraButton.frame = CGRect(x: UIScreen.main.bounds.width/2, y: 500, width: 60, height: 60)
        cameraButton.layer.cornerRadius = cameraButton.frame.height/2
        cameraButton.backgroundColor = UIColor.white
        
        camPreview.addSubview(cameraButton)
        
        
    }
    
    
    
    func setupPreview() {
        // Configure previewLayer
        previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
        previewLayer.frame = camPreview.bounds
        previewLayer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        camPreview.layer.addSublayer(previewLayer)
    }
    
    func setupSession() -> Bool {
        
        captureSession.sessionPreset = AVCaptureSession.Preset.high
        
        // Setup Camera
        // let camera = AVCaptureDevice.default(for: AVMediaType.video)
        let  camera = AVCaptureDevice.default(.builtInWideAngleCamera, for: AVMediaType.video, position: .front)
        
        do {
            let input = try AVCaptureDeviceInput(device: camera!)
            if captureSession.canAddInput(input) {
                captureSession.addInput(input)
                activeInput = input
            }
        } catch {
            print("Error setting device video input: \(error)")
            return false
        }
        
        
        
        // Movie output
        if captureSession.canAddOutput(movieOutput) {
            captureSession.addOutput(movieOutput)
        }
        
        return true
    }
    
    func setupCaptureMode(_ mode: Int) {
        // Video Mode
        
    }
    
    
    func startSession() {
        
        
        if !captureSession.isRunning {
            videoQueue().async {
                self.captureSession.startRunning()
            }
        }
    }
    
    func stopSession() {
        if captureSession.isRunning {
            videoQueue().async {
                self.captureSession.stopRunning()
            }
        }
    }
    
    func videoQueue() -> DispatchQueue {
        return DispatchQueue.main
    }
    
    
    
    func currentVideoOrientation() -> AVCaptureVideoOrientation {
        var orientation: AVCaptureVideoOrientation
        
        switch UIDevice.current.orientation {
        case .portrait:
            orientation = AVCaptureVideoOrientation.portrait
        case .landscapeRight:
            orientation = AVCaptureVideoOrientation.landscapeLeft
        case .portraitUpsideDown:
            orientation = AVCaptureVideoOrientation.portraitUpsideDown
        default:
            orientation = AVCaptureVideoOrientation.landscapeRight
        }
        
        return orientation
    }
    
    @objc func startCapture() {
        
        startRecording()
        
    }
    
    
    
    func tempURL() -> URL? {
        let directory = NSTemporaryDirectory() as NSString
        
        if directory != "" {
            let path = directory.appendingPathComponent(NSUUID().uuidString + ".mp4")
            return URL(fileURLWithPath: path)
        }
        
        return nil
    }
    
    
    func startRecording() {
        
        if movieOutput.isRecording == false {
            
            let connection = movieOutput.connection(with: AVMediaType.video)
            if (connection?.isVideoOrientationSupported)! {
                connection?.videoOrientation = currentVideoOrientation()
            }
            
            if (connection?.isVideoStabilizationSupported)! {
                connection?.preferredVideoStabilizationMode = AVCaptureVideoStabilizationMode.auto
            }
            
            let device = activeInput.device
            if (device.isSmoothAutoFocusSupported) {
                do {
                    try device.lockForConfiguration()
                    device.isSmoothAutoFocusEnabled = false
                    device.unlockForConfiguration()
                    
                    print("recording")
                } catch {
                    print("Error setting configuration: \(error)")
                }
                
            }
            
           
            outputURL = tempURL()
            player!.play()
            movieOutput.startRecording(to: outputURL, recordingDelegate: self)
            
            
        }
        else {
            player!.pause()
            stopRecording()
           
        }
        
    }
    
    func stopRecording() {
        
        if movieOutput.isRecording == true {
            player!.pause()
            movieOutput.stopRecording()
            
        }
    }
    
    func capture(_ captureOutput: AVCaptureFileOutput!, didStartRecordingToOutputFileAt fileURL: URL!, fromConnections connections: [Any]!) {
        
    }
    
    func capture(_ captureOutput: AVCaptureFileOutput!, didFinishRecordingToOutputFileAt outputFileURL: URL!, fromConnections connections: [Any]!, error: Error!) {
        print("kamalt")
        if (error != nil) {
            print("Error recording movie: \(error!.localizedDescription)")
        } else {
            
            let videoRecorded = outputURL as URL
            let a = String(outputURL.absoluteString)
            print(a)
            performSegue(withIdentifier: "showVideo", sender: videoRecorded)
            
        }
        outputURL = nil
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let vc = segue.destination as! VideoPlayback
        vc.videoURL = sender as! URL
        vc.preview = preview!
        
    }
    
    
    
    
}
