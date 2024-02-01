package jp.co.abs.paintkamada.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun saveDrawingProcess(bmp: Bitmap, context: Context) {
    val downloadPath = File(Environment.getExternalStorageDirectory().path + "/Kamada_Picture")
    //別スレッド起動
    CoroutineScope(Dispatchers.Default).launch(Dispatchers.IO) {
        try {
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
            val current = sdf.format(Date())
            // 保存先のファイル作成
            val fileName = "$current.png"
            val fos = FileOutputStream(File(downloadPath, fileName))
            // ファイルに書き込み
            //PNG形式で出力
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            // 処理が終わったら、メインスレッドに切り替える。
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "画像を保存しました", Toast.LENGTH_SHORT).show()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

