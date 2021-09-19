package com.example.budgetapp.ui.fragment


import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
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
import com.example.budgetapp.util.Constants.PREFERENCE_NAME
import com.example.budgetapp.util.Constants.PREFERENCE_PROFILE_EXISTANCE_KEY
import com.example.budgetapp.util.UtilityFunctions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val profileViewModel: ProfileViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()
    private lateinit var bitmap: Bitmap
    private lateinit var filepath: Uri
    private lateinit var binding: FragmentProfileBinding
    private lateinit var myPref : SharedPreferences

    private val takePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
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
        if (myPref.contains(PREFERENCE_PROFILE_EXISTANCE_KEY) ){
            changeViewVisibilityPostRegistration()
        }else{
            changeViewVisibilityForRegistration()
        }
        binding.updateCurrentBalance.setOnClickListener {
            updateCurrentBalance()
        }

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, object: androidx.lifecycle.Observer<List<Profile>>{
            override fun onChanged(profile: List<Profile>?) {
                if (profile!!.size >=1) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val listofImage = loadImageFromInternalStorage()
                        Log.d("imageList", "$listofImage")
                        for (i in listofImage) {
                            if (i.name.contains("profile")) {
                                Glide.with(requireContext()).load(i.bitmap).circleCrop()
                                    .into(binding.profileImage)
                            }
                        }
                        binding.bankName.setText(profile!![0].bankName)
                        binding.initialBalance.setText(profile!![0].initialBalance.toString())
                        if (profile!![0].currentBalance == 0f){
                            binding.currentBalance.setText(profile!![0].initialBalance.toString())
                        }else{
                            binding.currentBalance.setText(profile!![0].currentBalance.toString())
                        }
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


        binding.profileImage.setOnClickListener{

            if (myPref.contains(PREFERENCE_PROFILE_EXISTANCE_KEY)){
                Snackbar.make(binding.profileConstrain,"You can not changer profile picture",Snackbar.LENGTH_SHORT).show()
            }else{
                takePhoto.launch("image/*")
            }

        }

        binding.submitProfile.setOnClickListener {
            submitData(
                binding.profileName.text.toString(),
                binding.profileEmail.text.toString(),
                binding.bankName.text.toString(),
                binding.initialBalance.text.toString(),
                binding.materialCheckBox.isChecked
            )
        }
    }

    private fun changeViewVisibilityForRegistration() {
        binding.submitProfile.visibility = View.VISIBLE
        binding.updateCurrentBalance.visibility = View.GONE
        binding.balanceLayout.visibility = View.GONE
    }

    private fun changeViewVisibilityPostRegistration() {
        binding.submitProfile.visibility = GONE
        binding.updateCurrentBalance.visibility = VISIBLE
        binding.balanceLayout.visibility = VISIBLE

    }

    private fun updateCurrentBalance() {
        budgetViewModel.sumOfBudget.observe(viewLifecycleOwner){sum->
            profileViewModel.profileLiveData.observe(viewLifecycleOwner){
                var newCurBal : Float
                val initBal = it!![0].initialBalance
                if(sum != null) {
                    newCurBal = initBal + sum
                }else{
                    newCurBal = initBal
                }
                Log.d("TAG", "updateCurrentBalance:new bal:  $newCurBal \n sum : $sum \n init $initBal")
                profileViewModel.updateCurrentBalance(newCurBal)
            }
        }
        findNavController().navigate(R.id.action_global_profileFragment)

    }

    private fun submitData(
        name: String,
        email: String,
        bankName: String,
        initialBalance : String,
        checked: Boolean
    ) {
        profileViewModel.insertProfileData(
            Profile(
                name = name,
                email = email,
                profileImageFilePath = filepath.toString(),
                bankName = bankName,
                currentBalance = 0f,
                initialBalance =  initialBalance.toFloat(),
                primaryBank = checked
            )

        )
        val editor = myPref.edit()
        editor.putBoolean(PREFERENCE_PROFILE_EXISTANCE_KEY,true)
        editor.apply()
        findNavController().navigate(R.id.action_profileFragment_to_calenderViewFragment2)
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