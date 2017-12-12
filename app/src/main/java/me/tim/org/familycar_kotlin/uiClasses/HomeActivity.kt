package me.tim.org.familycar_kotlin.uiClasses

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.content_home.*
import me.tim.org.familycar_kotlin.data.*
import java.util.*
import android.content.IntentFilter
import android.location.Location
import android.net.wifi.WifiManager
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.widget.EditText
import com.google.gson.Gson
import me.tim.org.familycar_kotlin.HttpManager
import me.tim.org.familycar_kotlin.R
import me.tim.org.familycar_kotlin.WifiReceiver
import me.tim.org.familycar_kotlin.toJson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var MY_PERMISSIONS_REQUEST_FINE_LOCATION: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(View.OnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        })

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        //Check if there is a driver connected to this phone, if not ask the user for his name
        checkDriver()

        setHandlers()

        checkPermissions()


        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(WifiReceiver(), intentFilter)


    }

    private fun setHandlers() {
        btnTest.setOnClickListener(View.OnClickListener {  testRestCall() })
    }

    private fun checkDriver() {
        val sharedPref = getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE)
        val driverString = sharedPref.getString(getString(R.string.pref_driver), "")

        println("DriverString: $driverString")

        if (driverString == "") {
            //No driver detected, prompt user for his name to create a Driver object.
            val builder = AlertDialog.Builder(this)
            builder.setTitle("What is your name")
            builder.setCancelable(false)

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->  createSharedPrefDriver(input.text.toString())})

            val dialog = builder.show()
            val button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            button.setOnClickListener {
                val text = input.text.toString()
                if (text != "") {
                    createSharedPrefDriver(text)
                    dialog.dismiss()
                } else {
                    toast("Invalid name")
                }
            }
        } else {
            //There is a known driver.
            toast(driverString)
        }
    }

    private fun createSharedPrefDriver(name: String) {
        println("Creating shared pref driver")
        val sharedPref = getSharedPreferences(getString(R.string.pref_file_key), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        doAsync {
            val driver = HttpManager.post("/driver/create?name=$name")

            editor.putString(getString(R.string.pref_driver), driver)
            editor.commit()
            println("Commited shared pref driver")
        }

    }

    private fun testRestCall() {
        doAsync {
            val responeString = HttpManager.run("/driver/1")
            val driver: Driver = Driver.fromJSON(responeString)

//            val responseString = http.post("/driver/create?name=Testy test")
//            val driver: Driver = Driver.fromJSON(responseString)

            //Create Ride.
            val obdData = ArrayList<DataPoint>()
            val ride = Ride(0, driver, obdData)
            val json = ride.toJson()
            println(json)
            val rideJson = HttpManager.post("/ride/", json)
            val newRide: Ride = Ride.fromJSON(rideJson)

            uiThread {
                Log.d("Request", driver.name)
                longToast(driver.name)
            }
        }
    }

    private fun testReadWrite() {
        val writer = DataWriter(applicationContext)
        val reader = DataReader(applicationContext)

        val driver = Driver(0,"testDriver")
        val obdData = ArrayList<DataPoint>()
        val ride = Ride(0, driver, obdData)

        writer.saveRide(ride)

        val decryptedRide = reader.readRides()
        println(decryptedRide)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.nav_driving -> {
                val intent = Intent(applicationContext, DriveActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_history -> {
                val intent = Intent(applicationContext, HistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    private fun checkPermissions() {
        val permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION)
        }
    }
}
