package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityEquipmentPageBinding
import com.example.myapplication.fragments.DescriptionFragment
import com.example.myapplication.fragments.EquipmentSpecFragment
import com.example.myapplication.fragments.SpecificationsFragment

class EquipmentPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityEquipmentPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquipmentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val curTitle = intent.getStringExtra("eqName")
        supportActionBar?.title = curTitle
        val imageURL = intent.getStringArrayListExtra("eqImURL")!!
        val fullDesc = intent.getStringExtra("eqFullDesc")
        launchFragment(DescriptionFragment(imageURL, fullDesc))
        val priceList = intent.getIntegerArrayListExtra("eqPrice")!!
        binding.valueAOP.text = priceList[0].toString()
        binding.valueARP.text = priceList[1].toString()
        val specifications = intent.getStringExtra("eqSpec")!!
        binding.apply {
            navMenuSimp.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.description -> launchFragment(DescriptionFragment(imageURL, fullDesc))
                    R.id.specifications -> launchFragment(EquipmentSpecFragment(specifications))
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