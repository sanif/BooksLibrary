package ae.bluecast.library.Model

import java.io.Serializable

class Category : Serializable, Comparable<Category> {
    var categoryName: String? = null
    var categoryId: Int? = null
    var noOfBooks: Int? = null
    var firstChar: String? = ""
    var categoryBookUrl: String? = null

    override fun compareTo(datum: Category): Int {

        return this.categoryName!!.compareTo(datum.categoryName!!, ignoreCase = true)
    }
}
