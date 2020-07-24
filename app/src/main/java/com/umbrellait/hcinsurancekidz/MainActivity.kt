package com.umbrellait.hcinsurancekidz

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration.Builder(
            setOf(
//                R.id.characters_list_fragment,
//                R.id.locations_list_fragment,
                R.id.notAuthorizedProfileFragment,
                R.id.newslineHomeFragment
            )
        ).build()
    }

    private val navController: NavController by lazy { findNavController(R.id.fragmentNavigationHost) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tlbMain = findViewById<Toolbar>(R.id.toolbarMain)
        setSupportActionBar(tlbMain)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btmNavMain = findViewById<BottomNavigationView>(R.id.bottomNavView)

        bottomNavView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.notAuthorizedProfileFragment, R.id.newslineHomeFragment -> {
                    tlbMain.visibility = View.GONE
                    btmNavMain.visibility = View.VISIBLE
                }

                R.id.profileEnterPhoneNumberFragment, R.id.codeEnterProfileFragment, R.id.enterPersonalDataProfileFragment,
                R.id.registrationFinishedNoEmailProfileFragment, R.id.registrationFinishedWithEmailProfileFragment -> {
                    tlbMain.visibility = View.VISIBLE
                    tlbMain.title = ""
                    btmNavMain.visibility = View.GONE
                }

                R.id.nav_profile /*, R.id.locations_list_fragment, R.id.episodes_list_fragment*/ -> {
                    tlbMain.visibility = View.VISIBLE
                    tlbMain.title = "BBBBBBBBB"
                    btmNavMain.visibility = View.VISIBLE
                }
                else -> {
                    tlbMain.visibility = View.GONE
                    btmNavMain.visibility = View.GONE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}