package com.rubius.rwatchlocator

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.snatik.polygon.Point
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locatorView.database = Database()
        locatorView.database!!.rooms = listOf(
            Room(
                "301",
                listOf(
                    Point(0.0, 1.0),
                    Point(1.0, 1.0),
                    Point(1.0, 0.0),
                    Point(0.0, 0.0),
                    Point(1.0, 0.0),
                    Point(1.0, -1.0)
                    //Point(0.0, -1.0),
                    //Point(0.0, 0.0)
                )
            )
            /*Room(
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
            )
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
            )*/
        )

        printNode("r", locatorView.database!!.bspRoot, 0)

        buttonReset.setOnClickListener {
            locatorView.reset()
        }
        locatorView.listener = { scale, translationX, translationY ->
            label.text = "$scale, $translationX, $translationY"
        }
    }

    private fun printNode(prefix: String, node: TreeNode?, level: Int) {
        if (node == null)
            return
        Log.d("TREE", "${prefix}" + "    ".repeat(level) + "${level} ${node.lines.size}")
        printNode("f", node.front, level + 1)
        printNode("b", node.back, level + 1)
    }
}
