(ns snes-gamepad.mapping)

; Implementation of the Kiwitata SNES Gamepad: http://amzn.to/2pp29ab

(def kiwitata {:up-pressed           (fn [usb] (= (aget usb "axes" 1) -1))
               :down-pressed         (fn [usb] (= (aget usb "axes" 1) 1))
               :left-pressed         (fn [usb] (= (aget usb "axes" 0) -1))
               :right-pressed        (fn [usb] (= (aget usb "axes" 0) 1))
               :a-pressed            (fn [usb] (aget usb "buttons" 1 "pressed"))
               :b-pressed            (fn [usb] (aget usb "buttons" 2 "pressed"))
               :x-pressed            (fn [usb] (aget usb "buttons" 0 "pressed"))
               :y-pressed            (fn [usb] (aget usb "buttons" 3 "pressed"))
               :left-bumper-pressed  (fn [usb] (aget usb "buttons" 4 "pressed"))
               :right-bumper-pressed (fn [usb] (aget usb "buttons" 5 "pressed"))
               :select-pressed       (fn [usb] (aget usb "buttons" 8 "pressed"))
               :start-pressed        (fn [usb] (aget usb "buttons" 9 "pressed"))})

; Implementation of the Standard SNES Gamepad as defined by
; the W3C specification: https://w3c.github.io/gamepad/.

(def standard {:up-pressed           (fn [usb] (aget usb "buttons" 12 "pressed"))
               :down-pressed         (fn [usb] (aget usb "buttons" 13 "pressed"))
               :left-pressed         (fn [usb] (aget usb "buttons" 14 "pressed"))
               :right-pressed        (fn [usb] (aget usb "buttons" 15 "pressed"))
               :a-pressed            (fn [usb] (aget usb "buttons" 1 "pressed"))
               :b-pressed            (fn [usb] (aget usb "buttons" 0 "pressed"))
               :x-pressed            (fn [usb] (aget usb "buttons" 3 "pressed"))
               :y-pressed            (fn [usb] (aget usb "buttons" 2 "pressed"))
               :left-bumper-pressed  (fn [usb] (aget usb "buttons" 4 "pressed"))
               :right-bumper-pressed (fn [usb] (aget usb "buttons" 5 "pressed"))
               :select-pressed       (fn [usb] (aget usb "buttons" 8 "pressed"))
               :start-pressed        (fn [usb] (aget usb "buttons" 9 "pressed"))})
