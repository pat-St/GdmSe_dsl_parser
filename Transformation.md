# GdmgSe Aufgabe 3

Patrick Sterk

Stefan Schmunk

## Funktion

Generierung von Formen in HTML SVG Elementen.
## M2T: Eigenschaften
- Universeller Generator mit externer Abbildungsvorschrift 
  - Verwendung von Templates
- Plattformunabhängiges Zielmodell
- Generierter und manueller Code in Übersetztungseinheit gemischt
  - [Koreander](https://github.com/lukasjapan/koreander) als HTML Template Engine für Kotlin

### Template Code
```bash
!!!
%html
    %body
        %h1 ${name}
        %svg width=${width} height=${height}
            - shapes.filter{ i -> i.type.equals("Rectangle")}.forEach -> rect
                %rect fill=${rect.color} height=${rect.height} width=${rect.width} x=${rect.x} y=${rect.y}
            - shapes.filter{ i -> i.type.equals("Circle")}.forEach -> circle
                %circle fill=${circle.color} r=${circle.radius}  cx=${circle.x} cy=${circle.y}
            - shapes.filter{ i -> i.type.equals("Square")}.forEach -> rect
                %rect fill=${rect.color} height=${rect.width} width=${rect.width} x=${rect.x} y=${rect.y}
```
## Beispiel
### Kotlin DSL (Quellmodell)

```kotlin
val stickFigure: ShapeBuilder.() -> Unit = {
        name = "stick figure"
        x = 100
        y = 100
        rectangle {
            x = 29
            y = 18
            height = 30
            width = 2
            color = "Green"
        }
        rectangle {
            x = 10
            y = 25
            height = 2
            width = 40
            color = "Blue"
        }
        rectangle {
            x = 20
            y = 46
            height = 2
            width = 20
            color = "Black"
        }
        rectangle {
            x = 20
            y = 46
            height = 30
            width = 2
            color = "Red"
        }
        rectangle {
            x = 40
            y = 46
            height = 30
            width = 2
            color = "Red"
        }
        circle {
            x = 30
            y = 10
            radius = 8
            color = "Red"
        }
    }
```
### Zielmodell
```html
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <body>
        <h1>stick figure</h1>
        <svg height="100" width="100">
            <rect fill="Green" height="30" width="2" x="29" y="18"></rect>
            <rect fill="Blue" height="2" width="40" x="10" y="25"></rect>
            <rect fill="Black" height="2" width="20" x="20" y="46"></rect>
            <rect fill="Red" height="30" width="2" x="20" y="46"></rect>
            <rect fill="Red" height="30" width="2" x="40" y="46"></rect>
            <circle cx="30" cy="10" fill="Red" r="8"></circle>
    	</svg>
    </body>
</html>
```
### Browser Resultat
![](stick_figure_result.png)

### Link
https://github.com/pat-St/GdmSe_dsl_parser