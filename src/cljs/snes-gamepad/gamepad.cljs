(ns snes-gamepad.gamepad)

(defn- get-connected-gamepad
  "Updates the given gamepad with a handle to the physical USB-connected device.
   The browser returns null if there is currently no USB-connected gamepad."
  [gamepad]
  (let [gamepad-handle (aget (.getGamepads js/navigator) 0)]
    (assoc gamepad :usb gamepad-handle)))

(defn- gamepad-connected?
  "Returns whether the given gamepad is currently connected via USB."
  [gamepad]
  (not (nil? (gamepad :usb))))

(defn- execute-button-press-handlers
  "Sequentially executes a vector of button press handler functions."
  [handlers]
  (doseq [h handlers] (h)))

(defn- flagged-connected-previous-frame
  "Flags the given gamepad as having been connected via USB during the previous
   animation frame."
  [gamepad]
  (assoc gamepad :connected-previous-frame true))

(defn- flagged-disconnected-previous-frame
  "Flags the given gamepad as having been disconnected during the previous
   animation frame."
  [gamepad]
  (assoc gamepad :connected-previous-frame false))

(defn- was-connected-previous-frame?
  "Returns whether the given gamepad was connected via USB during the previous
  animation frame. Necessary when determining whether a gamepad has just been
  connected or disconnected."
  [gamepad]
  (gamepad :connected-previous-frame))

(defn- handle-connected-gamepad
  "Handles connection and button press events for a gamepad
   currently connected via USB. Returns the given gamepad after flagging it
   as connected during the current animation frame."
  [gamepad]
  (when (not (was-connected-previous-frame? gamepad))
    (execute-button-press-handlers (get-in gamepad [:handlers :connected])))
  (handle-gamepad-input gamepad)
  (flagged-connected-previous-frame gamepad))

(defn- handle-disconnected-gamepad
  "Handles disconnection events for a gamepad that is
   not currently connected via USB. Returns the given gamepad after flagging it
   as disconnected during the current animation frame."
  [gamepad]
  (when (was-connected-previous-frame? gamepad)
    (execute-button-press-handlers (get-in gamepad [:handlers :disconnected])))
  (flagged-disconnected-previous-frame gamepad))

(defn- up-pressed?
  "Returns whether the up directional pad button is currently being
   pressed on the given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :up-pressed]) (gamepad :usb)))

(defn- down-pressed?
  "Returns whether the down directional pad button is currently being
   pressed on the given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :down-pressed]) (gamepad :usb)))

(defn- left-pressed?
  "Returns whether the left directional pad button is currently being
   pressed on the given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :left-pressed]) (gamepad :usb)))

(defn- right-pressed?
  "Returns whether the right directional pad button is currently being
   pressed on the given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :right-pressed]) (gamepad :usb)))

(defn- a-pressed?
  "Returns whether the A button is currently being pressed on the
   given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :a-pressed]) (gamepad :usb)))

(defn- b-pressed?
  "Returns whether the B button is currently being pressed on the
   given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :b-pressed]) (gamepad :usb)))

(defn- x-pressed?
  "Returns whether the X button is currently being pressed on the
   given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :x-pressed]) (gamepad :usb)))

(defn- y-pressed?
  "Returns whether the Y button is currently being pressed on the
   given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :y-pressed]) (gamepad :usb)))

(defn- left-bumper-pressed?
  "Returns whether the left bumper is currently being pressed on the
   given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :left-bumper-pressed]) (gamepad :usb)))

(defn- right-bumper-pressed?
  "Returns whether the right bumper is currently being pressed on the
   given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :right-bumper-pressed]) (gamepad :usb)))

(defn- select-pressed?
  "Returns whether the select button is currently being pressed on the
   given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :select-pressed]) (gamepad :usb)))

(defn- start-pressed?
  "Returns whether the start button is currently being pressed on the
   given gamepad."
  [gamepad]
  ((get-in gamepad [:button-mapping :start-pressed]) (gamepad :usb)))

(defn- handle-gamepad-input
  "Checks a gamepad connected via USB for buttons press events and
   executes button press handler functions when necessary."
  [gamepad]
  (let [handlers (gamepad :handlers)]
    (when (up-pressed? gamepad)
      (execute-button-press-handlers (handlers :up)))
    (when (down-pressed? gamepad)
      (execute-button-press-handlers (handlers :down)))
    (when (left-pressed? gamepad)
      (execute-button-press-handlers (handlers :left)))
    (when (right-pressed? gamepad)
      (execute-button-press-handlers (handlers :right)))
    (when (a-pressed? gamepad)
      (execute-button-press-handlers (handlers :a)))
    (when (b-pressed? gamepad)
      (execute-button-press-handlers (handlers :b)))
    (when (x-pressed? gamepad)
      (execute-button-press-handlers (handlers :x)))
    (when (y-pressed? gamepad)
      (execute-button-press-handlers (handlers :y)))
    (when (left-bumper-pressed? gamepad)
      (execute-button-press-handlers (handlers :left-bumper)))
    (when (right-bumper-pressed? gamepad)
      (execute-button-press-handlers (handlers :right-bumper)))
    (when (select-pressed? gamepad)
      (execute-button-press-handlers (handlers :select)))
    (when (start-pressed? gamepad)
      (execute-button-press-handlers (handlers :start)))))

(defn- request-animation-frame
  "Asks the browser to listen for gamepad events during the next
   animation frame. Returns the given gamepad updated with a reference to
   the browser's animation frame event should it need to be cancelled."
  [gamepad]
  (assoc gamepad :animation-frame
    (.requestAnimationFrame js/window
      (partial button-press-listener-fn gamepad))))

(defn- cancel-animation-frame
  "Cancels gamepad event listening starting with the next animation frame."
  [gamepad]
  (.cancelAnimationFrame js/window
    (gamepad :animation-frame)))

(defn- button-press-listener-fn
  "Returns a function to be called each animation frame that listens for
   connection, disconnection, and button press events."
  [gamepad]
  (let [gamepad (get-connected-gamepad gamepad)]
    (if (gamepad-connected? gamepad)
      (-> gamepad
          handle-connected-gamepad
          request-animation-frame)
      (-> gamepad
          handle-disconnected-gamepad
          request-animation-frame))))

(defn- with-button-press-handler
  "Returns a gamepad with the given handler function registered for the given
   button press trigger event."
  [gamepad trigger handler]
  (update-in gamepad [:handlers trigger] conj handler))

(defn snes-gamepad
  "Returns an SNES gamepad with the given button mapping and no
   button press handlers."
  [button-mapping]
  {:handlers {
      :connected []
      :disconnected []
      :up []
      :down []
      :left []
      :right []
      :a []
      :b []
      :x []
      :y []
      :left-bumper []
      :right-bumper []
      :select []
      :start []}
    :button-mapping button-mapping
    :usb nil
    :animation-frame 0
    :connected-previous-frame false})

(defn enable!
  "Enables a connected SNES gamepad. If no gamepad is connected, the browser
   will wait for one to be connected before firing an on-connected event."
  [gamepad]
  (request-animation-frame gamepad))

(defn disable!
  "Disables an SNES gamepad. The browser will stop listening for gamepad
   connections and button press events until enable! is called."
  [gamepad]
  (cancel-animation-frame gamepad))

(defn on-connected
  "Binds a handler function to execute when an SNES gamepad is connected."
  [gamepad handler]
  (with-button-press-handler gamepad :connected handler))

(defn on-disconnected
  "Binds a handler function to execute when an SNES gamepad is disconnected."
  [gamepad handler]
  (with-button-press-handler gamepad :disconnected handler))

(defn on-up-button-pressed
  "Binds a handler function to execute when the up button is pressed on
   the directional pad."
  [gamepad handler]
  (with-button-press-handler gamepad :up handler))

(defn on-down-button-pressed
  "Binds a handler function to execute when the down button is pressed on
   the directional pad."
  [gamepad handler]
  (with-button-press-handler gamepad :down handler))

(defn on-left-button-pressed
  "Binds a handler function to execute when the left button is pressed on
   the directional pad."
  [gamepad handler]
  (with-button-press-handler gamepad :left handler))

(defn on-right-button-pressed
  "Binds a handler function to execute when the right button is pressed on
   the directional pad."
  [gamepad handler]
  (with-button-press-handler gamepad :right handler))

(defn on-a-button-pressed
  "Binds a handler function to execute when the A button is pressed."
  [gamepad handler]
  (with-button-press-handler gamepad :a handler))

(defn on-b-button-pressed
  "Binds a handler function to execute when the B button is pressed."
  [gamepad handler]
  (with-button-press-handler gamepad :b handler))

(defn on-x-button-pressed
  "Binds a handler function to execute when the X button is pressed."
  [gamepad handler]
  (with-button-press-handler gamepad :x handler))

(defn on-y-button-pressed
  "Binds a handler function to execute when the Y button is pressed."
  [gamepad handler]
  (with-button-press-handler gamepad :y handler))

(defn on-left-bumper-pressed
  "Binds a handler function to execute when the left bumper is pressed."
  [gamepad handler]
  (with-button-press-handler gamepad :left-bumper handler))

(defn on-right-bumper-pressed
  "Binds a handler function to execute when the right bumper is pressed."
  [gamepad handler]
  (with-button-press-handler gamepad :right-bumper handler))

(defn on-select-button-pressed
  "Binds a handler function to execute when the select button is pressed."
  [gamepad handler]
  (with-button-press-handler gamepad :select handler))

(defn on-start-button-pressed
  "Binds a handler function to execute when the start button is pressed."
  [gamepad handler]
  (with-button-press-handler gamepad :start handler))
