import com.google.gson.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File
import kotlin.system.exitProcess

@Serializable data class Root(val name: String, val block: Array<Form>, val width: Int, val height: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Root

        if (name != other.name) return false
        if (!block.contentEquals(other.block)) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + block.contentHashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }
}

@Serializable data class Form(val form_name: String, val form_type: Shape)

@Serializable data class Shape(val x: Int?, val y: Int?, val width: Int?, val height: Int?, val color: String?, val radius: Int?)


fun main() {
    val json = Json(JsonConfiguration.Stable)

    var gson = Gson()
    val jsonString: String = File("./example_input.json").readText(Charsets.UTF_8)
    var testModel = gson.fromJson(jsonString, Root::class.java)
    testModel.block.forEach { form ->
        val type = form.form_type
        when (form.form_name) {
            "rectangle" -> validateRectangle(type)
            "circle" -> validateCircle(type)
            "square" -> validateSquare(type)
            else -> {
                println("Wrong Form name: Use 'rectangle', 'circle' or 'square'")
                exitProcess(1)
            }
        }
     }
    println(testModel.toString())
    println("Json is valid")
    exitProcess(0)
}

fun validateRectangle(shape: Shape) {
    if (shape.x == null || shape.y == null || shape.width == null || shape.height == null ||
            shape.radius != null || shape.color == null) {
        println("Wrong Form Type for Rectangle")
        exitProcess(1)
    }
}

fun validateCircle(shape: Shape) {
    if (shape.x == null || shape.y == null || shape.width != null || shape.height != null ||
            shape.radius == null || shape.color == null) {
        println("Wrong Form Type for Circle")
        exitProcess(1)
    }
}

fun validateSquare(shape: Shape) {
    if (shape.x == null || shape.y == null || (shape.width == null && shape.height == null) ||
            shape.radius != null || shape.color == null ) {
        println("Wrong Form Type for Square")
    }
    if(shape.width != null && shape.height != null && shape.width != shape.height) {
        println("width must be identically to height")
        exitProcess(1)
    }
}

