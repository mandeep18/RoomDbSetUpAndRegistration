package com.kickhead.registrationwithdb.screens.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.kickhead.registrationwithdb.BuildConfig
import com.kickhead.registrationwithdb.R
import com.kickhead.registrationwithdb.database.AppDatabase
import com.kickhead.registrationwithdb.database.AppExecutors
import com.kickhead.registrationwithdb.model.TabModel
import com.kickhead.registrationwithdb.model.UserModel
import com.kickhead.registrationwithdb.utils.PlatFormUtils
import kotlinx.android.synthetic.main.fragment_registration.*
import java.io.File
import java.util.*


class RegistrationFragment:Fragment() {

    private var totalMarks: Int = 0
    private val CAMERA_REQ: Int = 100
    private val SIGNATURE_REQUEST_CODE = 200
    private val RECORD_REQUEST_CODE = 300
    private var outputFileUri: Uri? = null
    private var signature_path: String? = null
    private var mAppDatabase: AppDatabase? = null
    private var gender: String? = null
    private var interest: String? = null


    companion object {
        var TAG: String = RegistrationFragment::class.java.simpleName

        fun newInstance(tabModel: TabModel): RegistrationFragment {
            val fragment = RegistrationFragment()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_registration, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initViews()
        initAppDatabase()
        initSpinner()
        retrieveTasks()
    }

    private fun initViews(){
        //val userIdCount = SharedPreferencesHelper.getInt(SharedPrefConstant.USER_COUNT,0001)
        //tvUserId?.text =  "$userIdCount"
    }

    private fun setListeners() {
        tvDOB?.setOnClickListener(onClickListener)
        clAddSelfie?.setOnClickListener(onClickListener)
        btnSubmit?.setOnClickListener(onClickListener)
        tvSignature?.setOnClickListener(onClickListener)
        rbGender?.setOnCheckedChangeListener(onRadioCheckedChangeListener)
        cbMaths.setOnCheckedChangeListener(onCheckedChangeListener)
        cbEnglish.setOnCheckedChangeListener(onCheckedChangeListener)
        cbScience.setOnCheckedChangeListener(onCheckedChangeListener)

    }

    private fun initAppDatabase() {
        mAppDatabase = AppDatabase.getInstance(requireActivity())
    }

    private fun initSpinner() {
        spInterest?.apply {
            prompt = getString(R.string.select_interest)
            onItemSelectedListener = _onItemSelectedListener
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, getInterestList())
        }
    }

    private val _onItemSelectedListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                interest = parent?.getItemAtPosition(position) as String?
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    private val onCheckedChangeListener: CompoundButton.OnCheckedChangeListener =
        object : CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    totalMarks += 1000
                } else {
                    totalMarks -= 1000
                }
                tvTotalMarks?.text = "$totalMarks"
            }
        }

    private val onRadioCheckedChangeListener: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { p0, p1 ->
            when(p1){
                R.id.rbMale -> gender = getString(R.string.male)
                R.id.rbFemale -> gender = getString(R.string.female)
            }
        }

    private val onClickListener:View.OnClickListener = View.OnClickListener {
        when(it.id){
            R.id.tvDOB -> showDatePicker()
            R.id.clAddSelfie -> openCamera()
            R.id.tvSignature -> openImagePicker()
            R.id.btnSubmit -> submitDataIntoDB()
        }
    }


    private fun showDatePicker(){
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, monthOfYear, dayOfMonth ->
                tvDOB?.text = "$year-${monthOfYear + 1}-$dayOfMonth"
                tvAge?.text = "${mYear - year}"
            }, mYear, mMonth, mDay
        )
        datePickerDialog.apply {
            show()
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.purple_500))
            getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.purple_500))
        }
    }

    private fun openCamera(){
        if(!setupCameraPermissions()){
            return
        }
        try {
            val mFile = File(activity?.externalCacheDir?.absolutePath + "/" + System.currentTimeMillis() + ".jpg")
            mFile.createNewFile()

            outputFileUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".fileprovider", mFile)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(cameraIntent.resolveActivity(requireContext().packageManager)!=null && outputFileUri!=null){
                cameraIntent.apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    putExtra("android.intent.extras.CAMERA_FACING", 1)
                }
                startActivityForResult(cameraIntent, CAMERA_REQ)
            }

        }catch (e: Exception){
            e.printStackTrace()
        }


    }

    private fun openImagePicker(){
        if(!setupCameraPermissions()){
            return
        }
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SIGNATURE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        Log.e(TAG, "onActivityResult -${requestCode} , $resultCode")
        try {
            if (requestCode == SIGNATURE_REQUEST_CODE && resultCode == RESULT_OK) {
                signature_path = intent?.data?.path
                ivSignature?.setImageBitmap(MediaStore.Images.Media.getBitmap(context?.contentResolver, intent?.data))
            }else if (requestCode == CAMERA_REQ && resultCode == RESULT_OK) {
                Toast.makeText(context,"Captured by Camera", Toast.LENGTH_SHORT).show()
                Log.d(TAG,"outputFileUri-$outputFileUri , ${intent?.data}")
                if(outputFileUri!=null)
                    ivSelfie?.setImageBitmap(MediaStore.Images.Media.getBitmap(context?.contentResolver, outputFileUri))
            }else{
                super.onActivityResult(requestCode, resultCode, intent)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    private fun getInterestList():MutableList<String>{
        return arrayListOf<String>().apply {
            add(getString(R.string.select_interest))
            add("Cricket")
            add("Football")
            add("Reading")
            add("Hocky")
        }
    }

    private fun submitDataIntoDB(){
        if(etName?.text.toString().isNullOrEmpty()){
            Toast.makeText(context, "Please Enter User Name.", Toast.LENGTH_SHORT).show()
            etName?.requestFocus()
            return
        }
        if(etPhone?.text.toString().isNullOrEmpty()){
            Toast.makeText(context, "Please Enter Phone Number.", Toast.LENGTH_SHORT).show()
            etPhone?.requestFocus()
            return
        }

        Log.e(TAG,"werfgf-${PlatFormUtils.isValidEmail(etEmail.text.toString())}")
        if(!PlatFormUtils.isValidEmail(etEmail.text.toString())){
            Toast.makeText(context, "Please enter valid Email.", Toast.LENGTH_SHORT).show()
            return
        }
        if(tvAge?.text==getString(R.string.example)){
            Toast.makeText(context, "Please Add DOB.", Toast.LENGTH_SHORT).show()
            return
        }

        if(gender==null){
            Toast.makeText(context, "Please Select Gender.", Toast.LENGTH_SHORT).show()
            return
        }
        if(interest==null){
            Toast.makeText(context, "Please Select Interest.", Toast.LENGTH_SHORT).show()
            return
        }
        AppExecutors.instance.diskIO().execute(Runnable {
            mAppDatabase?.usersDao()?.insertChatSessionUser(
                chatSessionUserModel = UserModel(
                    name = etName.text.toString(),
                    email = etEmail.text.toString(),
                    phone = etPhone.text.toString(),
                    date_of_birth = tvDOB?.text.toString(),
                    gender = gender!!,
                    age = tvAge.text.toString(),
                    math = if(cbMaths.isChecked){ getString(R.string.thousand)}else{ "0" },
                    english = if(cbEnglish.isChecked){ getString(R.string.thousand)}else{ "0" },
                    science = if(cbScience.isChecked){ getString(R.string.thousand)}else{ "0" },
                    selfie_path = "${outputFileUri?.path}",
                    signature_path = "$signature_path",
                    interest = interest!!,
                    notes = etNotes.text.toString()
                ))
        })
        clearView()
        Toast.makeText(context, "User Registered SuccessFully", Toast.LENGTH_SHORT).show()
    }
    private fun clearView(){
        etName?.text = null
        etPhone?.text = null
        etEmail?.text = null
        tvDOB?.text = getString(R.string.select_dob)
        rbGender?.clearCheck()
        tvAge?.text = getText(R.string.example)
        cbMaths?.isChecked = false
        cbEnglish?.isChecked = false
        cbScience?.isChecked = false
        spInterest?.setSelection(0)
        etNotes?.text = null
        ivSelfie?.setImageDrawable(context?.getDrawable(R.drawable.ic_attach_file))
        tvTotalMarks?.text = "0"
        gender = null
        interest = null

    }
    private fun setupCameraPermissions() :Boolean{
        val permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), RECORD_REQUEST_CODE)
            return false
        }
        return true
    }

    private fun retrieveTasks(){
        mAppDatabase?.usersDao()?.loadAllChatSessionUsers()?.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()){
                tvUserId.text = String.format("%04d",(it[it.size-1].id+1))
            }else{
                tvUserId.text = String.format("%04d",1)
            }

        })
    }

}