package ae.bluecast.library.Activities

import ae.bluecast.library.Adapter.BookListAdapter
import ae.bluecast.library.Adapter.SuggestedBookAdapter
import ae.bluecast.library.Communications.ApiManager
import ae.bluecast.library.Interfaces.BookSelectListener
import ae.bluecast.library.Model.Books
import ae.bluecast.library.Model.GetBooksResponse
import ae.bluecast.library.R
import ae.bluecast.library.Utils.AppUtils
import ae.bluecast.library.Utils.ScreenUtils
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class BooksOfCategoryActivity : AppCompatActivity() {

    var menu: Menu? = null
    lateinit var adapter: BookListAdapter
    lateinit var bookSelectListener: BookSelectListener
    var bookList: ArrayList<Books> = ArrayList()
    var categoryName: String = ""

    lateinit var ivClose:ImageView
    lateinit var llSuggestion:LinearLayout
    lateinit var rvSuggestedBooks:RecyclerView
    lateinit var rvBooks : RecyclerView
    lateinit var toolbarGeneral :Toolbar
    lateinit var tvGeneralTitle :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_books_of_category)
        categoryName = intent.extras.getString("categoryName")
        initViews()
        setUpToolBar(categoryName)
        initClicks()
        getBooks()
    }

    private fun initViews() {
        ivClose = findViewById(R.id.ivClose)
        llSuggestion = findViewById(R.id.llSuggestion)
        rvSuggestedBooks = findViewById(R.id.rvSuggestedBooks)
        rvBooks = findViewById(R.id.rvBooks)
        toolbarGeneral = findViewById(R.id.toolbarGeneral)
        tvGeneralTitle = findViewById(R.id.tvGeneralTitle)
    }

    private fun getBooks() {
        val callback = object : Callback<GetBooksResponse> {
            override fun onResponse(call: Call<GetBooksResponse>, response: Response<GetBooksResponse>) {
                if (response.isSuccessful) {
                    bookList.addAll(response.body()!!.data!!)
                    setUpBooksList()
                    setUpSuggestedBooks()

                }
            }

            override fun onFailure(call: Call<GetBooksResponse>, t: Throwable) {

            }
        }
        ApiManager.getApi().service.books.enqueue(callback)
    }

    private fun initClicks() {
        ivClose.setOnClickListener {
            llSuggestion.visibility = View.GONE
        }
        bookSelectListener = object : BookSelectListener {
            override fun onBookSelected(position: Int, view: View) {
                val intent = Intent(this@BooksOfCategoryActivity, BookDetailActivity::class.java)
                intent.putExtra("categoryName",categoryName)
                intent.putExtra("url",bookList[position].bookImageUrl)
                var compat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@BooksOfCategoryActivity, view, "image")
                startActivity(intent, compat.toBundle())
            }
        }
    }

    private fun setUpSuggestedBooks() {
        rvSuggestedBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvSuggestedBooks.adapter = SuggestedBookAdapter(bookList)
        val linearSnapHelper: LinearSnapHelper? = LinearSnapHelper()
        linearSnapHelper!!.attachToRecyclerView(rvSuggestedBooks)
        val speedScroll: Long = 2000
        val handler = Handler()
        val runnable = object : Runnable {
            var count = 0
            override fun run() {
                if (count < rvSuggestedBooks.adapter!!.itemCount) {
                    rvSuggestedBooks.smoothScrollToPosition(count)
                    handler.postDelayed(this, speedScroll)
                    count++
                } else {
                    count = 0
                    handler.postDelayed(this, speedScroll)
                }
            }
        }

        handler.postDelayed(runnable, speedScroll)
    }

    private fun setUpBooksList() {
        if (bookList.size > 0) {
            adapter = BookListAdapter(bookList, 0, 0, bookSelectListener)
            adapter.setcViewType(0)
            rvBooks.layoutManager = LinearLayoutManager(this)
            rvBooks.adapter = ScaleInAnimationAdapter(adapter).apply {
                setDuration(300)
                setFirstOnly(true)
                setInterpolator(OvershootInterpolator())
            }
        }
    }

    private fun setUpBooksGridList() {
        if (bookList.size > 0) {
            val horizontalCount = getHorizontalCount(ScreenUtils.getScreenWidth(this))
            val bookWidth = ScreenUtils.getScreenWidth(this) / horizontalCount
            val bookHeight = bookWidth * 1.271028
            rvBooks.layoutManager = GridLayoutManager(this, horizontalCount)
            adapter = BookListAdapter(bookList, bookWidth, bookHeight.toInt(), bookSelectListener)
            adapter.setcViewType(1)
            rvBooks.adapter = ScaleInAnimationAdapter(adapter).apply {
                setDuration(300)
                setFirstOnly(true)
                setInterpolator(OvershootInterpolator())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.books_category_menu, menu)
        this.menu = menu
        menu!!.getItem(2).icon = AppUtils.getIcon(this, R.drawable.ic_search)
        menu.getItem(0).icon = AppUtils.getIcon(this, R.drawable.ic_list)
        menu.getItem(1).icon = AppUtils.getIcon(this, R.drawable.ic_grid)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.action_grid -> {
                val itemGrid = menu!!.findItem(R.id.action_grid)
                itemGrid.isVisible = false

                val itemList = menu!!.findItem(R.id.action_list)
                itemList.isVisible = true
                setUpBooksGridList()

            }
            R.id.action_list -> {
                val itemList = menu!!.findItem(R.id.action_list)
                itemList.isVisible = false

                val itemGrid = menu!!.findItem(R.id.action_grid)
                itemGrid.isVisible = true
                setUpBooksList()
            }
            R.id.action_search -> {

            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun setUpToolBar(categoryName: String) {
        setSupportActionBar(toolbarGeneral)
        tvGeneralTitle.text = categoryName
        tvGeneralTitle.setTextColor(AppUtils.getColor(this))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(AppUtils.getIcon(this, R.drawable.ic_back))
    }

    private fun getHorizontalCount(width: Int): Int {
        var gotCount: Boolean = true
        var count: Int = 1
        while (gotCount) {
            var value: Float = (width / count).toFloat()

            if (ScreenUtils.convertPixelsToDp(value, this!!) < 180) {
                gotCount = false
                return count
            }
            count++
        }

        return 1
    }

}
