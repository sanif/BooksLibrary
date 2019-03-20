package ae.bluecast.library.Interfaces

import ae.bluecast.library.Model.Category


interface CategorySelectListener {
    fun onCategorySelected(data : Category)
}