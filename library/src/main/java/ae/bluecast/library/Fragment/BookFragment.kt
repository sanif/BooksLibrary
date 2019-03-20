package ae.bluecast.library.Fragment

import ae.bluecast.library.Activities.BooksOfCategoryActivity
import ae.bluecast.library.Adapter.ShelfAdapter
import ae.bluecast.library.Interfaces.CategorySelectListener
import ae.bluecast.library.Model.Category
import ae.bluecast.library.R
import ae.bluecast.library.Utils.ScreenUtils
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import java.util.ArrayList


class BookFragment : Fragment() {

    lateinit var adapter: ShelfAdapter

    lateinit var categories: ArrayList<Category>
    lateinit var categorySelected: CategorySelectListener

lateinit var rvList: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =inflater.inflate(R.layout.fragment_terror, container, false)
        categories = arguments!!.get("data") as ArrayList<Category>
        initListeners()
        initView(view)

        return view
    }

    private fun initListeners() {
        categorySelected = object : CategorySelectListener {
            override fun onCategorySelected(data: Category) {
                val intent = Intent(activity, BooksOfCategoryActivity::class.java)
                intent.putExtra("categoryName",data.categoryName)
                startActivity(intent)
            }
        }
    }

    private fun initView(view: View) {
        rvList = view.findViewById(R.id.rvList)

        val vto = rvList.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {


                rvList.getViewTreeObserver().removeOnGlobalLayoutListener(this)

                val width = rvList.width
                val height = rvList.height

                val horizontalCount = getHorizontalCount(width)
                val verticalCount: Int = (height / (getBookWidth(width, horizontalCount) * 1.271028)).toInt()
//                val n = 2
//                binding.rvList.layoutParams.height = getBookHeight(getBookWidth(width, x)) * n
                rvList.layoutManager =
                        GridLayoutManager(activity, horizontalCount) as RecyclerView.LayoutManager?
                rvList.adapter =
                        ShelfAdapter(getBookWidth(width, horizontalCount), verticalCount, horizontalCount, categories,categorySelected)
//                binding.rvList.addItemDecoration(GridItemDecorator())


                val params = rvList.getLayoutParams()
                params.height = Math.floor(
                    (getBookWidth(
                        width,
                        horizontalCount
                    ) * verticalCount * 1.271028037)
                ).toInt() + 100
                rvList.setLayoutParams(params)


            }
        })
    }

    private fun getBookHeight(width: Int): Int {
        return width
    }

    private fun getBookWidth(width: Int, x: Int): Int {
        return width / x
    }

    private fun getHorizontalCount(width: Int): Int {
        var gotCount: Boolean = true
        var count: Int = 1
        while (gotCount) {
            var value: Float = (width / count).toFloat()

            if (ScreenUtils.convertPixelsToDp(value, context!!) < 150) {
                gotCount = false
                return count
            }

            count++
        }

        return 1
    }



}
