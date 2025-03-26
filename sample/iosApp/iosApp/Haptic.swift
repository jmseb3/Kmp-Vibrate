//
//  Haptic.swift
//  iosApp
//
//  Created by 정원희 on 3/25/25.
//

import CoreHaptics
import ComposeApp


enum HapticCategory {
    case a
    case b
    
    var hapticEvents: [CHHapticEvent] {
        switch self {
        case .a:
            [
                CHHapticEvent(eventType: .hapticContinuous, parameters: [], relativeTime: 0.3, duration: 0.5),
                CHHapticEvent(eventType: .hapticContinuous, parameters: [], relativeTime: 0.9, duration: 0.5),
                CHHapticEvent(eventType: .hapticContinuous, parameters: [], relativeTime: 1.5, duration: 0.5),
            ]
        case .b:
            [CHHapticEvent(eventType: .hapticContinuous, parameters: [], relativeTime: 0.1, duration: 3)]
        }
    }
}

class CustomHaptic  {
    private var engine: CHHapticEngine?
}

private extension CustomHaptic {
    
    func playHaptic(category: HapticCategory) {
        resetEngine() // 밑에서 구현 예정
        
        guard CHHapticEngine.capabilitiesForHardware().supportsHaptics else { return }
        do {
            let pattern = try CHHapticPattern(events: category.hapticEvents, parameters: [])
            let player = try engine?.makePlayer(with: pattern)
            try player?.start(atTime: 0)
        } catch {
            print("CHHapticEvent 구조가 이상하여 에러 발생 \(error.localizedDescription).")
        }
    }
    
    func resetEngine() {
        engine = try? CHHapticEngine()
        try? engine?.start()
        
        setStopHandler()
        setResetHandler()
    }
    
    func setStopHandler() {
        engine?.stoppedHandler = { reason in
            print("엔진이 멈춤 \(reason)")
        }
    }
    
    func setResetHandler() {
        engine?.resetHandler = { [weak self] in
            do {
                try self?.engine?.start()
            } catch {
                print("restart 실패 \(error)")
            }
        }
    }
}
