package com.example.budgetapp.ui.fragment


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.budgetapp.InternalStoragePhoto
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentProfileBinding
import com.example.budgetapp.entities.Profile
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import com.example.budgetapp.ui.viewModels.ProfileViewModel
import com.example.budgetapp.util.Constants.PREFERENCE_DATE
import com.example.budgetapp.util.Constants.PREFERENCE_KEY
import com.example.budgetapp.util.Constants.PREFERENCE_NAME
import com.example.budgetapp.util.UtilityFunctions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    val profileViewModel: ProfileViewModel by viewModels()
    val budgetViewModel: BudgetViewModel by viewModels()
    lateinit var bitmap: Bitmap
    lateinit var filepath: Uri
    lateinit var binding: FragmentProfileBinding
    lateinit var myPref : SharedPreferences

    val takePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        filepath = result
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(requireContext().contentResolver!!, filepath)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
        val isSavedSuccessfully =
            saveImageToInternalStorage("profile", bitmap)
        if (isSavedSuccessfully) {
            Log.d("TAG", "Profile Fragment: Photo saved")
        } else {
            Log.d("TAG", "Profile Fragment:Failed to save photo")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        activity?.title = "My Profile"
        myPref = requireContext().getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        binding.updateCurrentBalance.setOnClickListener {
            if (!myPref.contains(PREFERENCE_KEY)){
                updateCurrentBalance()
            }else{
                Snackbar.make(binding.profileConstrain,"Balance Up to Date. Try again tomorrow.",Snackbar.LENGTH_SHORT).show()
            }
        }

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, object: androidx.lifecycle.Observer<List<Profile>>{
            override fun onChanged(profile: List<Profile>?) {
                if (profile!!.size >=1) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val listofImage = loadImageFromInternalStorage()
                        Log.d("imageList", "$listofImage")
                        for (i in listofImage) {
                            if (i.name.endsWith("jpg")) {
                                Glide.with(requireContext()).load(i.bitmap).circleCrop()
                                    .into(binding.profileImage)
                            }
                        }
                        binding.bankName.setText(profile!![0].bankName)
                        binding.currentBalance.setText(profile!![0].currentBalance.toString())
                        binding.materialCheckBox.isChecked = profile!![0].primaryBank
                        binding.profileName.setText(profile!![0].name)
                        binding.profileEmail.setText(profile!![0].email)
                    }
                } else {
                    Toast.makeText(requireContext(), "Complete PRofile", Toast.LENGTH_SHORT).show()
                }
                profileViewModel.profileLiveData.removeObserver(this)
            }

        })


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

    private fun updateCurrentBalance() {
        val editor = myPref.edit()
        editor.putString(PREFERENCE_KEY,"Updated")
        editor.putLong(PREFERENCE_DATE,Calendar.getInstance().timeInMillis)
        editor.apply()

        val yesterDayinString = UtilityFunctions.getEndDate(1)
        val yesterDay = UtilityFunctions.dateStringToMillis(yesterDayinString)

        budgetViewModel.yesterDaysBudget(yesterDay)
        budgetViewModel.yesterDaysBudget.observe(viewLifecycleOwner){yesterDayBudget ->
            var yesterdaySpending:Float = 0f
            var yesterdayCredit:Float = 0f
            yesterDayBudget?.let {
                for (i in it){
                    if (i.creditOrDebit.equals("Debit")){
                        yesterdaySpending = yesterdaySpending +  i.amount
                    }else if(i.creditOrDebit.equals("Credit")){
                        yesterdayCredit = yesterdayCredit + i.amount
                    }
                }
                val yesterDayGrossValue = yesterdayCredit + (-1*yesterdaySpending)
                Log.d("TAG", "getYesterDaysBudget: $yesterDayGrossValue \n $yesterdayCredit \n ${-1*yesterdaySpending}")
                uploadNewCurrentBalance(yesterDayGrossValue)
            }
        }
    }

    private fun uploadNewCurrentBalance(yesterDayGrossValue: Float) {
        Log.d("TAG", "yesterDay gross: $yesterDayGrossValue")
        profileViewModel.profileLiveData.observe(viewLifecycleOwner,object : androidx.lifecycle.Observer<List<Profile>>{
            override fun onChanged(t: List<Profile>?) {
                val currentBal = t!![0].currentBalance
                val newCurrentBal = currentBal + yesterDayGrossValue
                Log.d("TAG", "onChanged: newBal $newCurrentBal")
                profileViewModel.updateCurrentBalance(newCurrentBal)
                profileViewModel.profileLiveData.removeObserver(this)
                findNavController().navigate(R.id.action_global_profileFragment)
            }
        })
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
        findNavController().navigate(R.id.action_profileFragment_to_calenderViewFragment2)
    }

    private fun saveImageToInternalStorage(fileName: String, bitmap: Bitmap): Boolean {

        val directory = ContextWrapper(requireContext()).getDir("imageDir",Context.MODE_PRIVATE)

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