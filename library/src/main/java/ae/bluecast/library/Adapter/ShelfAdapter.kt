package ae.bluecast.library.Adapter

import ae.bluecast.library.Interfaces.CategorySelectListener
import ae.bluecast.library.Model.Category
import ae.bluecast.library.R
import ae.bluecast.library.Utils.AppUtils
import android.content.Context
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide

import java.util.ArrayList

class ShelfAdapter(
    width: Int,
    verticalCount: Int,
    horizontalCount: Int,
    private val list: ArrayList<Category>,
    private val listener: CategorySelectListener
) : RecyclerView.Adapter<ShelfAdapter.CustomView>() {
    private val width: Int
    private val verticalCount: Int
    private val mHorizontalCount: Int
    private val books = intArrayOf(
        R.drawable.ic_book1,
        R.drawable.ic_book2,
        R.drawable.ic_book3,
        R.drawable.ic_book4,
        R.drawable.ic_book5,
        R.drawable.ic_book6,
        R.drawable.ic_book7,
        R.drawable.ic_book8,
        R.drawable.ic_book9
    )

    init {

        this.width = width
        this.verticalCount = verticalCount
        this.mHorizontalCount = horizontalCount
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomView {
        return CustomView(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_book, viewGroup, false))
    }

    override fun onBindViewHolder(customView: CustomView, position: Int) {
        //        int verticalCount = ScreenUtils.dpTopx(customView.itemView.getContext(),150.0f);
        //        int width = ScreenUtils.dpTopx(customView.itemView.getContext(),120.0f);
        //        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, verticalCount);
        ////        params.setMarginStart(5);
        ////        params.setMargins(0,25,0,20);
        //
        //        customView.llBack.setLayoutParams(params);

        customView.flMain.setOnClickListener { listener.onCategorySelected(list[position]) }
        if (position < list.size) {
            Glide.with(customView.ivBook.context).load(list[position].categoryBookUrl).into(customView.ivBook)
            customView.tvBookCount.text = list[position].noOfBooks.toString()
            customView.tvCategoryName.text = list[position].categoryName.toString()
            if (!list[position].firstChar.equals("")) {
                customView.tvFirstChar.text = list[position].firstChar
                customView.tvFirstChar.background = AppUtils.getIcon(customView.tvFirstChar.context, customView.tvFirstChar.background)
            }else{
                customView.tvFirstChar.visibility=View.GONE
            }
        } else {
            customView.flMain.visibility = View.GONE
        }

        customView.llBack.layoutParams.height = (width * 1.27).toInt()
        customView.ivBook.setImageResource(books[2])

        if (position == mHorizontalCount * verticalCount - 1) {
            customView.llBack.setBackgroundResource(R.drawable.bottom_right)
        } else if (position == mHorizontalCount * verticalCount - mHorizontalCount) {
            customView.llBack.setBackgroundResource(R.drawable.bottom_left)
        } else if (position < mHorizontalCount * verticalCount - 1 && position > mHorizontalCount * verticalCount - mHorizontalCount) {
            customView.llBack.setBackgroundResource(R.drawable.bottom_center)
        } else if (position % mHorizontalCount == 0) {
            customView.llBack.setBackgroundResource(R.drawable.top_left)
        } else if (position % mHorizontalCount == mHorizontalCount - 1) {
            customView.llBack.setBackgroundResource(R.drawable.top_right)
        } else if (position % mHorizontalCount > 0 && position % mHorizontalCount < mHorizontalCount - 1) {
            customView.llBack.setBackgroundResource(R.drawable.top_center)
        }

        //        customView.ivBook.setPadding(22,35,18,10);
    }

    override fun getItemCount(): Int {
        return mHorizontalCount * verticalCount
    }

    inner class CustomView(itemView: View) : RecyclerView.ViewHolder(itemView){
        var flMain = itemView.findViewById<FrameLayout>(R.id.flMain)!!
        var ivBook = itemView.findViewById<ImageView>(R.id.iv_book)!!
        var tvBookCount = itemView.findViewById<TextView>(R.id.tvBookCount)!!
        var tvCategoryName = itemView.findViewById<TextView>(R.id.tvCategoryName)!!
        var tvFirstChar = itemView.findViewById<TextView>(R.id.tvFirstChar)!!
        var llBack = itemView.findViewById<LinearLayout>(R.id.llBack)!!

    }

}
