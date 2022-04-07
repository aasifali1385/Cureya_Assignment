package com.aa.cureya.assignment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aa.cureya.assignment.Fragments.NewsFragment
import com.aa.cureya.assignment.Fragments.ProfileFragment
import com.aa.cureya.assignment.Fragments.UsersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class NavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)


        val profile = ProfileFragment()
        val users = UsersFragment()
        val news = NewsFragment()


        setFragment(profile)

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    setFragment(profile)
//                    window.setStatusBarColor(resources.getColor(R.color.accountButtonBack))
                }
                R.id.users -> {
                    setFragment(users)
//                    window.setStatusBarColor(resources.getColor(R.color.accountButtonBack))
                }
                R.id.news -> {
                    setFragment(news)
//                    window.setStatusBarColor(resources.getColor(R.color.accountButtonBack))
                }

            }
            true
        }

    }

    private fun setFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            Log.e("tab", "logout......")
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@NavActivity, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}