package ae.bluecast.library.Adapter

import ae.bluecast.library.Interfaces.BookSelectListener
import ae.bluecast.library.Model.Books
import ae.bluecast.library.R
import ae.bluecast.library.Utils.AppUtils
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide

class BookListAdapter(bookList: ArrayList<Books>, bookWidth: Int, bookHeight: Int, listener: BookSelectListener) :
    RecyclerView.Adapter<BookListAdapter.CustomBooksHolder>() {

    lateinit var view: View
    var cViewType: Int = 0
    val cBookWidth = bookWidth
    val cBookHeight = bookHeight
    val cListener: BookSelectListener = listener
    var cBookList: ArrayList<Books> = bookList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomBooksHolder {
        if (getcViewType() == 0) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_books_list, parent, false)
            return BooksListViewHolder(view)

        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_books_grid, parent, false)
//            view = LayoutInflater.from(parent.context).inflate(R.layout.item_books_list, parent, false)
            return BooksGridViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return cBookList.size
    }

    override fun onBindViewHolder(holder: CustomBooksHolder, position: Int) {

        if (getcViewType() == 0) {
            holder as BooksListViewHolder
            holder.bindView(position)
        } else {
            holder as BooksGridViewHolder
            holder.bindView(position)
        }
//        holder.itemView.setOnClickListener(object: View.OnClickListener{
//            override fun onClick(p0: View?) {
//                cListener.onBookSelected(position,)
//            }
//
//        })


    }

    open class CustomBooksHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class BooksListViewHolder(itemView: View) : CustomBooksHolder(itemView) {

        var ivBook = itemView.findViewById<ImageView>(R.id.ivBookListCover)!!
        var tvAuthor: TextView = itemView.findViewById(R.id.tvListAuthor)
        var tvBookName: TextView = itemView.findViewById(R.id.tvBookListName)
        var ratingBar: RatingBar = itemView.findViewById(R.id.ratingBarList)
        var tvRating: TextView = itemView.findViewById(R.id.tvListRating)
        fun bindView(position: Int) {
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    cListener.onBookSelected(position, ivBook)
                }

            })
            Glide.with(ivBook.context).load(cBookList[position].bookImageUrl).into(ivBook)
            tvAuthor.text = cBookList[position].authorName
            tvBookName.text = cBookList[position].BookName
            ratingBar.rating = cBookList[position].Rating!!.toFloat()
            ratingBar.progressDrawable.setColorFilter(AppUtils.getColor(ratingBar.context), PorterDuff.Mode.SRC_IN)
            tvRating.text = cBookList[position].Rating.toString()
        }

    }

    inner class BooksGridViewHolder(itemView: View) : CustomBooksHolder(itemView) {

        var ivBookCoverGrid = itemView.findViewById<ImageView>(R.id.ivBookCoverGrid)!!
        var tvAuthor: TextView = itemView.findViewById(R.id.tvAuthorNameGrid)
        var tvBookName: TextView = itemView.findViewById(R.id.tvBookNameGrid)
        var tvRating: TextView = itemView.findViewById(R.id.tvGridRating)
        var ratingBar: RatingBar = itemView.findViewById(R.id.ratingBarGrid)
        fun bindView(position: Int) {
            ivBookCoverGrid.layoutParams.height = cBookHeight
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    cListener.onBookSelected(position, ivBookCoverGrid)
                }

            })
            Glide.with(ivBookCoverGrid.context).load(cBookList[position].bookImageUrl).into(ivBookCoverGrid)
            tvAuthor.text = cBookList[position].authorName
            tvBookName.text = cBookList[position].BookName
            tvRating.text = cBookList[position].Rating.toString()
            ratingBar.progressDrawable.setColorFilter(AppUtils.getColor(ratingBar.context), PorterDuff.Mode.SRC_IN)

        }
    }


    fun getcViewType(): Int {
        return cViewType
    }

    fun setcViewType(viewType: Int) {
        this.cViewType = viewType
    }

}