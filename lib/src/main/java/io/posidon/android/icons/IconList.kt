package io.posidon.android.icons

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.posidon.android.icons.tools.Tools
import io.posidon.android.icons.view.GridView
import io.posidon.android.launcherutils.IconTheming
import posidon.android.conveniencelib.Graphics
import posidon.android.conveniencelib.clone
import posidon.android.conveniencelib.dp

class IconList : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var searchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridView = GridView(this).apply {
            numColumns = 4
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, this@IconList.dp(72).toInt(), 0, this@IconList.dp(8).toInt())
            setFadingEdgeLength(this@IconList.dp(56).toInt())
            isVerticalFadingEdgeEnabled = true
            clipToPadding = false
        }
        searchBar = EditText(this@IconList).apply {
            hint = context.getString(R.string.search_icons)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    (gridView.adapter as IconsAdapter).search(s.toString())
                }
            })
            setPadding(this@IconList.dp(24).toInt(), 0, this@IconList.dp(24).toInt(), 0)
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, this@IconList.dp(64).toInt())
            setBackgroundColor(0xdd111213.toInt())
        }
        setContentView(FrameLayout(this).apply {
            addView(gridView)
            addView(searchBar)
        })
        window.decorView.setBackgroundColor(0x55111213)
        window.statusBarColor = 0xdd111213.toInt()

        gridView.adapter = IconsAdapter()
    }

    internal inner class IconsAdapter : BaseAdapter() {

        private val themeRes = packageManager.getResourcesForApplication(packageName)
        private val icons =
            try { IconTheming.getResourceNames(themeRes, packageName) }
            catch (e: Exception) { emptyList() }
        private val searchResults = ArrayList(icons)

        fun search(term: String) {
            searchResults.clear()
            val searchOptimizedTerm = Tools.searchOptimize(term)
            for (string in icons) {
                if (Tools.searchOptimize(string).contains(searchOptimizedTerm)) {
                    searchResults.add(string)
                }
            }
            notifyDataSetChanged()
        }

        override fun getCount(): Int = searchResults.size
        override fun getItem(position: Int): Any? = null
        override fun getItemId(position: Int): Long = 0

        inner class ViewHolder(val icon: ImageView)

        override fun getView(i: Int, cv: View?, parent: ViewGroup): View? {
            var convertView = cv
            val viewHolder: ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(this@IconList).inflate(R.layout.drawer_item, parent, false)
                viewHolder = ViewHolder(convertView.findViewById(R.id.iconimg))
                convertView.findViewById<View>(R.id.icontxt).visibility = View.GONE
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            val intRes = themeRes.getIdentifier(searchResults[i], "drawable", packageName)
            if (intRes == 0) {
                viewHolder.icon.setImageDrawable(null)
                viewHolder.icon.setOnClickListener(null)
            } else {
                val drawable = Graphics.tryAnimate(this@IconList, themeRes.getDrawable(intRes, null))
                viewHolder.icon.setImageDrawable(drawable)
                viewHolder.icon.setOnClickListener { onIconClick(it, i) }
            }
            return convertView
        }

        fun onIconClick(v: View, i: Int) {
            BottomSheetDialog(this@IconList, R.style.bottomsheet).run {
                setContentView(LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER_HORIZONTAL
                    addView(ImageView(context).apply {
                        setImageDrawable(
                            drawable.clone()
                                ?.let { d -> Graphics.tryAnimate(this@IconList, d) })
                        layoutParams = ViewGroup.LayoutParams(
                            dp(185).toInt(),
                            this@IconList.dp(185).toInt()
                        )
                        setPadding(
                            0,
                            this@IconList.dp(24).toInt(),
                            0,
                            this@IconList.dp(10).toInt()
                        )
                    })
                    addView(TextView(context).apply {
                        text = searchResults[i]
                        textSize = 20f
                        setTextColor(0xffffffff.toInt())
                        setPadding(
                            0,
                            this@IconList.dp(10).toInt(),
                            0,
                            this@IconList.dp(24).toInt()
                        )
                        gravity = Gravity.CENTER_HORIZONTAL
                    })
                })
                window!!.findViewById<View>(R.id.design_bottom_sheet)
                    .setBackgroundResource(R.drawable.bottom_sheet)
                show()
            }
        }
    }
}