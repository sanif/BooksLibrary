package ae.bluecast.library.Adapter

import ae.bluecast.library.Interfaces.CharacterSelectedListener
import ae.bluecast.library.Model.BookNameAlphabets
import ae.bluecast.library.R
import ae.bluecast.library.Utils.CustomSliderLayoutManager
import android.content.Context
import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CharacterSelectorAdapter(
    internal var selectedListener: CharacterSelectedListener,
    alphabet: MutableList<BookNameAlphabets>
) :
        RecyclerView.Adapter<CharacterSelectorAdapter.cVIewHolder>() {

    val cAlphabet = alphabet

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): cVIewHolder {
        return cVIewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_character_selector, viewGroup, false))

    }

    override fun onBindViewHolder(cVIewHolder: cVIewHolder, i: Int) {
        cVIewHolder.setIsRecyclable(false)
        cVIewHolder.tvChar.text = cAlphabet[i].alphabet
        cVIewHolder.itemView.setOnClickListener { view ->
            CustomSliderLayoutManager.isDragging = true
            selectedListener.onItemSelected(i)
        }
        if (cAlphabet[i].isSelected)
            cVIewHolder.tvChar.setTextColor(cVIewHolder.tvChar.context.resources.getColor(R.color.white))
        else
            cVIewHolder.tvChar.setTextColor(cVIewHolder.tvChar.context.resources.getColor(R.color.textGrey))

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return cAlphabet.size
    }

    inner class cVIewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvChar = itemView.findViewById<TextView>(R.id.tvChar)
        fun setOverlayColor(@ColorInt color: Int) {
            tvChar.setBackgroundColor(color)
        }

    }

}
