package jp.techacademy.yuki.naito.taskapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import jp.techacademy.yuki.naito.taskapp.databinding.ActivityMainBinding
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import io.realm.RealmChangeListener
import io.realm.Sort
import java.util.*
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.realm.RealmResults

const val EXTRA_TASK = "jp.techacademy.yuki.naito.taskapp.TASK"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var mTaskAdapter: TaskAdapter
    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm> {
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

    /*    val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)*/

        binding.fab.setOnClickListener { view ->
     /*       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }
        // Realmの設定
        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        // ListViewの設定
        mTaskAdapter = TaskAdapter(this)
        toolbar.setTitleTextColor(Color.WHITE)
        searchButton.setBackgroundColor(Color.BLUE)
        searchButton.setOnClickListener {
            Log.d("Clock", "検索ボタンがクリックされました。")
            reloadSelectedListView()

        }
        // ListViewをタップしたときの処理
        listView1.setOnItemClickListener { parent, view, position, id ->
            // 入力・編集する画面に遷移させる
            val task = parent.adapter.getItem(position) as Task
            val intent = Intent(this, InputActivity::class.java)
            intent.putExtra(EXTRA_TASK, task.id)
            startActivity(intent)
        }

        // ListViewを長押ししたときの処理
        listView1.setOnItemLongClickListener { parent, _, position, _ ->
            // タスクを削除する
            val task = parent.adapter.getItem(position) as Task

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this)

            builder.setTitle("削除")
            builder.setMessage(task.title + "を削除しますか")

            builder.setPositiveButton("OK"){_, _ ->
                val results = mRealm.where(Task::class.java).equalTo("id", task.id).findAll()

                mRealm.beginTransaction()
                results.deleteAllFromRealm()
                mRealm.commitTransaction()

                reloadListView()
            }

            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }

        // アプリ起動時に表示テスト用のタスクを作成する
        //addTaskForTest()

        reloadListView()
    }
    private fun reloadSelectedListView() {
        var realm = Realm.getDefaultInstance()
        var taskRealmResults: RealmResults<Task>
        // Realmデータベースから、「すべてのデータを取得して新しい日時順に並べた結果」を取得
        if(!searchView.text.toString().equals(""))
            taskRealmResults = realm.where(Task::class.java).equalTo("category",searchView.text.toString()).findAll().sort("date", Sort.DESCENDING)
        else
            taskRealmResults = mRealm.where(Task::class.java).findAll().sort("date", Sort.DESCENDING)
        // 上記の結果を、TaskListとしてセットする
        mTaskAdapter.mTaskList = realm.copyFromRealm(taskRealmResults)
        // TaskのListView用のアダプタに渡す
        listView1.adapter = mTaskAdapter

        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mTaskAdapter.notifyDataSetChanged()
    }
    private fun reloadListView() {
        var realm = Realm.getDefaultInstance()
        // Realmデータベースから、「すべてのデータを取得して新しい日時順に並べた結果」を取得
        val taskRealmResults = realm.where(Task::class.java).findAll().sort("date", Sort.DESCENDING)

        // 上記の結果を、TaskListとしてセットする
        mTaskAdapter.mTaskList = realm.copyFromRealm(taskRealmResults)
        // TaskのListView用のアダプタに渡す
        listView1.adapter = mTaskAdapter

        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mTaskAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()

        mRealm.close()
    }

    private fun addTaskForTest() {
        val task = Task()
        task.title = "作業"
        task.contents = "プログラムを書いてPUSHする"
        task.date = Date()
        task.id = 0
        mRealm.beginTransaction()
        mRealm.copyToRealmOrUpdate(task)
        mRealm.commitTransaction()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}