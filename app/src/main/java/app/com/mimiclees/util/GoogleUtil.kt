package app.com.mimiclees.util

import android.content.Context
import android.os.AsyncTask
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.lang.ref.WeakReference

class GoogleUtil {

    public interface GAIDCallback{
        fun onSucces(result: String?)
        fun onFail(e:Exception)
    }

    class GAIDTask(ctx: Context, callback: GAIDCallback): AsyncTask<Void, Void, String>() {

        private var ctx: WeakReference<Context> =  WeakReference<Context>(ctx)
        private var idCallback: GAIDCallback = callback
        private lateinit var mException: Exception

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }


        override fun doInBackground(vararg params: Void?): String? {
            var idInfo: AdvertisingIdClient.Info? = null

            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(ctx.get())
            } catch (e: GooglePlayServicesNotAvailableException) {
                mException = e
            } catch (e: GooglePlayServicesRepairableException) {
                mException = e
            } catch (e: Exception) {
                mException = e
            }
            var advertId: String? = null
            try {
                advertId = idInfo!!.id
            } catch (e: Exception) {
                mException = e
            }
            return advertId
        }

        override fun onPostExecute(result: String?) {
            if (idCallback!=null && result!=null){
                idCallback.onSucces(result)
            } else{
                idCallback.onFail(mException)
            }

        }
    }


}