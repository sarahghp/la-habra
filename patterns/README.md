## About Patterns
Generated using [Pattern Fills from Irene Ros](https://github.com/iros/patternfills).

This is the basic command to create a new color svg set from the base patterns.
```bash
patternfills -f svg -d "DESTINATION/FILE.svg" -b "background-color" -g "foreground-color"
```

For self-defined patterns, the command is 
```bash
patternfills -f svg -d "DESTINATION/FILE.svg" -b "background-color" -g "foreground-color" -s "SOURCE/DIR"
```


patternfills -f svg -d "./self-pink.svg" -b "hsla(356, 95%, 67%, 0)" -g "hsla(356, 95%, 67%, 1)" -s "./sources"
