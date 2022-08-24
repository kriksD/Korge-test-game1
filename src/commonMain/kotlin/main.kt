import com.soywiz.korev.*
import com.soywiz.korge.*
import com.soywiz.korge.box2d.*
import com.soywiz.korge.input.*
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Circle
import com.soywiz.korgw.*
import com.soywiz.korim.color.*
import com.soywiz.korma.geom.*
import org.jbox2d.dynamics.*
import kotlin.random.*
import kotlin.collections.*

suspend fun main() = Korge(
    quality = GameWindow.Quality.PERFORMANCE,
    width = 800,
    height = 1000,
    virtualWidth = 800,
    virtualHeight = 1000,
    bgcolor = Colors.DARKGRAY
) {
    var pause = false

    solidRect(800, 1000, Colors["#113648"])

    line(Point(0, 950), Point(800, 950), Colors.RED)
    val ground = solidRect(300, 100, Colors.BLACK).position(100, 900).registerBodyWithFixture(
        type = BodyType.STATIC,
        friction = 0.2
    )
    ground.centerXOnStage()
    ground.alignBottomToBottomOf(this)

    val text1 = text("0", textSize = 36.0, Colors.BLACK).position(12, 12)
    val text2 = text("0", textSize = 36.0, Colors.WHITE).position(10, 10)

    val objects = mutableListOf<View>()

    var randomView = randomView()
    this.addChild(randomView)

    onMove {
        randomView.position(it.currentPosLocal)
    }

    onClick {
        if (!pause) {
            randomView.alpha = 1.0
            randomView.registerBodyWithFixture(
                type = BodyType.DYNAMIC,
            )

            objects.add(randomView)
            text1.text = objects.size.toString()
            text2.text = objects.size.toString()

            randomView = randomView()
            stage.addChild(randomView)
            randomView.position(it.currentPosLocal)
            randomView.alpha = 0.7
        }
    }

    val mainText = text("", 72.0, Colors.YELLOW)
    mainText.centerXOnStage()

    val secondText = text("", 40.0, Colors.YELLOW)
    secondText.centerXOnStage()
    secondText.alignTopToBottomOf(mainText)

    addUpdater {
        if (objects.find { it.pos.y >= 950 } != null) {
            mainText.text = "GAME OVER"
            secondText.text = "Press \"Enter\" to restart"
            pause = true
        }

        if (input.keys.justReleased(Key.ENTER)) {
            pause = false

            objects.forEach {
                it.removeFromParent()
            }
            objects.clear()

            text1.text = objects.size.toString()
            text2.text = objects.size.toString()

            mainText.text = ""
            secondText.text = ""
        }
    }
}

fun randomView(): View {
    return when (Random.nextInt(4)) {
        0, 1, 2 -> SolidRect(randomSize(), randomSize(), randomColor()).rotation(randomAngle())
        3 -> Circle(randomRadius(), randomColor()).anchor(0.5, 0.5)
        else ->SolidRect(randomSize(), randomSize(), randomColor()).rotation(randomAngle())
    }
}

fun randomAngle(): Angle = Random.nextInt(0, 90).degrees
fun randomSize(): Int = Random.nextInt(30, 200)
fun randomRadius(): Double = Random.nextInt(30, 70).toDouble()
fun randomRound(): Double = Random.nextInt(10, 30).toDouble()
fun randomColor(): RGBA = RGBA(Random.nextInt(100, 150), Random.nextInt(100, 150), Random.nextInt(100, 150))
