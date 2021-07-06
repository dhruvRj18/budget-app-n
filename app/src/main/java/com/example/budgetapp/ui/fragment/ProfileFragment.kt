package com.example.budgetapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentProfileBinding
import com.example.budgetapp.entities.Profile
import com.example.budgetapp.ui.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.fragment_profile) {
    val profileViewModel:ProfileViewModel by viewModels()
    lateinit var filepath : Uri
    lateinit var bitmap:Bitmap
    lateinit var binding: FragmentProfileBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        activity?.title = "My Profile"


        binding.profileImage.setOnClickListener {
            openFileChooser()

        }
        binding.submitProfile.setOnClickListener {
            submitData(binding.profileName.text.toString(),
            bitmap,
            binding.profileEmail.text.toString(),
            binding.bankName.text.toString(),
            binding.currentBalance.text.toString(),
            binding.materialCheckBox.isChecked)
        }
    }

    private fun submitData(name: String, image: Bitmap, email: String, bankName: String, currentBalance: String, checked: Boolean) {
        profileViewModel.insertProfileData(Profile(name = name,email = email,profileImage = bitmap,bankName = bankName,
        currentBalance = currentBalance.toFloat(),primaryBank = checked))

    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.also {
            it.type = "image/*"
            startActivityForResult(Intent.createChooser(it,"Pick up a photo"),0)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data?.data != null){
            filepath = data?.data!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val source = ImageDecoder.createSource(context?.contentResolver!!,filepath)
                bitmap = ImageDecoder.decodeBitmap(source)
                bitmap.compress(Bitmap.CompressFormat.PNG,50,ByteArrayOutputStream())
               // Glide.with(this).load(bitmap).into(binding.profileImage)
                binding.profileImage.setImageBitmap(bitmap)
            }
        }
    }
}