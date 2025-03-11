package tw.edu.ncku.gs.R76131117

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 讓程式知道執行時該呼叫哪個 layout 檔來顯示螢幕(呼叫 activity_main.xml)
        // R 代表 res 目錄內的資源檔案（resources）。
        // layout 代表 res/layout/ 資料夾。
        // activity_main 代表 activity_main.xml 這個佈局檔案。
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val linearLayout = findViewById<LinearLayout>(R.id.main)
        // textView 要放進 layout 後使用者才能在螢幕上看見訊息
        // val textView = TextView(this)
        // textView.text = "Secret"

        val cheerLeaders = listOf("Jason","Timmy","Yoyo")
        for(i in 0..2){
            // 宣告一個 TextView 物件
            val textView = TextView(this)
            textView.text = cheerLeaders[i]
            linearLayout.addView(textView)
        }

        // Log 要放在 onCreate 函式裡，msg 為要輸出的訊息，建立 Log 後即可使用 LogCat 來篩選訊息進行 Debug
        Log.v("Jason","Secret")
        Log.d("Jason","Baseball")
        Log.i("Jason","Tainan")
        Log.w("Jason","Mall");
        Log.e("Jason","Kotlin")
    }
}