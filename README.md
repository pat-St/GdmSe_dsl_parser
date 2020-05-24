# GdmgSe Aufgabe 2

Patrick Sterk

Stefan Schmunk

## Funktion

Aus Konfigurationen JSON Objekte generieren, die Formen repräsentieren.

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
|shape       |=| rectangle \| circle \| square |
|rectangle  |   =| x, y, width, height, color |
|circle       | =| x, y, radius, color |
|square |       =| x, y, width, color |