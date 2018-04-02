package com.rubius.rwatchlocator

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import java.util.concurrent.atomic.AtomicReference

class MainActivity : Activity() {
    val random = Random()

    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locatorView.database = Database()
        locatorView.database!!.rooms = listOf(
            /*Room(
                "0.0",
                listOf(
                    Point(0.0, 0.0),
                    Point(1.0, 0.0),
                    Point(1.0, 1.0),
                    Point(0.0, 1.0)
                )
            ),
            Room(
                "0.1",
                listOf(
                    Point(1.0, 0.0),
                    Point(2.0, 0.0),
                    Point(2.0, 1.0),
                    Point(1.0, 1.0)
                )
            ),
            Room(
                "0.2",
                listOf(
                    Point(2.0, 0.0),
                    Point(3.0, 0.0),
                    Point(3.0, 1.0),
                    Point(2.0, 1.0)
                )
            ),
            Room(
                "0.3",
                listOf(
                    Point(3.0, 0.0),
                    Point(4.0, 0.0),
                    Point(4.0, 1.0),
                    Point(3.0, 1.0)
                )
            ),
            Room(
                "1.0",
                listOf(
                    Point(0.0, 1.0),
                    Point(1.0, 1.0),
                    Point(1.0, 2.0),
                    Point(0.0, 2.0)
                )
            ),
            Room(
                "1.1",
                listOf(
                    Point(1.0, 1.0),
                    Point(2.0, 1.0),
                    Point(2.0, 2.0),
                    Point(1.0, 2.0)
                )
            ),
            Room(
                "1.2",
                listOf(
                    Point(2.0, 1.0),
                    Point(3.0, 1.0),
                    Point(3.0, 2.0),
                    Point(2.0, 2.0)
                )
            ),
            Room(
                "1.3",
                listOf(
                    Point(3.0, 1.0),
                    Point(4.0, 1.0),
                    Point(4.0, 2.0),
                    Point(3.0, 2.0)
                )
            ),
            Room(
                "2.0",
                listOf(
                    Point(0.0, 2.0),
                    Point(1.0, 2.0),
                    Point(1.0, 3.0),
                    Point(0.0, 3.0)
                )
            ),
            Room(
                "2.1",
                listOf(
                    Point(1.0, 2.0),
                    Point(2.0, 2.0),
                    Point(2.0, 3.0),
                    Point(1.0, 3.0)
                )
            ),
            Room(
                "2.2",
                listOf(
                    Point(2.0, 2.0),
                    Point(3.0, 2.0),
                    Point(3.0, 3.0),
                    Point(2.0, 3.0)
                )
            ),
            Room(
                "2.3",
                listOf(
                    Point(3.0, 2.0),
                    Point(4.0, 2.0),
                    Point(4.0, 3.0),
                    Point(3.0, 3.0)
                )
            ),
            Room(
                "3.0",
                listOf(
                    Point(0.0, 3.0),
                    Point(1.0, 3.0),
                    Point(1.0, 4.0),
                    Point(0.0, 4.0)
                )
            ),
            Room(
                "3.1",
                listOf(
                    Point(1.0, 3.0),
                    Point(2.0, 3.0),
                    Point(2.0, 4.0),
                    Point(1.0, 4.0)
                )
            ),
            Room(
                "3.2",
                listOf(
                    Point(2.0, 3.0),
                    Point(3.0, 3.0),
                    Point(3.0, 4.0),
                    Point(2.0, 4.0)
                )
            ),
            Room(
                "3.3",
                listOf(
                    Point(3.0, 3.0),
                    Point(4.0, 3.0),
                    Point(4.0, 4.0),
                    Point(3.0, 4.0)
                )
            )*/
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

        printNode("r", locatorView.database!!.bspRoot, 0)

        buttonReset.setOnClickListener {
            locatorView.reset()
        }
        locatorView.onPointAdded = ::onPointAdded
        seekBar.setOnSeekBarChangeListener(SeekBarListener(true))
        seekBar2.setOnSeekBarChangeListener(SeekBarListener(false))

        buttonStartScanning.setOnClickListener {
            if (isScanning)
                stopScanning()
            else
                tryStartingScanning(true)
        }

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
    }

    private fun onPointAdded(room: Room?, x: Double, y: Double): RssiMeasurement? {
        val rssiMeasurement = lastRssiMeasurement.get()
        if (rssiMeasurement == null) {
            showToast("No RSSI measurements recorded")
            return null
        }

        if ((Date().time - rssiMeasurement.createdAt.time) > 120 * 1000) {
            lastRssiMeasurement.set(null)
            showToast("RSSI measurement is too old")
            return null
        }

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            // deprecated in API 26
            vibrator.vibrate(60)
        }

        val roomName = if (room == null) "Unknown" else room.name
        showToast("Added ${rssiMeasurement.devices.size} measurements to room $roomName")

        return rssiMeasurement
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
        Log.d("TREE", "${prefix}" + "    ".repeat(level) + "${level} ${node.lines.size} ${node.convexLines.size} ${node.frontRoom?.name} ${node.backRoom?.name}")
        printNode("f", node.front, level + 1)
        printNode("b", node.back, level + 1)
    }

    private val lastRssiMeasurement = AtomicReference<RssiMeasurement?>()

    private val myScanCallback = MyScanCallback()

    companion object {
        const val REQUEST_ENABLE_BLUETOOTH: Int = 0
        const val REQUEST_ALLOW_LOCATION: Int = 1
    }

    private fun tryStartingScanning(shouldRequest: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return

        val bluetoothAdapter = bluetoothAdapter
        if (bluetoothAdapter == null) {
            showToast("This device does not have a Bluetooth adapter")
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            if (shouldRequest)
                startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH)
            return
        }

        checkPermissionsAndStartScanning(true)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun hasLocationPermission(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private var isScanning: Boolean = false
        set(value) {
            field = value

            if (value)
                buttonStartScanning.text = "Stop"
            else
                buttonStartScanning.text = "Start"
        }

    private fun checkPermissionsAndStartScanning(shouldRequest: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return

        if (!hasLocationPermission()) {
            if (shouldRequest)
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ALLOW_LOCATION)
            return
        }

        isScanning = true

        val scanSettings = ScanSettings.Builder()
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(10000)
            .build()
        bluetoothLeScanner?.startScan(listOf<ScanFilter>(), scanSettings, myScanCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            REQUEST_ENABLE_BLUETOOTH -> tryStartingScanning(false)
            REQUEST_ALLOW_LOCATION -> checkPermissionsAndStartScanning(false)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        stopScanning()
        super.onStop()
    }

    private fun stopScanning() {
        bluetoothLeScanner?.stopScan(myScanCallback)
        isScanning = false
    }

    inner class MyScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d("TAGG", "Got single $result")
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.d("TAGG", "Got batch $results")
            blink()
            if (results != null) {
                val measurement = RssiMeasurement(Date(), results.map { it.device.address to it.rssi }.toMap())
                lastRssiMeasurement.set(measurement)
            }
            super.onBatchScanResults(results)
        }

        override fun onScanFailed(errorCode: Int) {
            lastRssiMeasurement.set(null)
            super.onScanFailed(errorCode)
        }
    }

    private fun blink() {
        imageView.alpha = 1.0f
        imageView.animate().alpha(0.0f).setDuration(800L).start()
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
