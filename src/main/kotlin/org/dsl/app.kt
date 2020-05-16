package org.dsl

import com.google.gson.GsonBuilder

sealed class Shape(xPos: Int, yPos: Int, color: String)

data class Root(val name: String, val shapes: List<Shape>, val width: Int, val height: Int, var color: String) : Shape(width, height, color)
data class Rectangle(val xPos: Int, val yPos: Int, val height: Int, val width: Int, val color: String) : Shape(xPos, yPos, color)
data class Circle(val xPos: Int, val yPos: Int, val radius: Int, val color: String) : Shape(xPos, yPos, color)
data class Square(val xPos: Int, val yPos: Int, val width: Int, val color: String) : Shape(xPos, yPos, color)

abstract class AbstractShapeBuilder {
    abstract val className: String
    abstract var x: Int?
    abstract var y: Int?
    abstract var color: String?
    abstract fun build(): Shape
}

internal fun String?.testNull(className: String, text: String): String {
    if (this == null) throw IllegalArgumentException("""In $className: $text is not set""")
    return this
}

internal fun Int?.testNegative(className: String, text: String): Int {
    if (this == null) throw IllegalArgumentException("""In $className: $text is not set""")
    if (this <= 0) throw IllegalArgumentException("""In $className: $text should be greater 0""")
    return this
}

internal fun Int.cmp(className: String, text: String, parentSize: Int): Int {
    if (this >= parentSize) throw IllegalArgumentException("""In $className: $text must be lower than $parentSize""")
    return this
}

class ShapeBuilder() : AbstractShapeBuilder() {
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
        if (shapes.size < 1) throw IllegalArgumentException("""In $buildName: none shapes are set""")
        x.testNegative(buildName, "x")
        y.testNegative(buildName, "y")
        color.testNull(buildName, "color")
        name.testNull(buildName, "name")
        return Root(name!!, shapes, x!!, y!!, color!!)
    }

    fun rectangle(block: RectangleBuilder.() -> Unit) {
        x.testNegative(className, "x")
        y.testNegative(className, "y")
        val r = RectangleBuilder(x!!, y!!)
        r.block()
        shapes.add(r.build())
    }

    fun circle(block: CircleBuilder.() -> Unit) {
        x.testNegative(className, "x")
        y.testNegative(className, "y")
        val c = CircleBuilder(x!!, y!!)
        c.block()
        shapes.add(c.build())
    }

    fun square(block: SquareBuilder.() -> Unit) {
        x.testNegative(className, "x")
        y.testNegative(className, "y")
        val s = SquareBuilder(x!!, y!!)
        s.block()
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

fun buildShape(block: ShapeBuilder.() -> Unit): String {
    val b = ShapeBuilder()
    b.block()
    val root = b.build()
    return GsonBuilder().setPrettyPrinting().create().toJson(root)
}

fun main() {
    val json = buildShape() {
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
    print(json)
}