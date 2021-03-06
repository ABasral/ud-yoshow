package udirect.com.yoshow

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
//import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic
import ly.img.android.pesdk.assets.font.basic.FontPackBasic
import ly.img.android.pesdk.assets.frame.basic.FramePackBasic
import ly.img.android.pesdk.assets.overlay.basic.OverlayPackBasic
import ly.img.android.pesdk.assets.sticker.emoticons.StickerPackEmoticons
import ly.img.android.pesdk.assets.sticker.shapes.StickerPackShapes
import ly.img.android.pesdk.backend.model.constant.Directory
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.backend.model.state.SaveSettings
import ly.img.android.pesdk.backend.model.state.VideoEditorSaveSettings
import ly.img.android.pesdk.backend.model.state.manager.SettingsList
import ly.img.android.pesdk.ui.activity.ImgLyIntent
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.*
import ly.img.android.pesdk.ui.utils.PermissionRequest
import ly.img.android.serializer._3._0._0.PESDKFileWriter
import java.io.File
import java.io.IOException
import android.content.Intent as Intent1

class edit : Activity(), PermissionRequest.Response {

    companion object {
        const val VESDK_RESULT = 1
        const val GALLERY_RESULT = 2
    }

    // Important permission request for Android 6.0 and above, don't forget to add this!
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun permissionGranted() {}

    override fun permissionDenied() {
        /* TODO: The Permission was rejected by the user. The Editor was not opened,
         * Show a hint to the user and try again. */
    }

    // Create a empty new SettingsList and apply the changes on this referance.
    // If you include our asset Packs and use our UI you also need to add them to the UI,
    // otherwise they are only available for the backend (like Serialisation)
    // See the specific feature sections of our guides if you want to know how to add our own Assets.
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun createVesdkSettingsList() =
            VideoEditorSettingsList()
                    .configure<UiConfigFilter> {
                        it.setFilterList(FilterPackBasic.getFilterPack())
                    }
                    .configure<UiConfigText> {
                        it.setFontList(FontPackBasic.getFontPack())
                    }
                    .configure<UiConfigFrame> {
                        it.setFrameList(FramePackBasic.getFramePack())
                    }
                    .configure<UiConfigOverlay> {
                        it.setOverlayList(OverlayPackBasic.getOverlayPack())
                    }
                    .configure<UiConfigSticker> {
                        it.setStickerLists(
                                StickerPackEmoticons.getStickerCategory(),
                                StickerPackShapes.getStickerCategory()
                        )
                    }
                    .configure<VideoEditorSaveSettings> {
                        // Set custom editor image export settings
                        it.setExportDir(Directory.DCIM, "SomeFolderName")
                        it.setExportPrefix("result_")
                        it.setSavePolicy(SaveSettings.SavePolicy.RETURN_ALWAYS_ONLY_OUTPUT)
                    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

         val sharebut = findViewById<Button>(R.id.share)
            sharebut.setOnClickListener{
                val intent = Intent1(this,udirect.com.yoshow.Share.NextActivity::class.java)
                startActivity(intent)
            }
        val againbut = findViewById<Button>(R.id.again)
        againbut.setOnClickListener{
            val intent = Intent1(this,udirect.com.yoshow.opengl.NewStoryActivity::class.java)
            startActivity(intent)
        }

        //openGallery.setOnClickListener {
        val videoView = findViewById<VideoView>(R.id.video)
        //Creating MediaController
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        //specify the location of media file
        val uri:Uri = Uri.fromFile( File(
                Environment.getExternalStorageDirectory()
                        .path + "/UDIRECT/final.mp4")
        )
        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.seekTo(1)
        videoView.start()

        openEditor(Uri.fromFile( File(
                Environment.getExternalStorageDirectory()
                        .path + "/UDIRECT/final.mp4")
        ))
        //}

    }

    fun openSystemGalleryToSelectAnVideo() {
        val intent = Intent1(Intent1.ACTION_PICK)
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,"video/*")

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, GALLERY_RESULT)
        } else {
            Toast.makeText(
                    this,
                    "No Gallery APP installed",
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    fun openEditor(inputImage: Uri?) {
        val settingsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            createVesdkSettingsList()
        } else {
            Toast.makeText(this, "Video support needs Android 4.3", Toast.LENGTH_LONG).show()
            return
        }

        settingsList.configure<LoadSettings> {
            it.source = inputImage
        }

        settingsList[LoadSettings::class].source = inputImage

        VideoEditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, VESDK_RESULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent1?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == GALLERY_RESULT) {
            // Open Editor with some uri in this case with an video selected from the system gallery.
            openEditor(data?.data)

        } else if (resultCode == RESULT_OK && requestCode == VESDK_RESULT) {
            // Editor has saved an Video.
            val resultURI = data?.getParcelableExtra<Uri?>(ImgLyIntent.RESULT_IMAGE_URI)?.also {
                // Scan result uri to show it up in the Gallery
                sendBroadcast(Intent1(Intent1.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(it))
            }

            val sourceURI = data?.getParcelableExtra<Uri?>(ImgLyIntent.SOURCE_IMAGE_URI)?.also {
                // Scan source uri to show it up in the Gallery
                sendBroadcast(Intent1(Intent1.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(it))
            }

            Log.i("VESDK", "Source video is located here $sourceURI")
            Log.i("VESDK", "Result video is located here $resultURI")

            // TODO: Do something with the result image
            if (resultURI != null) {
                File(resultURI.path).copyTo(File(Environment.getExternalStorageDirectory().path + "/UDIRECT/final.mp4"), true)
            }
          val somedir =   File(Environment.getExternalStorageDirectory().path+"/DCIM/SomeFolderName")
            somedir.deleteRecursively()
            val somedir1 =   File(Environment.getExternalStorageDirectory().path+"/Stories")
            somedir1.deleteRecursively()
            // OPTIONAL: read the latest state to save it as a serialisation
            val lastState = data?.getParcelableExtra<SettingsList>(ImgLyIntent.SETTINGS_LIST)
            try {
                PESDKFileWriter(lastState).writeJson(
                        File(
                                Environment.getExternalStorageDirectory(),
                                "serialisationReadyToReadWithPESDKFileReader.json"
                        )
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else if (resultCode == RESULT_CANCELED && requestCode == VESDK_RESULT) {
            // Editor was canceled
            val sourceURI = data?.getParcelableExtra<Uri?>(ImgLyIntent.SOURCE_IMAGE_URI)
            // TODO: Do something with the source...
        }
    }

    override fun onBackPressed() {
        val intent = Intent1(this,udirect.com.yoshow.Home.HomeActivity::class.java)
        startActivity(intent)
    }
}