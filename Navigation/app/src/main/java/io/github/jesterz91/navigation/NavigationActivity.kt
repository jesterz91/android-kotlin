package io.github.jesterz91.navigation

import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class NavigationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, AnkoLogger {

    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val navController = findNavController(R.id.my_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }
            toast("Navigated to $dest")
            info("Navigated to $dest")
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    private fun setupActionBar(navController: NavController, appBarConfig : AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        when(menu.itemId) {
            R.id.home_dest -> toast("home dest")
            R.id.deeplink_dest -> toast("deep dest")
            else -> toast("asd")
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
//        val retValue = super.onCreateOptionsMenu(menu)
//        if (isShowOverflowMenu) {
//
//        }
//        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found. Otherwise, bubble up to the parent.
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment)) || super.onOptionsItemSelected(item)
    }
}