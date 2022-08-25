package app.com.mimiclejp.webinterface

import android.app.Activity
import android.content.Intent
import android.webkit.JavascriptInterface
import android.widget.Toast
import app.com.mimiclejp.R
import app.com.mimiclejp.common.storage.AppPreference
import app.com.mimiclejp.ui.webview.WebViewerActivity

class WebAppInterface(private val mContext: Activity) {
    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
//        web_content!!.post {
//            if (web_content!!.canGoBack() && web_content!!.url != "https://booking.pregolfshow.com/golfclubs") {
//                web_content!!.goBack()
//            } else {
//            }
//        }
    }

    //알림 설정
    @JavascriptInterface
    fun getNoti() {
        AppPreference(mContext).getNotiType()
    }

    @JavascriptInterface
    fun saveNoti(idxNoti: String) {
        AppPreference(mContext).setNotiType(idxNoti)
    }

    //회원번호
    @JavascriptInterface
    fun getMemno() {
        AppPreference(mContext).getMemNo()
    }

    @JavascriptInterface
    fun setMemno(memNo: String) {
        AppPreference(mContext).setMemNo(memNo)
        (mContext as WebViewerActivity).setPushInfo()
    }

    @JavascriptInterface
    fun goExitPop() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(mContext)
        builder.setTitle("")
        builder.setMessage(R.string.str_exit)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            mContext.finish()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
        }
        builder.show()
    }

    @JavascriptInterface
    fun exit() {
        mContext.finish()
    }

    @JavascriptInterface
    fun callSnsSheet(content: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        mContext.startActivity(shareIntent)

    }
}