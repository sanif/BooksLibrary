package ae.bluecast.library.Activities

import ae.bluecast.library.R
import ae.bluecast.library.Utils.AppUtils
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchCategoriesActivity : AppCompatActivity() {

    lateinit var iv_back : ImageView
    lateinit var edtSearch : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_categories)
        initViews()
        setViews()
        initClicks()
    }

    private fun initViews() {
        iv_back = findViewById(R.id.iv_back)
        edtSearch = findViewById(R.id.edtSearch)
    }

    private fun setViews() {
        iv_back.setImageDrawable(AppUtils.getIcon(this,R.drawable.ic_back))
        edtSearch.setCompoundDrawablesWithIntrinsicBounds(
            AppUtils.getIcon(
                this,
                R.drawable.ic_search
            ), null, null, null
        )

    }

    private fun initClicks() {
        iv_back.setOnClickListener{
            onBackPressed()
        }
    }

}
