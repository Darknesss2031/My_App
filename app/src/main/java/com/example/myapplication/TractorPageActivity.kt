package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityTractorPageBinding
import com.example.myapplication.fragments.DescriptionFragment
import com.example.myapplication.fragments.SpecificationsFragment

class TractorPageActivity : AppCompatActivity() {

    lateinit var binding: ActivityTractorPageBinding
    private var specList = ArrayList<ArrayList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTractorPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val curTitle = intent.getStringExtra("trCorpName") + " " + intent.getStringExtra("trMod")
        supportActionBar?.title = curTitle
        val imageURL = intent.getStringArrayListExtra("trImURL")!!
        val fullDesc = intent.getStringExtra("trFullDesc")
        for (i in 0 until Constance.specSize.size) {
            val cur = intent.getStringArrayListExtra("S$i")
            Log.d("MyLog", cur.toString())
            specList.add(cur!!)
        }
        launchFragment(DescriptionFragment(imageURL, fullDesc))
        binding.apply {
            navMenuSimp.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.description -> launchFragment(DescriptionFragment(imageURL, fullDesc))
                    R.id.specifications -> launchFragment(SpecificationsFragment(specList))
                }
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finishActivity(RESULT_CANCELED)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchFragment(frag : Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.descFrag, frag).commit()
    }

}

