package com.best.now.nine.ui

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.net.toFile
import com.best.now.nine.BaseVMActivity
import com.best.now.nine.R
import com.best.now.nine.databinding.ActivityImageBinding
import com.best.now.nine.ext.timeFormatter2
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

/**
author:zhoujingjin
date:2023/9/17
 */
class ImageActivity:BaseVMActivity() {
    private val binding by binding<ActivityImageBinding>(R.layout.activity_image)
    private var type = 80
    private var showSelect:Boolean = false
    override fun initView() {
        showSelect = intent.getBooleanExtra("showSelect",false)
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener { finish() }
            addSave.setOnClickListener {
                if (!showSelect){
                   startActivity(Intent(this@ImageActivity,ImageActivity::class.java).putExtra("showSelect",true))
                }else{
                    selectedImageBitmap?.let {
                        saveCompressedImage(it,type)
                    }
                }
            }
            high.isSelected = type==80
            high.setOnClickListener {
                type = 80
                high.isSelected = true
                medium.isSelected = false
                low.isSelected = false
                textChange()

            }
            medium.setOnClickListener {
                type = 50
                high.isSelected = false
                medium.isSelected = true
                low.isSelected = false
                textChange()
            }
            low.setOnClickListener {
                type = 30
                high.isSelected = false
                medium.isSelected = false
                low.isSelected = true
                textChange()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==2){
            if(resultCode== RESULT_OK){
                val selectedImageUri = data?.data
                setImage(selectedImageUri)
            }else{
                finish()
            }
        }
    }

    private var selectedImageBitmap:Bitmap?=null
    private fun setImage(data: Uri?) {
        binding.llChoose.visibility = View.VISIBLE
        binding.tvTips.visibility = View.VISIBLE
        binding.ivResult.visibility = View.VISIBLE
        binding.ivNo.visibility = View.GONE
        binding.addSave.text = "Compress and save"
        binding.flDetail.visibility = View.VISIBLE
        Glide.with(this).load(data).fitCenter().into(binding.ivResult)
        data?.let {
            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        selectedImageBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                        selectedImageBitmap?.let { bit->
                            val ori = getImageSize(it)
                            binding.tvOriginal.text = "original：${bit.width}x${bit.height} ${ori}kb"
                        }
                        textChange()
                    }
                }
            }
        }

    }
   private fun textChange(){
        selectedImageBitmap?.let {
            val after = convertBitmapToFileSize(it,type)
            binding.tvAfter.text = "after：${it.width}x${it.height} ${after}kb"
            }

    }
    private fun saveCompressedImage(image: Bitmap,quality: Int) {
        val root = Environment.getExternalStorageDirectory()
        val fileDir = File("$root/CompressedImages")
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        val fileName = "compressed_image_${timeFormatter2.format(Date())}.jpg"
        val outputFile = File(fileDir, fileName)

        try {
            val outputStream = FileOutputStream(outputFile)
            image.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.close()
            // 添加到相册
            MediaStore.Images.Media.insertImage(contentResolver, outputFile.absolutePath, fileName, null)
            ToastUtils.showShort("Pictures are saved at:${outputFile.absolutePath}")
        } catch (e: IOException) {
        }
    }


   private fun convertBitmapToFileSize(bitmap: Bitmap,quality: Int): Long {
        val root = Environment.getExternalStorageDirectory()
        val fileDir = File("$root/CompressedImages")
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        val fileName = "temp.jpg"
        val outputFile = File(fileDir, fileName)
        val outputStream = ByteArrayOutputStream()
        try {
            // 将 Bitmap 压缩到输出流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            // 将输出流中的数据写入文件
            val fileOutputStream = FileOutputStream(outputFile)
            fileOutputStream.write(outputStream.toByteArray())
            fileOutputStream.close()
            // 返回文件大小
            return outputFile.length()/1024
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return 0L // 发生异常时返回0字节
    }
    private fun getImageSize(imageUri: Uri): Long {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val projection = arrayOf(MediaStore.Images.Media.SIZE)
        var imageSize: Long = -1

        // 使用内容提供程序查询获取图片的大小
        val cursor: Cursor? = contentResolver.query(imageUri, projection, null, null, null)
        cursor?.use { c ->
            if (c.moveToFirst()) {
                val sizeIndex: Int = c.getColumnIndex(MediaStore.Images.Media.SIZE)
                imageSize = c.getLong(sizeIndex)
            }
        }

        return imageSize/1024
    }
    override fun initData() {
        if (showSelect){
            val intent =  Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val root = Environment.getExternalStorageDirectory()
        val fileDir = File("$root/CompressedImages")
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        val fileName = "temp.jpg"
        val outputFile = File(fileDir, fileName)
        outputFile.delete()
    }
}