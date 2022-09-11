package com.goldmen.android.nasa_apod.ui

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.goldmen.android.nasa_apod.R
import com.goldmen.android.nasa_apod.databinding.ActivityMainBinding
import com.goldmen.android.nasa_apod.utils.modeCheck
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modeCheck(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)
        val binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
            setSupportActionBar(it.appBarMain.toolbar)
        }

        supportFragmentManager.findFragmentById(R.id.nav_host_content).also {
            navController = (it as NavHostFragment).navController
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.nav_detail) {
                    binding.appBarMain.toolbar.title = ""
                }
            }
        }

        AppBarConfiguration.Builder(setOf(R.id.nav_main)).build().also {
            setupActionBarWithNavController(this, navController, it)
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_settings -> {
                showThemeSettingDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun showThemeSettingDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.apply_theme_title)
            .setCancelable(false)
            .setItems(R.array.theme_mode_types) { dialog, position ->
                when (position) {
                    0 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    else -> dialog.dismiss()
                }
            }
            .create().apply { show() }
    }
}