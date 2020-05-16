# GdmgSe Aufgabe 2

Patrick Sterk

Stefan Schmunk

## Formale Spezifikation in EBNF als Metasprache

|      |      ||
| ---- | :----: |---|
|name      |=| String|
|x              | =| Number|
|y               |=| Number|
|width       |=| Number|
|height      |=| Number|
|radius      |=| Number |
|color         |=| String|
|root          |=| name, x, y, color, shapes |
|shapes        |=| shape, { shape }|
|shape       |=|   rectangle \| circle \| square|
|rectangle  |   =| x, y, width, height, color |
|circle       | =| x, y, radius, color |
|square |       =| x, y, width, color |

## Beispiel

### Eingabe per Kotlin DSL

```kotlin
buildShape() {
    name = "Test"
    x = 30
    y = 41
    color = "Black"
    rectangle {
        x = 3
        y = 5
        height = 43
        width = 3
        color = "Green"
    }
    rectangle {
        x = 3
        y = 40
        height = 43
        width = 3
        color = "Blue"
    }
    circle {
        x = 5
        y = 10
        radius = 5
        color = "Red"
    }
    square {
        x = 10
        y = 5
        width = 3
        color = "Yellow"
    }
}
```

### Ausgabe der DSL

```json
{
  "name": "Test",
  "width": 30,
  "height": 41,
  "color": "Black",
  "shapes": [
    {
      "x": 3,
      "y": 5,
      "height": 43,
      "width": 3,
      "color": "Green"
    },
    {
      "x": 3,
      "y": 40,
      "height": 43,
      "width": 3,
      "color": "Blue"
    },
    {
      "x": 5,
      "y": 10,
      "radius": 5,
      "color": "Red"
    },
    {
      "x": 10,
      "y": 5,
      "width": 3,
      "color": "Yellow"
    }
  ]
}
```

#### Weitere Beispiele

```json
{
  "name": "Only Rectangles",
  "width": 100,
  "height": 200,
  "color": "White",
  "shapes": [
    {
      "x": 10,
      "y": 10,
      "height": 10,
      "width": 10,
      "color": "Yellow"
    },
    {
      "x": 20,
      "y": 20,
      "height": 20,
      "width": 20,
      "color": "Red"
    },
    {
      "x": 30,
      "y": 30,
      "height": 30,
      "width": 30,
      "color": "Blue"
    },
    {
      "x": 40,
      "y": 40,
      "height": 40,
      "width": 40,
      "color": "Black"
    }
  ]
}
{
  "name": "Only circles",
  "width": 10,
  "height": 10,
  "color": "Blue",
  "shapes": [
    {
      "x": 2,
      "y": 3,
      "radius": 5,
      "color": "Red"
    }
  ]
}
```

### Link

https://github.com/pat-St/GdmSe_dsl_parser