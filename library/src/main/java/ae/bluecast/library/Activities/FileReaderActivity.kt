package ae.bluecast.library.Activities

import ae.bluecast.library.R
import ae.bluecast.library.Utils.AppUtils
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import java.io.File

class FileReaderActivity : AppCompatActivity() {

    lateinit var fileName: String

    lateinit var toolbarGeneral :Toolbar
    lateinit var tvGeneralTitle : TextView
    lateinit var pdfView : PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_file_reader)
        var url: String = intent.extras.getString("data")
        fileName = intent.extras.getString("name")
        var actualfile: File = File(url)
        initViews()
        setUptoolBar()
        loadFile(actualfile)
    }

    private fun initViews() {
        toolbarGeneral = findViewById(R.id.toolbarGeneral)
        tvGeneralTitle = findViewById(R.id.tvGeneralTitle)
        pdfView = findViewById(R.id.pdfView)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUptoolBar() {
        setSupportActionBar(toolbarGeneral)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(AppUtils.getIcon(this, R.drawable.ic_back))
        tvGeneralTitle.text = fileName
        tvGeneralTitle.setTextColor(AppUtils.getColor(this))
    }

    private fun loadFile(actualfile: File) {

        if (actualfile.exists()) {
            pdfView.fromFile(actualfile)
                .enableAnnotationRendering(true)
                .swipeHorizontal(false)
                .nightMode(true)
                .onLoad(object : OnLoadCompleteListener {
                    override fun loadComplete(nbPages: Int) {
                        Toast.makeText(this@FileReaderActivity, "Completed", Toast.LENGTH_SHORT).show()
                    }
                })
                .spacing(10) // in dp
                .onPageError(object : OnPageErrorListener {
                    override fun onPageError(page: Int, t: Throwable?) {
                        Toast.makeText(this@FileReaderActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
                .load()
        }

    }
}
