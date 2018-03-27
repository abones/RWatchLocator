package com.rubius.rwatchlocator

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.snatik.polygon.Point
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : Activity() {
    val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locatorView.database = Database()
        locatorView.database!!.rooms = listOf(
            Room(
                "301",
                listOf(
                    Point(0.0, 7.0),
                    Point(4.0, 7.0),
                    Point(4.0, 14.0),
                    Point(0.0, 14.0)
                )
            ),
            Room(
                "302",
                listOf(
                    Point(0.0, 0.0),
                    Point(8.0, 0.0),
                    Point(8.0, 7.0),
                    Point(0.0, 7.0)
                )
            ),
            Room(
                "302a",
                listOf(
                    Point(8.0, 0.0),
                    Point(10.0, 0.0),
                    Point(10.0, 5.0),
                    Point(8.0, 5.0)
                )
            ),
            Room(
                "302b",
                listOf(
                    Point(8.0, 5.0),
                    Point(10.0, 5.0),
                    Point(10.0, 7.0),
                    Point(8.0, 7.0)
                )
            ),
            Room(
                "303",
                listOf(
                    Point(10.0, 0.0),
                    Point(18.0, 0.0),
                    Point(18.0, 7.0),
                    Point(10.0, 7.0)
                )
            ),
            Room(
                "304a",
                listOf(
                    Point(18.0, 0.0),
                    Point(22.0, 0.0),
                    Point(22.0, 7.0),
                    Point(18.0, 7.0)
                )
            ),
            Room(
                "304",
                listOf(
                    Point(22.0, 0.0),
                    Point(26.0, 0.0),
                    Point(26.0, 7.0),
                    Point(22.0, 7.0)
                )
            ),
            Room(
                "305",
                listOf(
                    Point(26.0, 0.0),
                    Point(30.0, 0.0),
                    Point(30.0, 7.0),
                    Point(26.0, 7.0)
                )
            ),
            Room(
                "306",
                listOf(
                    Point(30.0, 0.0),
                    Point(45.0, 0.0),
                    Point(45.0, 7.0),
                    Point(30.0, 7.0)
                )
            ),
            Room(
                "H2",
                listOf(
                    Point(10.0, 7.0),
                    Point(45.0, 7.0),
                    Point(45.0, 11.0),
                    Point(10.0, 11.0)
                )
            ),
            Room(
                "H2",
                listOf(
                    Point(10.0, 7.0),
                    Point(45.0, 7.0),
                    Point(45.0, 11.0),
                    Point(10.0, 11.0)
                )
            ),
            Room(
                "H1",
                listOf(
                    Point(4.0, 7.0),
                    Point(10.0, 7.0),
                    Point(10.0, 11.0),
                    Point(8.0, 11.0),
                    Point(8.0, 14.0),
                    Point(4.0, 14.0)
                )
            ),
            Room(
                "WC",
                listOf(
                    Point(8.0, 11.0),
                    Point(10.0, 11.0),
                    Point(10.0, 14.0),
                    Point(8.0, 14.0)
                )
            ),
            Room(
                "H3",
                listOf(
                    Point(-3.0, 14.0),
                    Point(10.0, 14.0),
                    Point(10.0, 18.0),
                    Point(-3.0, 18.0)
                )
            ),
            Room(
                "311",
                listOf(
                    Point(10.0, 11.0),
                    Point(14.0, 11.0),
                    Point(14.0, 18.0),
                    Point(10.0, 18.0)
                )
            ),
            Room(
                "310",
                listOf(
                    Point(14.0, 11.0),
                    Point(22.0, 11.0),
                    Point(22.0, 14.0),
                    Point(18.0, 14.0),
                    Point(18.0, 18.0),
                    Point(14.0, 18.0)
                )
            ),
            Room(
                "310a",
                listOf(
                    Point(18.0, 14.0),
                    Point(22.0, 14.0),
                    Point(22.0, 18.0),
                    Point(18.0, 18.0)
                )
            ),
            Room(
                "309",
                listOf(
                    Point(22.0, 11.0),
                    Point(34.0, 11.0),
                    Point(34.0, 18.0),
                    Point(22.0, 18.0)
                )
            ),
            Room(
                "308",
                listOf(
                    Point(34.0, 11.0),
                    Point(38.0, 11.0),
                    Point(38.0, 18.0),
                    Point(34.0, 18.0)
                )
            ),
            Room(
                "H4",
                listOf(
                    Point(38.0, 11.0),
                    Point(42.0, 11.0),
                    Point(42.0, 18.0),
                    Point(38.0, 18.0)
                )
            ),
            Room(
                "WC",
                listOf(
                    Point(42.0, 11.0),
                    Point(45.0, 11.0),
                    Point(45.0, 18.0),
                    Point(42.0, 18.0)
                )
            )
        )
        //locatorView.database!!.rooms = genRooms(10, 7)
        for (room in locatorView.database!!.rooms)
            Log.d("ROOM", "$room")

        printNode("r", locatorView.database!!.bspRoot, 0)

        buttonReset.setOnClickListener {
            locatorView.reset()
        }
        locatorView.listener = { scale, translationX, translationY ->
            label.text = "$scale, $translationX, $translationY"
        }
        locatorView.onPointAdded = { room, _, _ ->
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                // deprecated in API 26
                vibrator.vibrate(60)
            }

            val roomName = if (room == null) "Unknown" else room.name
            Toast.makeText(this, "Added to room $roomName", Toast.LENGTH_SHORT).show()
        }
        seekBar.setOnSeekBarChangeListener(SeekBarListener(true))
        seekBar2.setOnSeekBarChangeListener(SeekBarListener(false))
    }

    private fun genRooms(maxRooms: Int, maxVerticesPerRoom: Int): List<Room> {
        val result = arrayListOf<Room>()

        var roomCount = 0
        while (roomCount < maxRooms) {
            val vertices = arrayListOf<Point>()
            while (vertices.size < maxVerticesPerRoom) {
                val x = -10.0 + (10.0 - (-10.0)) * random.nextDouble()
                val y = -10.0 + (10.0 - (-10.0)) * random.nextDouble()
                vertices.add(Point(x, y))

                if (vertices.size > 3 && random.nextDouble() > 0.8)
                    break
            }
            result.add(Room("", vertices))

            ++roomCount
        }

        return result
    }

    private fun printNode(prefix: String, node: TreeNode?, level: Int) {
        if (node == null)
            return
        Log.d("TREE", "${prefix}" + "    ".repeat(level) + "${level} ${node.lines.size} ${node.convexLines.size}")
        printNode("f", node.front, level + 1)
        printNode("b", node.back, level + 1)
    }

    inner class SeekBarListener(private val isMin: Boolean) : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            if (isMin)
                locatorView.minLevel = p1
            else
                locatorView.maxLevel = p1
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }
    }
}
