package com.example.myapplication.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.example.myapplication.Constance
import com.example.myapplication.GetUserDataActivity
import com.example.myapplication.classes.UserData
import com.example.myapplication.databinding.FragmentGeninfoBinding
import com.squareup.picasso.Picasso
import java.net.URI

class GeninfoFragment : Fragment() {

    lateinit var binding: FragmentGeninfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeninfoBinding.inflate(layoutInflater)
        binding.apply {
            textDelivery.movementMethod = LinkMovementMethod.getInstance()
            textPhoto.movementMethod = LinkMovementMethod.getInstance()
            textSupplier.movementMethod = LinkMovementMethod.getInstance()
            textTender.movementMethod = LinkMovementMethod.getInstance()
            textContacts.movementMethod = LinkMovementMethod.getInstance()
            val userName = Constance.SNP + UserData.name
            textSNP.text = userName
            val phoneNum = Constance.phone + UserData.phoneNumber
            textPhone.text = phoneNum
            butReset.setOnClickListener{ onClickReset() }
        }
        return binding.root
    }

    private fun onClickReset() {
        val intent = Intent(this.context, GetUserDataActivity::class.java)
        this.startActivity(intent)
    }

}