package xyz.doikki.videoplayer.pipextension.simple.ui

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.ListAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import xyz.doikki.videoplayer.pipextension.VideoManager
import xyz.doikki.videoplayer.pipextension.databinding.VideoLayoutPlayUiBinding
import xyz.doikki.videoplayer.pipextension.simple.develop.isOverlayPermissions
import xyz.doikki.videoplayer.pipextension.simple.develop.launchOverlay

abstract class SimpleVideoUiActivity : AppCompatActivity() {

    private val typeOverlayLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && isOverlayPermissions()) {
            toPip()
        }
    }

    protected val viewBinding by lazy {
        VideoLayoutPlayUiBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    private val videoManager = VideoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!videoManager.onBackPressed()) {
                    onBackPressedCallback()
                }
            }
        })
        viewBinding.toolbar.title = createToolbarTitle()
        viewBinding.toolbar.setTitleTextColor(Color.WHITE)
        createPlayAdapter().let { viewBinding.listView.adapter = it }
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewBinding.toolbar.isVisible = isShowToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewBinding.listView.adapter = null
        super.onDestroy()
        videoManager.onDestroy()
        typeOverlayLauncher.unregister()
    }

    override fun onPause() {
        super.onPause()
        videoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        videoManager.onResume()
    }

    fun toPip() {
        if (!isOverlayPermissions()) {
            typeOverlayLauncher.launchOverlay(packageName)
        } else {
            videoManager.attachParent(release = false)
            finish()
        }
    }

    protected abstract fun createPlayAdapter(): ListAdapter?

    protected open fun onBackPressedCallback() {
        finish()
    }

    protected open fun isShowToolbar(): Boolean {
        return true
    }

    protected open fun createToolbarTitle(): String {
        return "视频播放"
    }

}