package org.dsl

import com.google.gson.GsonBuilder
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringWriter
import java.io.Writer
import java.security.InvalidParameterException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


sealed class Shape()

data class Root(val name: String, val width: Int, val height: Int, val shapes: List<Shape>) : Shape()
data class Rectangle(val x: Int, val y: Int, val height: Int, val width: Int, val color: String) : Shape()
data class Circle(val x: Int, val y: Int, val radius: Int, val color: String) : Shape()
data class Square(val x: Int, val y: Int, val width: Int, val color: String) : Shape()

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

@Throws(Exception::class)
fun prettyPrint(html: Document?) {
    val tf: Transformer = TransformerFactory.newInstance().newTransformer()
    val writer: Writer = StringWriter()

    tf.setOutputProperty(OutputKeys.METHOD, "xml");
    tf.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD XHTML 1.0 Transitional//EN");
    tf.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
    tf.setOutputProperty(OutputKeys.METHOD, "html");
    tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    tf.setOutputProperty(OutputKeys.INDENT, "yes");
    tf.transform(DOMSource(html), StreamResult(writer))

    println(writer)

}

fun modelToModel(root: ShapeBuilder.() -> Unit): Document {

    val dbf = DocumentBuilderFactory.newInstance()
    val builder = dbf.newDocumentBuilder()
    val doc = builder.newDocument()

    val b = ShapeBuilder()
    b.root()
    val root: Root = b.build()

    val html: Element = doc.createElement("html")
    doc.appendChild(html)
    val body: Element = doc.createElement("body")
    html.appendChild(body)

    val svgElement: Element = doc.createElement("svg")
    svgElement.setAttribute("width", root.width.toString())
    svgElement.setAttribute("height", root.height.toString())

    for(shape in root.shapes) {
        when (shape) {
            is Rectangle -> {
                val rectElement: Element = doc.createElement("rect")
                rectElement.setAttribute("width", shape.width.toString())
                rectElement.setAttribute("height", shape.height.toString())
                rectElement.setAttribute("x", shape.x.toString())
                rectElement.setAttribute("y", shape.y.toString())
                rectElement.setAttribute("fill", shape.color)
                svgElement.appendChild(rectElement)
            }
            is Circle -> {
                val circleElement: Element = doc.createElement("circle")
                circleElement.setAttribute("cx", shape.x.toString())
                circleElement.setAttribute("cy", shape.y.toString())
                circleElement.setAttribute("r", shape.radius.toString())
                circleElement.setAttribute("fill", shape.color)
                svgElement.appendChild(circleElement)
            }
            is Square -> {
                val squareElement: Element = doc.createElement("rect")
                squareElement.setAttribute("width", shape.width.toString())
                squareElement.setAttribute("height", shape.width.toString())
                squareElement.setAttribute("x", shape.x.toString())
                squareElement.setAttribute("y", shape.y.toString())
                squareElement.setAttribute("fill", shape.color)
                svgElement.appendChild(squareElement)
            }
            is Root -> println("Out of Scope")
        }
    }
    body.appendChild(svgElement)
    return doc
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
    prettyPrint(modelToModel(example1))

    // Example2
    prettyPrint(modelToModel(example2))

    // Example4
    prettyPrint(modelToModel(stickFigure))
}

fun main() {
    createExamples()
}
