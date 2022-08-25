package app.com.mimiclees.ui.dialog

import android.content.Context
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.view.View
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import app.com.mimiclees.R

/**
 * 커스텀 로딩 다이얼로그
 */
class LoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        setCanceledOnTouchOutside(true)
        window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val ivLoading = findViewById<View>(R.id.ivloading) as ImageView
        val framani = ivLoading.drawable as AnimationDrawable
        framani.start()
    }
}