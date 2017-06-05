# SNES Gamepad

A ClojureScript library providing a simple event handler interface for using USB SNES gamepads in HTML5 game projects.

[![Clojars Project](https://img.shields.io/clojars/v/snes-gamepad.svg)](https://clojars.org/snes-gamepad)

For more on the :video_game: Gamepad API, see https://w3c.github.io/gamepad/ and https://developer.mozilla.org/en-US/docs/Web/API/Gamepad_API.

## Quick Start

The SNES Gamepad library allows you to bind handler functions to SNES gamepad events, such as `on-connected` or `on-start-button-pressed`.

```clojure
(ns your.app.namespace
  (:require [snes-gamepad.mapping :as button-mapping]
            [snes-gamepad.gamepad :as snes-gamepad]))

(-> (snes-gamepad/snes-gamepad button-mapping/kiwitata)
    (snes-gamepad/on-connected #(.log js/console "SNES gamepad connected!"))
    (snes-gamepad/on-disconnected #(.log js/console "SNES gamepad disconnected."))
    (snes-gamepad/on-b-button-pressed #(.log js/console "Samus jumps into the air!"))
     snes-gamepad/enable!)
```

## Function Reference

| Function                   | Parameters             | Description                                                                                                                                        |
|----------------------------|------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| `snes-gamepad`             | `button-mapping`       | Returns an SNES gamepad with the given button mapping and no button press handlers.                                                                |
| `enable!`                  | None                   | Enables a connected SNES gamepad. If no gamepad is connected, the browser will wait for one to be connected before firing an `on-connected` event. |
| `disable!`                 | None                   | Disables an SNES gamepad. The browser will stop listening for gamepad connections and button press events until `enable!` is called.               |                                                                                    |
| `on-connected`             | `gamepad handler`      | Binds a handler function to execute when an SNES gamepad is connected.                                                                             |
| `on-disconnected`          | `gamepad handler`      | Binds a handler function to execute when an SNES gamepad is disconnected.                                                                          |
| `on-up-button-pressed`     | `gamepad handler`      | Binds a handler function to execute when the up button is pressed on the directional pad.                                                          |
| `on-down-button-pressed`   | `gamepad handler`      | Binds a handler function to execute when the down button is pressed on the directional pad.                                                        |
| `on-left-button-pressed`   | `gamepad handler`      | Binds a handler function to execute when the left button is pressed on the directional pad.                                                        |
| `on-right-button-pressed`  | `gamepad handler`      | Binds a handler function to execute when the right button is pressed on the directional pad.                                                       |
| `on-a-button-pressed`      | `gamepad handler`      | Binds a handler function to execute when the A button is pressed.                                                                                  |
| `on-b-button-pressed`      | `gamepad handler`      | Binds a handler function to execute when the B button is pressed.                                                                                  |
| `on-x-button-pressed`      | `gamepad handler`      | Binds a handler function to execute when the X button is pressed.                                                                                  |
| `on-y-button-pressed`      | `gamepad handler`      | Binds a handler function to execute when the Y button is pressed.                                                                                  |
| `on-left-bumper-pressed`   | `gamepad handler`      | Binds a handler function to execute when the left bumper is pressed.                                                                               |
| `on-right-bumper-pressed`  | `gamepad handler`      | Binds a handler function to execute when the right bumper is pressed.                                                                              |
| `on-select-button-pressed` | `gamepad handler`      | Binds a handler function to execute when the select button is pressed.                                                                             |
| `on-start-button-pressed`  | `gamepad handler`      | Binds a handler function to execute when the start button is pressed.                                                                              |

## Available Mappings

The SNES Gamepad library comes with two button mappings in the `snes-gamepad.mapping` namespace:

- `snes-gamepad.mapping/kiwitata`: Button mapping for [SNES gamepads manufactured by Kiwitata](http://amzn.to/2pp29ab).
- `snes-gamepad.mapping/standard`: Button mapping for [SNES gamepads that follow the W3C spec](https://w3c.github.io/gamepad/#remapping).

Pull requests for additional mappings are welcome!

## Single-Player Only

The SNES Gamepad library does not currently support multiple gamepads connected to the same computer.
