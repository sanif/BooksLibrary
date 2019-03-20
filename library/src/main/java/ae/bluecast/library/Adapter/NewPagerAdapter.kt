package ae.bluecast.library.Adapter

import ae.bluecast.library.Fragment.BookFragment
import ae.bluecast.library.Model.Category
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

class NewPagerAdapter internal constructor(
    fm: FragmentManager,
    cou: Int,
    categories: MutableList<ArrayList<Category>>
) : FragmentPagerAdapter(fm) {


    private val cCount = cou
    private val mCategories : MutableList<ArrayList<Category>> = categories

    override fun getItem(p0: Int): Fragment {

         var args :Bundle = Bundle()
        args.putSerializable("data",mCategories[p0])
        var mFragment : BookFragment = BookFragment()
        mFragment.arguments = args
        return mFragment
    }

    override fun getCount(): Int {
        return cCount
    }

}