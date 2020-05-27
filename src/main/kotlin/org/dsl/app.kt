@file:JvmName("Main")
package org.dsl

import java.security.InvalidParameterException
import de.cvguy.kotlin.koreander.Koreander
import java.io.File

sealed class Shape() {
    abstract val type: String
    abstract val color: String?
    abstract val x: Int?
    abstract val y: Int?
    abstract val height: Int?
    abstract val width: Int?
    abstract val radius: Int?
}

data class Root(val name: String, override val width: Int, override val height: Int, val shapes: List<Shape>) : Shape() {
    override val type: String ="Root"
    override val x: Int? = null
    override val y: Int? = null
    override val radius: Int? = null
    override val color: String? = null
}
data class Rectangle(override val x: Int, override val y: Int, override val height: Int, override val width: Int, override val color: String) : Shape() {
    override val type: String ="Rectangle"
    override val radius: Int? = null
}
data class Circle(override val x: Int, override val y: Int, override val radius: Int, override val color: String) : Shape() {
    override val type: String ="Circle"
    override val height: Int? = null
    override val width: Int? = null
}
data class Square(override val x: Int, override val y: Int, override val width: Int, override val color: String) : Shape() {
    override val type: String ="Square"
    override val height: Int? = null
    override val radius: Int? = null
}

abstract class AbstractShapeBuilder {
    abstract val className: String
    abstract var x: Int?
    abstract var y: Int?
    abstract fun build(): Shape
}

internal fun String?.testNull(className: String, text: String): String {
    if (this == null || this.isBlank()) throw IllegalArgumentException("""In $className: $text is not set""")
    if (this.length < 2) throw InvalidParameterException("""In $className: $this in $text is not allowed""")
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

fun isRectange(input: Any): Boolean {
    return input is Rectangle
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
    var name: String? = null
        set(value: String?) {
            field = value.testNull(className, "name")
        }

    private val shapes: MutableList<Shape> = ArrayList()

    override fun build(): Root {
        val buildName = "$className under build"
        val testedX = x.testNegative(buildName, "x")
        val testedY = y.testNegative(buildName, "y")
        val testedName = name.testNull(buildName, "name")
        val testedShapes = shapes.testEmpty(buildName, "shape")
        return Root(testedName,  testedX, testedY, testedShapes)
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
    var color: String? = null
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
        val testedX = x.testNegative(buildName, "x").cmp(buildName, "x", parentX)
        val testedY = y.testNegative(buildName, "y").cmp(buildName, "y", parentY)
        val testedHeight = height.testNegative(buildName, "height")
        val testedWidth = width.testNegative(buildName, "width")
        val testedColor = color.testNull(buildName, "color")
        return Rectangle(testedX, testedY, testedHeight, testedWidth, testedColor)
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
    var color: String? = null
        set(value: String?) {
            field = value.testNull(className, "color")
        }
    var radius: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "radius")
        }

    override fun build(): Circle {
        val buildName = "$className under build"
        val testedX = x.testNegative(buildName, "x").cmp(buildName, "x", parentX)
        val testedY = y.testNegative(buildName, "y").cmp(buildName, "y", parentY)
        val testedRadius = radius.testNegative(buildName, "radius")
        val testedColor = color.testNull(buildName, "color")
        return Circle(testedX, testedY, testedRadius, testedColor)
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
    var color: String? = null
        set(value: String?) {
            field = value.testNull(className, "color")
        }
    var width: Int? = null
        set(value: Int?) {
            field = value.testNegative(className, "width")
        }

    override fun build(): Square {
        val buildName = "$className under build"
        val testedX = x.testNegative(buildName, "x").cmp(buildName, "x", parentX)
        val testedY = y.testNegative(buildName, "y").cmp(buildName, "y", parentY)
        val testedWidth = width.testNegative(buildName, "width")
        val testedColor = color.testNull(buildName, "color")

        return Square(testedX, testedY, testedWidth, testedColor)
    }
}

fun modelToModel(root: ShapeBuilder.() -> Unit): Unit {

    val b = ShapeBuilder()
    b.root()
    val root: Root = b.build()

    val input = File("input.kor")
    val output = Koreander().render(input,root)
    println(output.toString())
}

fun createExamples() {
    val example1: ShapeBuilder.() -> Unit = {
        name ="Only Rectangles"
        x = 100
        y = 200
        rectangle {
            x = 10
            y = 10
            height = 100
            width = 100
            color = "Yellow"
        }
        rectangle {
            x = 20
            y = 20
            height = 200
            width = 200
            color = "Red"
        }
        rectangle {
            x = 30
            y = 30
            height = 300
            width = 300
            color = "Blue"
        }
        rectangle {
            x = 40
            y = 40
            height = 400
            width = 400
            color = "Black"
        }
    }

    val example2: ShapeBuilder.() -> Unit = {
        name = "Only circles"
        x = 10
        y = 10
        circle {
            x = 2
            y = 3
            radius = 5
            color = "Red"
        }
    }
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
    // Example1
    modelToModel(example1)

    // Example2
    modelToModel(example2)

    // Example4
    modelToModel(stickFigure)
}

fun main() {
    createExamples()
}
