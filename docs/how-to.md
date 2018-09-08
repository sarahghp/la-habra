# How-To La Habra

Hello and welcome to a description of the La Habra API. Please feel free to download/fork and mess around with the library. [Tweet me](https://twitter.com/superSGP) with what you make! 

But **Beware**: This is an active art tool that will shift and evolve as my practice does and it comes with no guarantees of support, updates, or bugfixes. At any point after September 2018, this documentation may be out of date, though I will do my best to be sure it is not.

These functions are best paired with a set of [snippets for Atom](https://github.com/sarahgp/la-habra-snippets), so you don't have to remember function signatures in the middle of a set. Those 🤞🏽 should 🤞🏽 always be up-to-date.

Caveats given; on with the show!

## The Basics

La Habra is a Clojurescript—Figwheel—Electron app. That means the graphics code is written in Clojurescript ([reference](http://cljs.info/cheatsheet/) | [koans](http://clojurescriptkoans.com/) | [tutorial](https://jaredforsyth.com/tutorial-cljs/)), which is live-reloaded by Figwheel and run in an Electron app window.

Everything is drawn into a single SVG via [Reagent](https://reagent-project.github.io/). That means you can use any CSS style that works on an SVG in the map attached to an element. (Check out the link for more about the syntax.)

```cljs
[:circle {:cx     100
          :cy     100
          :r      20
          :style {:opacity .7}}] // add any style
```

### Starting the App

To start the application, you must start both Figwheel and Electron.

#### Starting Figwheel
 In one terminal window, start Figwheel with 
```
lein cooper
```
or 
```
npm start
```

If this fails, try removing the `target` directory.

#### Starting Electron

Start Electron with 
```
electron .
```

or 

```
npm server
```

### Where to Put the Code

All the relevant code is  in [`ui_src/ui`](https://github.com/sarahgp/la-habra/tree/master/ui_src/ui). In particular, `core.cljs` is the only file you will need at performance time. It imports all the necessary dependencies.

### Cx List

This is the core of the whole shebang. Everything you want drawn into the SVG must be represented in the list returned `cx` function, located towards the bottom of the `core.cljs` file. (Since Clojurescript is read in order, everything must be defined before it is used — no hoisting here!)

```cljs
(defn cx [frame]
  (list
  
  [:circle {:cx     100
            :cy     100
            :r      20
            :style {:opacity .7}}]
 
    
  )) ;
```

The frame value is passed to the function each time the frame is incremented.

### The Timers

The timers take care of incrementing the frame and redrawing the SVG. Both are evaluated once when the code loads initially. The app must be refreshed manually to reset the values.

`start-cx-timer` redraws the SVG every 50ms. `start frame timer` increments the frame value every 500ms. Both values can be changed. 

## Generating Shapes

Ok, so, you want to add some shapes to your SVG! You could, if you wanted to, add shapes by writing the SVG code with the Reagent/Hiccup syntax.

```cljs
[:circle {:cx     100
          :cy     100
          :r      20
          :style {:opacity .7}}]
```

However, La Habra provides functions for generating and varying the underlying hashmaps as data and then drawing them when the manipulation is complete. This is what is produced when the [related snippets](https://github.com/sarahgp/la-habra-snippets) are invoked. To create the circle as above, we would write:

```cljs
(->>
  (gen-circ "#e0f" 100 100 20) ; arguments are fill, cx, cy, and r
  (draw))                      ; transforms the data from gen-circ into reagent
```

If we wanted the circle to only appear every fourth frame, we can use the `nth-frame` function.

```cljs
(->>
  (gen-circ "#e0f" 100 100 20)
  (draw)
  (when (nth-frame 4 frame))) ; uses the frame argument passed to `cx`
```

To show every frame BUT the fourth, we can use [`when-not`](http://clojuredocs.org/clojure.core/when-not).

```cljs
(->>
  (gen-circ "#e0f" 100 100 20)
  (draw)
  (when-not (nth-frame 4 frame))) ; uses the frame argument passed to `cx`
```

All of these examples (and snippets) use the Clojurescript [thread-last macro](http://clojuredocs.org/clojure.core/-%3E%3E), which calls each function, from top to bottom, with the result of the previous function being passed as the last argument to the next one. In this case, the basic circle data is passed to draw and then the code representing the circle is passed to `when`, which only adds it to the list of elements in the SVG when the predicate, `(nth-frame 4 frame)`, returns true.


### Basic Shapes

These basic shapes have generator helpers. Each takes a set of mandatory arguments and most an optional `mask` argument. 

Each must be passed to `draw` in order to be rendered.

#### `gen-circ`  
arguments: `fill-string x y radius & mask`

`fill-string` is any CSS-acceptable color string: hex, hsla, rgba. 

`x` `y` and `radius` are values accepted by SVG: plain numbers, strings with measurement specified (e.g., `"100px"`, `"40vw"`, etc.)

The optional `mask` argument must be the ID to a mask definition. These can be generated using `url` — `(url "mask-name")`. See `masks` below for more information.

**EXAMPLE**
```cljs
(->>
  (gen-circ "#e0f" 100 100 20)
  (draw))       
```

#### `gen-rect`  
arguments: `fill-string x y w h & mask`

`fill-string` is any CSS-acceptable color string: hex, hsla, rgba.

`x` `y` `w` and `h` are values accepted by SVG: plain numbers, strings with measurement specified (e.g., `"100px"`, `"40vw"`, etc.)

The optional `mask` argument must be the ID to a mask definition. These can be generated using `url` — `(url "mask-name")`. See `masks` below for more information. 

**EXAMPLE**
```cljs
  (->>
    (gen-rect "#e0f" 100 100 500 100)
    (draw))
```
 
#### `gen-line`
arguments: `first-point second-point color & width`

`first-point` and `second-point` expect vectors representing the start and end points of the line: `[x y]`,

`color` is any CSS-acceptable color string: hex, hsla, rgba. It will be the stroke color of the line.

The optional `width` argument sets the stroke-width. It defaults to `4px` if no value is passed.

**EXAMPLE**
```cljs

(->>
  (gen-line [40 100] [80 400] "#e0f")
  (draw))

(->>
  (gen-line [40 100] [80 400] "#e0f" 10)
  (draw))
```

#### `gen-poly`
arguments: `fill-string points & mask`

`fill-string` is any CSS-acceptable color string: hex, hsla, rgba.

`points` is a vector of points for the polygon. See [the SVG polygon element spec](https://developer.mozilla.org/en-US/docs/Web/SVG/Element/polygon) for more information.

The optional `mask` argument must be the ID to a mask definition. These can be generated using `url` — `(url "mask-name")`. See `masks` below for more information. 

**EXAMPLE**
```cljs
(->>
  (gen-poly "#e0f" [100 100 400 400 300 100 200 50])
  (draw))
```

#### `gen-shape`
arguments: `fill-string path & mask`

`fill-string` is any CSS-acceptable color string: hex, hsla, rgba.

`path` is an SVG path string (i.e., what you would pass to the `d` attribute). See [the SVG path spec](https://developer.mozilla.org/en-US/docs/Web/SVG/Element/path) for more information.

`path` can also be one of geometric paths already defined in La Habra — `tri` `square` `pent` `hex` `hept` `oct`.

The optional `mask` argument must be the ID to a mask definition. These can be generated using `url` — `(url "mask-name")`. See `masks` below for more information. 

**EXAMPLE**
```cljs
(->>
  (gen-shape "#e0f" hex)
  (draw))

(->>
  (gen-shape "#e0f" "M92.6912391 1.03515625 184.526999 51.461505 184.526999 101.419922 184.526999 152.314202 92.6912391 202.740551 0.855479021 152.314202 0.855479021 98.7460938 0.855479021 51.461505z")
  (draw))
```

### Changing Styles

What if you want to change the basic shapes? What if you want to adjust the opacity, add an attribute not covered by the basic shape generation functions, or translate, rotate, and scale?

`style` to the rescue!

#### `style`
arguments: `changes shape`

`changes` is a hashmap of style attributes and values to be added to the `shape`, the map of shape data produced by the `gen-*` functions.

`style` is usually used with the thread-last macro. It can be called multiple times and later values will overwrite earlier values. By default, all shapes have `transform-origin: center` applied as a default style.

**EXAMPLE**
```cljs
; draws an octagon with dashed outline, scaled up
; and moved to the center-ish of the viewport
; pink and oct are defined in La Habra
(->>
  (gen-shape "hsla(360, 10%, 10%, 0)" oct)
  (style {:transform "translate(40vw, 40vh) scale(2)")})
  (style {:stroke pink 
          :stroke-width 10 
          :stroke-dasharray 20 
          :stroke-dashoffset 1000
          :stroke-linecap :round
          :stroke-linejoin :round})
  (draw))
```

### Compound Generators

These generate more complex sets of shapes.

#### `gen-bg-lines`
arguments: `color num style`

Generates a number of lines of increasing width.

`color` is any CSS-acceptable color string: hex, hsla, rgba.

`num` is the number of lines to draw.

The optional `style` argument takes the same sort of styles hashmap one would pass to the `style` function.

**EXAMPLE**
```cljs
(gen-bg-lines "#e0f" 
              (mod frame 80)
              {:opacity .5})
```


#### `gen-cols`
arguments: `color stroke-width num-cols offset`

`color` is any CSS-acceptable color string: hex, hsla, rgba.

`stroke-width` `num-cols` and `offset` are numbers representing those attributes. `offset` determines how far apart the lines will be and does **not** take stroke width into account.


**EXAMPLE**
```cljs
(gen-cols "#e0f" 4 40 20)
```


#### `gen-rows`
arguments: `color stroke-width num-rows offset`

`color` is any CSS-acceptable color string: hex, hsla, rgba.

`stroke-width` `num-rows` and `offset` are numbers representing those attributes. `offset` determines how far apart the lines will be and does **not** take stroke width into account.


**EXAMPLE**
```cljs
(gen-rows "#e0f" 4 40 20)
```


#### `gen-line-grid`
arguments: `color stroke-width num-cols num-rows offset`

This function generates the columns returned by `gen-cols` and the rows from `gen-rows` together.

`color` is any CSS-acceptable color string: hex, hsla, rgba.

`stroke-width` `num-cols` and `num-rows` are numbers representing those attributes.

`offset` is a map with keys for row and column offsets: `{:row 20 :col 10}`.


**EXAMPLE**
```cljs
(gen-line-grid "#e0f" 4 80 40 {:row 20 :col 10})
```


#### `gen-grid`
arguments: `num-cols num-rows offset base-obj`

This function creates a grid from whatever base object is passed to it.

`num-cols` and `num-rows` are numbers representing those attributes.

`offset` is a map with keys for row and column offsets: `{:row 20 :col 10}`.

`base-obj` is any shape to be repeated.

**EXAMPLE**
```cljs
(->>
  (gen-grid
    10 10
    {:col 100 :row 100}
    (gen-circ "#e0f" 10 10 10)) 
   (map #(style {:opacity .5} %)) ; note: style and draw must map over the group
   (map draw))
```


#### `freak-out`
arguments: `max-x max-y max-r num color`  
OR `max-x max-y max-r num color style`  
OR `min-x max-x min-y max-y max-r num color`  
OR `min-x max-x min-y max-y max-r num color style`

This function generates a new set of circles each redraw (so every 50ms). It gives a sparkle effect.

`min-x` `max-x` `min-y` and `max-y` are numbers that together define the square in which the circles appear. If only two values, `max-x` and `max-y`, are passed, the minimum is set to `0`. 

`max-r` is the maximum radius. The minimum is always 0.

`color` is any CSS-acceptable color string: hex, hsla, rgba.

The optional `style` argument takes the same sort of styles hashmap one would pass to the `style` function.


**EXAMPLE**
```cljs
;; this example shows freakout with the minimum number of arguments
(freak-out @width   ;@width is a helper that provides the width of the window
           @height  ;@height is a helper that provides the height of the window
           10  
           100
           "#e0f")
           
;; this example shows freakout with the maximum number of arguments           
(freak-out 200 @width
           200 @height
           10  
           100
           "#e0f"
           {:opacity .5})
```


## Fills
### Solid
### Patterned

## Animating Shapes with Atoms 

## Groups
## Masks 
