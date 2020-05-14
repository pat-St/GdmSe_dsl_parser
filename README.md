# GdmSe Aufgabe 2

## syntax und semantik in EBNF

_root_          := name block
block           := _forms_ width height
_forms_         := _forms_ _form_ | _form_
_form_          := form_name form_type
form_type       := _polygon_ | _rectangle_ | _circle_ | _square_

_polygon_       := points color
points          := _points_list_
_points_list_   := _points_list_ _point_ | _point_
_point_         := Number

_rectangle_     := x y width height color 

_circle_        := x y radius color

_square_        := x y width color

form_name       := String
name            := String
color           := String
width           := Number
height          := Number
x               := Number
y               := Number
radius          := number
