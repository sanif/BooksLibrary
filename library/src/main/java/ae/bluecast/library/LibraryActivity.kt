package ae.bluecast.library

import ae.bluecast.library.Activities.SearchCategoriesActivity
import ae.bluecast.library.Adapter.CharacterSelectorAdapter
import ae.bluecast.library.Adapter.NewPagerAdapter
import ae.bluecast.library.Communications.ApiManager
import ae.bluecast.library.Interfaces.CharacterScrollListener
import ae.bluecast.library.Interfaces.CharacterSelectedListener
import ae.bluecast.library.Model.BookNameAlphabets
import ae.bluecast.library.Model.Category
import ae.bluecast.library.Model.CategoryResponse
import ae.bluecast.library.Utils.AppUtils
import ae.bluecast.library.Utils.CustomSliderLayoutManager
import ae.bluecast.library.Utils.PreferenceUtils
import ae.bluecast.library.Utils.ScreenUtils
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.Menu
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.support.v7.widget.Toolbar
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class LibraryActivity : AppCompatActivity() {
    //    CustomPagerAdapter adapter;
    lateinit var adapter: NewPagerAdapter

    lateinit var rvCenter:RecyclerView
    lateinit var vp :ViewPager
    lateinit var tvSearch : EditText
    lateinit var ivCircle : ImageView
    lateinit var toolBarMain : Toolbar
    lateinit var ivToolBarImage : ImageView

    var bookNameAlphabets: MutableList<BookNameAlphabets> = arrayListOf()

    internal var selectorListener: CharacterScrollListener? = null
    var dataArray: MutableList<ArrayList<Category>> = ArrayList()
    var isShelfRotated: Boolean = false
    var selectedCharacterPosition: Int = 0
    lateinit var viewpagerListener: ViewPager.OnPageChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceUtils.setColorCode(this, "#af165f")
        setContentView(R.layout.activity_library)
        initViews()
        //        adapter = new CustomPagerAdapter(getSupportFragmentManager());
        setViewIcons()
        getCategory()
        initListener()
        setChar()
        setUpToolBar()

    }

    private fun initViews() {
//        val icludedLayout = findViewById(R.id.icludedLayout)
        rvCenter = findViewById(R.id.rvData)
        vp = findViewById(R.id.vp)
        tvSearch = findViewById(R.id.tvSearch)
        ivCircle = findViewById(R.id.ivCircle)
        toolBarMain = findViewById(R.id.toolBarMain)
        ivToolBarImage = findViewById(R.id.ivToolBarImage)
    }

    private fun setViewPager(categories: ArrayList<Category>) {
        val vto = vp.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                vp.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = vp.width
                val height = vp.height
                val horizontalCount = getHorizontalCount(width)
                val verticalCount: Int = (height / (getBookWidth(width, horizontalCount) * 1.271028)).toInt()
                val params = vp.layoutParams
                params.height = Math.floor(
                    (getBookWidth(
                        width,
                        horizontalCount
                    ) * verticalCount * 1.271028037)
                ).toInt() + 100
                vp.layoutParams = params

                //SetUpViewPager
                var itemsPerPage = horizontalCount * verticalCount
                var pageCount: Int = categories.size / itemsPerPage
                var remainder = categories.size % itemsPerPage
                if (pageCount != 0 && remainder > 0) {
                    /*If more are left then a new page need to be allocated
                    ie: 5 items, itemsPerPage = 4 then remainder will be 1
                    so to include than 1 item, a new page need to be created.
                    */
                    pageCount += 1
                }
                if (pageCount == 0)
                    pageCount = 1

                categories.sort()//Data will be sorted according to name-> Sorted using Comaparable inside Datum Objects
                splitCategoryToList(categories, itemsPerPage)
                adapter = NewPagerAdapter(supportFragmentManager, pageCount, dataArray)
                vp.adapter = adapter
                vp.setPageTransformer(true, CubeOutTransformer())
                rvCenter.smoothScrollToPosition(0)

            }
        })
        vp.requestLayout()

    }

    private fun splitCategoryToList(categories: ArrayList<Category>, itemsPerPage: Int) {
        var tempChar: String = ""
        while (categories.size > 0) {

            var split: java.util.ArrayList<Category> = java.util.ArrayList()
            for (i: Int in 0 until itemsPerPage) {

                if (categories.size > 0) {

                    var firstChar: String = categories[0].categoryName.toString()[0].toString()
                    if (!firstChar.equals(tempChar)) {
                        categories[0].firstChar = firstChar
                        tempChar = firstChar
//                        var bookNameAlphabet = BookNameAlphabets()
//                        bookNameAlphabet.alphabet= firstChar
//                        bookNameAlphabets.add(bookNameAlphabet)

                    }
                    split.add(categories[0])
                    categories.remove(categories[0])
                }
            }
            dataArray.add(split)
        }
//        charAdapter.notifyDataSetChanged()
    }

    private fun getBookWidth(width: Int, x: Int): Int {
        return width / x
    }

    private fun getHorizontalCount(width: Int): Int {
        var gotCount: Boolean = true
        var count: Int = 1
        while (gotCount) {
            var value: Float = (width / count).toFloat()

            if (convertPixelsToDp(value, this) < 150) {
                gotCount = false
                return count
            }

            count++
        }

        return 1
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu.getItem(0).icon = AppUtils.getIcon(this, R.drawable.ic_notification)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setViewIcons() {
        tvSearch.setCompoundDrawablesWithIntrinsicBounds(
            AppUtils.getIcon(
                this,
                R.drawable.ic_search
            ), null, null, null
        )
        ivCircle.background = AppUtils.getIcon(this, ivCircle.background)
    }

    private fun setUpToolBar() {
        setSupportActionBar(toolBarMain)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(AppUtils.getIcon(this, R.drawable.ic_menu))
        ivToolBarImage.setImageResource(R.drawable.emirates)
    }

    private lateinit var charAdapter: CharacterSelectorAdapter

    private fun setChar() {
        charAdapter = CharacterSelectorAdapter(CharacterSelectedListener { pos ->
            //            val position = rvCenter.getChildLayoutPosition(view)
            rvCenter.smoothScrollToPosition(pos)
        }, bookNameAlphabets)

        rvCenter.adapter = charAdapter
        rvCenter.layoutManager =
                CustomSliderLayoutManager(
                    this,
                    LinearLayoutManager.HORIZONTAL,
                    false,
                    CharacterScrollListener { position ->

                        //Find character AT WHICH Page and move to it/.
                        selectedCharacterPosition = position
                        setViewColor(position)
                        val pos = findCharacterAndMoveShelf(bookNameAlphabets[position]?.alphabet ?: "")
                        if (pos != -1 && pos != vp.currentItem) {
                            isShelfRotated = true
                            vp.removeOnPageChangeListener(viewpagerListener)
                            vp.setCurrentItem(pos, true)
                            Handler().postDelayed({
                                vp.addOnPageChangeListener(viewpagerListener)
                            }, 1000L)
//
                        }
//                            manually = false

                    })
        val padding = ScreenUtils.getScreenWidth(this) / 2 - ScreenUtils.dpTopx(this, 17f)
        rvCenter.setPadding(padding, 0, padding, 0)


    }

    private fun initListener() {
        viewpagerListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    var index: Int = vp.currentItem
                    var pos: Int = -1

                    for (i in 0..bookNameAlphabets.size - 1) {
                        if (bookNameAlphabets[i].alphabet == dataArray[index][0].categoryName!![0].toString()) {
                            pos = i
                            if (pos == selectedCharacterPosition)
                                pos = -1
                            break
                        }
                    }
                    if (pos != -1) {
                        setViewColor(pos)
                        rvCenter.layoutManager!!.scrollToPosition(pos)
                        selectedCharacterPosition = pos
                    }
//                            rvCenter.layoutManager!!.smoothScrollToPosition(rvCenter,RecyclerView.State(),pos)

//                    }
                }

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {

            }

        }

        vp.addOnPageChangeListener(viewpagerListener)

        tvSearch.setOnClickListener {
            val intent = Intent(this@LibraryActivity, SearchCategoriesActivity::class.java)
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@LibraryActivity,
                tvSearch as View,
                resources.getString(R.string.txt_search_books)
            )
//            startActivity(intent, optionsCompat.toBundle())
            Toast.makeText(this,"This feature will be added soon",Toast.LENGTH_SHORT).show()

        }

    }

    private fun setViewColor(pos: Int) {
        for (i: Int in bookNameAlphabets.indices) {
            bookNameAlphabets[i].isSelected = i == pos
        }
        charAdapter.notifyDataSetChanged()
    }


    private fun findCharacterAndMoveShelf(c: String): Int {

        var valueGot: Int = -1
        var isValuePresentInSelectedDataSet: Boolean = false
        val viewPagerPos = vp.currentItem


//        if (!c.equals(dataArray[viewPagerPos][0].categoryName.toString()[0].toString(), true)) {
//            //Value not found on 1st item on current position
//            for (category: Category in dataArray[viewPagerPos]) {
//                if (category.firstChar.equals(c, true)) {
//                    isValuePresentInSelectedDataSet = true // if data present in current set dont scroll ,so -1
//                }
//            }
//        } else {
//            isValuePresentInSelectedDataSet = true
//        }

        if (!isValuePresentInSelectedDataSet) {
            for (i: Int in dataArray.indices) {
                for (category: Category in dataArray[i]) {
                    if (category.firstChar.equals(c, true)) {
                        valueGot = i
                        return valueGot
                    }
                }
            }
        }
        return valueGot
    }

    private fun getCategory() {
        val callback = object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.body()!!.success!!) {
                    setViewPager(response.body()!!.data!!)
                    response.body()!!.data!!.sort()
                    var temChar: String = ""
                    for (category: Category in response.body()!!.data!!) {
                        if (!temChar.equals(category.categoryName!![0].toString(), true)) {
                            temChar = category.categoryName!![0].toString()
                            var bookNameAlphabet = BookNameAlphabets()
                            bookNameAlphabet.alphabet = temChar
                            bookNameAlphabets.add(bookNameAlphabet)
                        }
                    }
                    charAdapter.notifyDataSetChanged()
                    setViewColor(0)

                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                t.printStackTrace()
            }
        }
        ApiManager.getApi().service.categories.enqueue(callback)
    }


}
