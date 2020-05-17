package org.dsl

import com.google.gson.GsonBuilder
import java.security.InvalidParameterException

sealed class Shape(x: Int, y: Int, color: String)

data class Root(val name: String, val width: Int, val height: Int, var color: String, val shapes: List<Shape>) : Shape(width, height, color)
data class Rectangle(val x: Int, val y: Int, val height: Int, val width: Int, val color: String) : Shape(x, y, color)
data class Circle(val x: Int, val y: Int, val radius: Int, val color: String) : Shape(x, y, color)
data class Square(val x: Int, val y: Int, val width: Int, val color: String) : Shape(x, y, color)

abstract class AbstractShapeBuilder {
    abstract val className: String
    abstract var x: Int?
    abstract var y: Int?
    abstract var color: String?
    abstract fun build(): Shape
}

internal fun String?.testNull(className: String, text: String): String {
    if (this == null) throw IllegalArgumentException("""In $className: $text is not set""")
    if (this.isBlank()) throw InvalidParameterException("""In $className: $this in $text is not allowed""")
    return this
}

internal fun Int?.testNegative(className: String, text: String): Int {
    if (this == null) throw IllegalArgumentException("""In $className: $text is not set""")
    if (this <= 0) throw InvalidParameterException("""In $className: $text should be greater 0""")
    return this
}

internal fun Int.cmp(className: String, text: String, parentSize: Int): Int {
    if (this >= parentSize) throw InvalidParameterException("""In $className: $text must be lower than $parentSize""")
    return this
}

internal fun List<Shape>.testEmpty(className: String, text: String): List<Shape> {
    if (this.isEmpty()) throw NoSuchElementException("""In $className: no $text found""")
    return this
}

class ShapeBuilder(private var parentX: Int? = null, private var parentY: Int? = null) : AbstractShapeBuilder() {
    override val className = "ShapeBuilder"
    override var x: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "x")
        }
    override var y: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "y")
        }
    override var color: String? = null
        set(value: String?) {
            field = value.testNull(className, "color")
        }
    var name: String? = null
        set(value: String?) {
            field = value.testNull(className, "name")
        }

    private val shapes: MutableList<Shape> = ArrayList()

    override fun build(): Root {
        val buildName = "$className under build"
        val testedX = if (parentX == null) { x.testNegative(buildName, "x") } else { x.testNegative(buildName, "x").cmp(buildName, "x", parentX!!) }
        val testedY = if (parentY == null) { y.testNegative(buildName, "y") } else { y.testNegative(buildName, "y").cmp(buildName, "x", parentY!!) }
        val testedColor = color.testNull(buildName, "color")
        val testedName = name.testNull(buildName, "name")
        val testedShapes = shapes.testEmpty(buildName, "shape")
        return Root(testedName,  testedX, testedY, testedColor, testedShapes)
    }

    fun rectangle(shape: RectangleBuilder.() -> Unit) {
        val r = RectangleBuilder(x.testNegative(className, "x"), y.testNegative(className, "y"))
        r.shape()
        shapes.add(r.build())
    }

    fun circle(shape: CircleBuilder.() -> Unit) {
        val c = CircleBuilder(x.testNegative(className, "x"), y.testNegative(className, "y"))
        c.shape()
        shapes.add(c.build())
    }

    fun square(shape: SquareBuilder.() -> Unit) {
        val s = SquareBuilder(x.testNegative(className, "x"), y.testNegative(className, "y"))
        s.shape()
        shapes.add(s.build())
    }

    fun root(shape: ShapeBuilder.() -> Unit) {
        val s = ShapeBuilder(x.testNegative(className, "x"), y.testNegative(className, "y"))
        s.shape()
        shapes.add(s.build())
    }
}

class RectangleBuilder(private var parentX: Int, private var parentY: Int) : AbstractShapeBuilder() {
    override val className: String = "RectangleBuilder"
    override var x: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "x")
        }
    override var y: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "y")
        }
    override var color: String? = null
        set(value: String?) {
            field = value.testNull(className, "color")
        }
    var height: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "height")
        }
    var width: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "width")
        }

    override fun build(): Rectangle {
        val buildName = "$className under build"
        x.testNegative(buildName, "x").cmp(buildName, "x", parentX)
        y.testNegative(buildName, "y").cmp(buildName, "y", parentY)
        height.testNegative(buildName, "height")
        width.testNegative(buildName, "width")
        color.testNull(buildName, "color")
        return Rectangle(x!!, y!!, height!!, width!!, color!!)
    }
}

class CircleBuilder(var parentX: Int, var parentY: Int) : AbstractShapeBuilder() {
    override val className: String = "CircleBuilder"
    override var x: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "x")
        }
    override var y: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "y")
        }
    override var color: String? = null
        set(value: String?) {
            field = value.testNull(className, "color")
        }
    var radius: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "radius")
        }

    override fun build(): Circle {
        val buildName = "$className under build"
        x.testNegative(buildName, "x").cmp(buildName, "x", parentX)
        y.testNegative(buildName, "y").cmp(buildName, "y", parentY)
        radius.testNegative(buildName, "radius")
        color.testNull(buildName, "color")
        return Circle(x!!, y!!, radius!!, color!!)
    }
}

class SquareBuilder(private var parentX: Int, private var parentY: Int) : AbstractShapeBuilder() {
    override val className: String = "SquareBuilder"
    override var x: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "x")
        }
    override var y: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "y")
        }
    override var color: String? = null
        set(value: String?) {
            field = value.testNull(className, "color")
        }
    var width: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "width")
        }

    override fun build(): Square {
        val buildName = "$className under build"
        x.testNegative(buildName, "x").cmp(buildName, "x", parentX)
        y.testNegative(buildName, "y").cmp(buildName, "y", parentY)
        color.testNull(buildName, "color")
        width.testNegative(buildName, "width")
        return Square(x!!, y!!, width!!, color!!)
    }
}

fun generate(root: ShapeBuilder.() -> Unit): String {
    val b = ShapeBuilder()
    b.root()
    val root = b.build()
    return GsonBuilder().setPrettyPrinting().create().toJson(root)
}

fun createExamples() {
    val first = generate() {
        name ="Only Rectangles"
        x = 100
        y = 200
        color = "White"
        rectangle {
            x = 10
            y = 10
            height = 10
            width = 10
            color = "Yellow"
        }
        rectangle {
            x = 20
            y = 20
            height = 20
            width = 20
            color = "Red"
        }
        rectangle {
            x = 30
            y = 30
            height = 30
            width = 30
            color = "Blue"
        }
        rectangle {
            x = 40
            y = 40
            height = 40
            width = 40
            color = "Black"
        }
    }
    val second = generate() {
        name = "Only circles"
        x = 10
        y = 10
        color = "Blue"
        circle {
            x = 2
            y = 3
            radius = 5
            color = "Red"
        }
    }
    val third = generate() {
        name = "With nested root"
        x = 10
        y = 10
        color = "Blue"
        circle {
            x = 2
            y = 3
            radius = 5
            color = "Red"
        }
        root {
            name = "child root"
            x = 9
            y = 9
            color = "Blue"
            square {
                x = 8
                y = 5
                width = 3
                color = "Yellow"
            }
            root {
                name = "child of child root"
                x = 8
                y = 8
                color = "Blue"
                rectangle {
                    x = 3
                    y = 5
                    height = 43
                    width = 3
                    color = "Green"
                }
            }
        }
    }
    val json = generate() {
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
    println(first)
    println(second)
    println(third)
    println(json)
}

fun main() {
    createExamples()
}