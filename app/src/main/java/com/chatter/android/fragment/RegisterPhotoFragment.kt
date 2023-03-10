package com.chatter.android.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.chatter.android.R
import com.chatter.android.activity.RegisterActivity
import java.io.ByteArrayOutputStream

@Suppress("NAME_SHADOWING", "DEPRECATION")
class RegisterPhotoFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var openCameraButton: Button
    private lateinit var openGalleryButton: Button

    private val photoRequestCode = 1234
    private val galleryRequestCode = 4321

    private lateinit var thiss: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_register_photo, container, false)
        thiss = view.context

        init(view)
        onLoad(view)

        return view
    }

    private fun init(view: View) {
        profileImage = view.findViewById(R.id.profileImage)
        openCameraButton = view.findViewById(R.id.openCameraButton)
        openGalleryButton = view.findViewById(R.id.openGalleryButton)
    }

    private fun onLoad(view: View) {
        openCameraButton.setOnClickListener { takePhoto(view) }
        openGalleryButton.setOnClickListener { openGallery() }

        if (!RegisterActivity.user.imagePath) {
            Glide.with(thiss)
                .load(RegisterActivity.user.image.extras?.get("data"))
                .circleCrop()
                .into(profileImage)
        } else {
            Glide.with(thiss)
                .load(RegisterActivity.user.image.data)
                .circleCrop()
                .into(profileImage)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto(view: View) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity((view.context as AppCompatActivity).packageManager) != null) {
            startActivityForResult(intent, photoRequestCode)
        }
    }

    private fun openGallery() {
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            ), galleryRequestCode
        )
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == photoRequestCode && resultCode == RESULT_OK) {
            Glide.with(thiss)
                .load(data?.extras?.get("data"))
                .circleCrop()
                .into(profileImage)

            RegisterActivity.user.image = data!!

            val inputStream =
                thiss.contentResolver.openInputStream(data.extras?.get("data") as Uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                byteArrayOutputStream
            )
            val data: ByteArray = byteArrayOutputStream.toByteArray()
            RegisterActivity.user.imageData = data
        } else if (requestCode == galleryRequestCode && resultCode == RESULT_OK) {
            Glide.with(thiss)
                .load(data?.data)
                .circleCrop()
                .into(profileImage)

            RegisterActivity.user.image = data!!
            RegisterActivity.user.imagePath = true


            val inputStream =
                thiss.contentResolver.openInputStream(data.data as Uri)
            RegisterActivity.user.imageData = inputStream?.readBytes()!!
        }
    }
}