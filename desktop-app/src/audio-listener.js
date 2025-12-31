/**
 * Audio Listener - Automatic Song Detection
 * Author: Sangam
 * Captures system audio and detects looping/repeating songs
 */

class AudioListener {
    constructor() {
        this.isListening = false;
        this.audioContext = null;
        this.mediaStream = null;
        this.analyser = null;
        this.dataArray = null;
        this.audioFingerprints = [];
        this.currentSongPattern = null;
        this.loopDetectionThreshold = 0.85; // 85% similarity = loop detected
        this.captureInterval = null;
        this.onSongDetected = null;
        this.onLoopDetected = null;
    }

    // Request microphone permission and start listening
    async startListening() {
        if (this.isListening) return { success: true, message: 'Already listening' };

        try {
            // Request microphone permission
            this.mediaStream = await navigator.mediaDevices.getUserMedia({ 
                audio: {
                    echoCancellation: false,
                    noiseSuppression: false,
                    autoGainControl: false,
                    sampleRate: 44100
                } 
            });

            // Setup audio analysis
            this.audioContext = new (window.AudioContext || window.webkitAudioContext)();
            const source = this.audioContext.createMediaStreamSource(this.mediaStream);
            
            this.analyser = this.audioContext.createAnalyser();
            this.analyser.fftSize = 2048;
            this.analyser.smoothingTimeConstant = 0.8;
            
            source.connect(this.analyser);

            const bufferLength = this.analyser.frequencyBinCount;
            this.dataArray = new Uint8Array(bufferLength);

            this.isListening = true;

            // Start continuous audio analysis
            this.startAudioAnalysis();

            return { 
                success: true, 
                message: 'Audio listening started successfully',
                permissionGranted: true
            };

        } catch (error) {
            console.error('Microphone access denied:', error);
            return { 
                success: false, 
                message: 'Microphone permission denied. Please allow access.',
                permissionGranted: false,
                error: error.message
            };
        }
    }

    // Stop listening
    stopListening() {
        this.isListening = false;

        if (this.captureInterval) {
            clearInterval(this.captureInterval);
            this.captureInterval = null;
        }

        if (this.mediaStream) {
            this.mediaStream.getTracks().forEach(track => track.stop());
            this.mediaStream = null;
        }

        if (this.audioContext) {
            this.audioContext.close();
            this.audioContext = null;
        }

        return { success: true, message: 'Audio listening stopped' };
    }

    // Analyze audio patterns continuously
    startAudioAnalysis() {
        let sampleCount = 0;
        const samplesPerFingerprint = 20; // Take fingerprint every 20 samples (~2 seconds)

        this.captureInterval = setInterval(() => {
            if (!this.isListening || !this.analyser) return;

            // Get frequency data
            this.analyser.getByteFrequencyData(this.dataArray);

            // Calculate audio characteristics
            const audioLevel = this.getAverageVolume();
            
            // Only process if there's actual audio (not silence)
            if (audioLevel > 10) {
                const fingerprint = this.createAudioFingerprint();
                
                sampleCount++;
                
                // Create fingerprint periodically
                if (sampleCount >= samplesPerFingerprint) {
                    this.processFingerprint(fingerprint);
                    sampleCount = 0;
                }

                // Check for loops in real-time
                if (this.audioFingerprints.length > 5) {
                    this.detectLoop();
                }
            }

        }, 100); // Analyze every 100ms
    }

    // Create audio fingerprint from current frequency data
    createAudioFingerprint() {
        const fingerprint = {
            timestamp: Date.now(),
            frequency: [],
            energy: 0,
            spectralCentroid: 0,
            spectralRolloff: 0
        };

        // Split frequency spectrum into bands
        const bands = 32;
        const bandSize = Math.floor(this.dataArray.length / bands);

        for (let i = 0; i < bands; i++) {
            let bandEnergy = 0;
            for (let j = 0; j < bandSize; j++) {
                bandEnergy += this.dataArray[i * bandSize + j];
            }
            fingerprint.frequency.push(Math.round(bandEnergy / bandSize));
        }

        // Calculate energy
        fingerprint.energy = fingerprint.frequency.reduce((a, b) => a + b, 0);

        // Calculate spectral centroid (brightness)
        let weightedSum = 0;
        let totalEnergy = 0;
        for (let i = 0; i < fingerprint.frequency.length; i++) {
            weightedSum += i * fingerprint.frequency[i];
            totalEnergy += fingerprint.frequency[i];
        }
        fingerprint.spectralCentroid = totalEnergy > 0 ? weightedSum / totalEnergy : 0;

        return fingerprint;
    }

    // Process new fingerprint
    processFingerprint(fingerprint) {
        this.audioFingerprints.push(fingerprint);

        // Keep only last 100 fingerprints (about 3-4 minutes of audio)
        if (this.audioFingerprints.length > 100) {
            this.audioFingerprints.shift();
        }

        // Notify about audio activity
        if (this.onSongDetected) {
            this.onSongDetected({
                timestamp: fingerprint.timestamp,
                energy: fingerprint.energy,
                brightness: fingerprint.spectralCentroid
            });
        }
    }

    // Detect if audio is looping/repeating
    detectLoop() {
        if (this.audioFingerprints.length < 10) return;

        const recentFingerprints = this.audioFingerprints.slice(-10);
        const olderFingerprints = this.audioFingerprints.slice(0, -10);

        // Compare recent audio with older audio to detect repetition
        for (let i = 0; i < olderFingerprints.length - 10; i++) {
            const comparisonSegment = olderFingerprints.slice(i, i + 10);
            const similarity = this.compareFingerprints(recentFingerprints, comparisonSegment);

            if (similarity > this.loopDetectionThreshold) {
                // Loop detected!
                if (this.onLoopDetected) {
                    this.onLoopDetected({
                        detected: true,
                        similarity: (similarity * 100).toFixed(1),
                        timestamp: Date.now(),
                        message: `Song loop detected (${(similarity * 100).toFixed(0)}% match)`
                    });
                }
                break;
            }
        }
    }

    // Compare two fingerprint sequences
    compareFingerprints(segment1, segment2) {
        if (segment1.length !== segment2.length) return 0;

        let totalSimilarity = 0;

        for (let i = 0; i < segment1.length; i++) {
            const fp1 = segment1[i];
            const fp2 = segment2[i];

            // Compare frequency patterns
            let bandSimilarity = 0;
            for (let j = 0; j < fp1.frequency.length; j++) {
                const diff = Math.abs(fp1.frequency[j] - fp2.frequency[j]);
                const maxVal = Math.max(fp1.frequency[j], fp2.frequency[j]);
                if (maxVal > 0) {
                    bandSimilarity += 1 - (diff / maxVal);
                }
            }
            bandSimilarity = bandSimilarity / fp1.frequency.length;

            // Compare energy
            const energyDiff = Math.abs(fp1.energy - fp2.energy);
            const maxEnergy = Math.max(fp1.energy, fp2.energy);
            const energySimilarity = maxEnergy > 0 ? 1 - (energyDiff / maxEnergy) : 0;

            // Weighted average
            const similarity = (bandSimilarity * 0.7) + (energySimilarity * 0.3);
            totalSimilarity += similarity;
        }

        return totalSimilarity / segment1.length;
    }

    // Get current audio volume level
    getAverageVolume() {
        if (!this.dataArray) return 0;
        
        let sum = 0;
        for (let i = 0; i < this.dataArray.length; i++) {
            sum += this.dataArray[i];
        }
        return sum / this.dataArray.length;
    }

    // Get listening status
    getStatus() {
        return {
            isListening: this.isListening,
            fingerprintsCollected: this.audioFingerprints.length,
            currentVolume: this.isListening ? this.getAverageVolume() : 0
        };
    }

    // Clear collected fingerprints
    clearFingerprints() {
        this.audioFingerprints = [];
    }
}

// Export for use in renderer
if (typeof module !== 'undefined' && module.exports) {
    module.exports = AudioListener;
}
