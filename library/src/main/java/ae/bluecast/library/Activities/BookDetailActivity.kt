package ae.bluecast.library.Activities

import ae.bluecast.library.R
import ae.bluecast.library.Utils.AppUtils
import ae.bluecast.library.Utils.PermissionUtils
import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import java.io.File


class BookDetailActivity : AppCompatActivity() {

    lateinit var downloadManager: DownloadManager
    internal val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    lateinit var filename: String
    lateinit var fileExtension: String
    lateinit var broadCastReciever: BroadcastReceiver

    lateinit var btnOpen :Button
    lateinit var bookRating :RatingBar
    lateinit var ivBookCover : ImageView
    lateinit var toolbarGeneral :Toolbar
    lateinit var tvGeneralTitle :TextView

    var refid: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        initView()
        initClick()
        setUpToolbar()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastReciever)
    }

    private fun initClick() {
        btnOpen.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (PermissionUtils.hasPermissions(this@BookDetailActivity, true, PERMISSIONS)) {
                    //Start Download
                    downloadFile("https://s3-us-west-2.amazonaws.com/pressbooks-samplefiles/GrahamColorTheme/The-Prince-grahamcolor.pdf")
                }
            }
        })
        broadCastReciever = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var url: String = Environment.DIRECTORY_DOWNLOADS + "/BookShelf//$filename.$fileExtension"
                var file: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                var actualfile: File = File(file.absolutePath + "/BookShelf//$filename.$fileExtension")
                if (file.exists())
                    openFile(filename, actualfile.absolutePath)
            }

        }
        registerReceiver(broadCastReciever, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


    }


    private fun downloadFile(url: String) {
        var downloadUri: Uri = Uri.parse(url)
        val fullName = url.substringAfterLast("/")
        filename = fullName.substringBeforeLast(".")
        fileExtension = url.substringAfterLast(".")
        var file: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var actualfile: File = File(file.absolutePath + "/BookShelf//$filename.$fileExtension")
        if (actualfile.exists()) {
            openFile(filename, actualfile.absolutePath)
            return
        }
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("Bookshelf")
        request.setDescription("Downloading $filename.$fileExtension")
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "/BookShelf//$filename.$fileExtension"
        )
        refid = downloadManager.enqueue(request)

    }

    private fun openFile(name: String?, absolutePath: String) {
        val intent = Intent(this, FileReaderActivity::class.java)
        intent.putExtra("data", absolutePath)
        intent.putExtra("name", name)
        startActivity(intent)
    }


    private fun initView() {

        btnOpen = findViewById(R.id.btnOpen)
        bookRating = findViewById(R.id.bookRating)
        ivBookCover = findViewById(R.id.ivBookCover)
        toolbarGeneral = findViewById(R.id.toolbarGeneral)
        tvGeneralTitle = findViewById(R.id.tvGeneralTitle)


        bookRating.progressDrawable.setColorFilter(AppUtils.getColor(this), PorterDuff.Mode.SRC_IN)
        var url: String = intent.extras.getString("url", "")
        Glide.with(ivBookCover).load(url).into(ivBookCover)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.item_MyBooks -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mybooks, menu)
        menu!!.getItem(0).icon = AppUtils.getIcon(this, R.drawable.ic_mybooks)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            99 -> {
                if (PermissionUtils.hasPermissions(this, permissions)) {
                    //Start Download
                    downloadFile("https://s3-us-west-2.amazonaws.com/pressbooks-samplefiles/GrahamColorTheme/The-Prince-grahamcolor.pdf")
                }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbarGeneral)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(AppUtils.getIcon(this, R.drawable.ic_back))
        tvGeneralTitle.text = intent.extras.getString("categoryName")
        tvGeneralTitle.setTextColor(AppUtils.getColor(this))
    }
}
