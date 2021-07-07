package com.example.budgetapp.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.budgetapp.InternalStoragePhoto
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentProfileBinding
import com.example.budgetapp.entities.Profile
import com.example.budgetapp.ui.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    val profileViewModel: ProfileViewModel by viewModels()
    lateinit var bitmap: Bitmap
    lateinit var filepath: Uri
    lateinit var binding: FragmentProfileBinding
    val takePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        filepath = result
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(requireContext().contentResolver!!, filepath)
            bitmap = ImageDecoder.decodeBitmap(source)
        } 
        val isSavedSuccessfully =
            saveImageToInternalStorage("${filepath.lastPathSegment}.jpg", bitmap)
        if (isSavedSuccessfully) {
            Toast.makeText(requireContext(), "Photo saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to save photo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        activity?.title = "My Profile"
        profileViewModel.getProfile()
        profileViewModel.profileLiveData.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val listofImage = loadImageFromInternalStorage()
                    Log.d("imageList", "$listofImage")
                    for (i in listofImage) {
                        if (i.name.endsWith("jpg")) {
                            Glide.with(requireContext()).load(i.bitmap).circleCrop()
                                .into(binding.profileImage)
                        }
                    }
                    binding.bankName.setText(profile.bankName)
                    binding.currentBalance.setText(profile.currentBalance.toString())
                    binding.materialCheckBox.isChecked = profile.primaryBank
                }
            } else {
                Toast.makeText(requireContext(), "Complete PRofile", Toast.LENGTH_SHORT).show()
            }
        }


        binding.profileImage.setOnClickListener {

            takePhoto.launch("image/*")
        }

        binding.submitProfile.setOnClickListener {

            submitData(
                binding.profileName.text.toString(),
                binding.profileEmail.text.toString(),
                binding.bankName.text.toString(),
                binding.currentBalance.text.toString(),
                binding.materialCheckBox.isChecked
            )
        }
    }


    private fun submitData(
        name: String,
        email: String,
        bankName: String,
        currentBalance: String,
        checked: Boolean
    ) {
        profileViewModel.insertProfileData(
            Profile(
                name = name,
                email = email,
                profileImageFilePath = filepath.toString(),
                bankName = bankName,
                currentBalance = currentBalance.toFloat(),
                primaryBank = checked
            )
        )

    }

    private fun saveImageToInternalStorage(fileName: String, bitmap: Bitmap): Boolean {
        return try {
            requireContext().openFileOutput("$fileName.jpg", MODE_PRIVATE).use { outputStream ->
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun loadImageFromInternalStorage(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = requireContext().filesDir.listFiles()
            files.filter {
                it.canRead() && it.isFile && it.name.endsWith(".jpg")
            }.map {
                val bytes = it.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bitmap)
            } ?: listOf()
        }
    }
}