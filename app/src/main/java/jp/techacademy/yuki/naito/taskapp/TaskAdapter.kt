package jp.techacademy.yuki.naito.taskapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(context: Context): BaseAdapter() {
    private val mLayoutInflater: LayoutInflater
    //var mTaskList= mutableListOf<String>()
    var mTaskList= mutableListOf<Task>()
    init {
        this.mLayoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return mTaskList.size
    }

    override fun getItem(position: Int): Any {
        return mTaskList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null)

        val textView1 = view.findViewById<TextView>(android.R.id.text1)
        val textView2 = view.findViewById<TextView>(android.R.id.text2)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)
        // 後でTaskクラスから情報を取得するように変更する
        textView1.text = mTaskList[position].title
        textView2.text = simpleDateFormat.format(mTaskList[position].date)

        return view
    }
}
