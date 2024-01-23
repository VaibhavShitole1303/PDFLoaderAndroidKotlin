package com.example.pdfloaderdemo.activites



import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import com.example.pdfloaderdemo.common.DoubleClickListener
import com.example.pdfloaderdemo.databinding.ActivityMainBinding
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    // on below line we are creating
    // a variable for our pdf view.
    lateinit var pdfView: PDFView
    private lateinit var binding: ActivityMainBinding

    // on below line we are creating a variable for our pdf view url.
    var pdfUrl = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // on below line we are initializing
        // our pdf view with its id.
        pdfView = binding.idPDFView

        //for offline mode
        //on click of float button we are going to add pdf to our app
        initListener()
        // if you want to load from Url

        // on below line we are calling our async
        // task to load our pdf file from url.
        // we are also passing our pdf view to
        // it along with pdf view url.
//        RetrievePDFFromURL(pdfView).execute(pdfUrl)
    }

    /**
     * Set a double click listener on a view.
     *
     * @param action The action to perform on a double click.
     */
    fun View.setOnDoubleClickListener(action: (view: View) -> Unit) =
        this.setOnClickListener(DoubleClickListener(action))
    //to load from the device
    private fun initListener() {
        pdfView.setOnFocusChangeListener { view, b ->
            Log.d("pdfView",pdfView.zoom.toString())

        }
        if ( pdfView.zoom==pdfView.minZoom
        ){
            binding.floatingActionButton.visibility=View.VISIBLE
            binding.floatingActionButtonfullscreen.visibility=View.VISIBLE
            binding.top.visibility=View.VISIBLE
        }

        binding.idPDFView.setOnDoubleClickListener {
            Log.d("pdfView",pdfView.zoom.toString())

            binding.floatingActionButton.visibility=View.VISIBLE
            binding.floatingActionButtonfullscreen.visibility=View.VISIBLE
            binding.top.visibility=View.VISIBLE
        }
        binding.floatingActionButton.setOnClickListener {
            launcher.launch("application/pdf")
            binding.add.visibility= View.GONE
        }
        binding.floatingActionButtonfullscreen.setOnClickListener {
            binding.top.visibility=View.GONE
            binding.idPDFView.maxZoom
            binding.floatingActionButtonfullscreen.visibility=View.GONE
            binding.floatingActionButton.visibility=View.GONE
            // try block to hide Action bar
            // try block to hide Action bar
            try {
                this.supportActionBar!!.hide()
            } // catch block to handle NullPointerException
            catch (e: NullPointerException) {
            }
        }
    }


    private  val launcher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){uri->
      uri?.let {
          binding.idPDFView.fromUri(it).load()
          binding.top.setText(it.toString())
      }
    }

    
    // if you want to load from Url
    // on below line we are creating a class for
    // our pdf view and passing our pdf view
    // to it as a parameter.
    class RetrievePDFFromURL(pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

        // on below line we are creating a variable for our pdf view.
        val mypdfView: PDFView = pdfView

        // on below line we are calling our do in background method.
        override fun doInBackground(vararg params: String?): InputStream? {
            // on below line we are creating a variable for our input stream.
            var inputStream: InputStream? = null
            try {
                // on below line we are creating an url
                // for our url which we are passing as a string.
                val url = URL(params.get(0))

                // on below line we are creating our http url connection.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

                // on below line we are checking if the response
                // is successful with the help of response code
                // 200 response code means response is successful
                if (urlConnection.responseCode == 200) {
                    // on below line we are initializing our input stream
                    // if the response is successful.
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            }
            // on below line we are adding catch block to handle exception
            catch (e: Exception) {
                // on below line we are simply printing
                // our exception and returning null
                e.printStackTrace()
                return null;
            }
            // on below line we are returning input stream.
            return inputStream;
        }

        // on below line we are calling on post execute
        // method to load the url in our pdf view.
        override fun onPostExecute(result: InputStream?) {
            // on below line we are loading url within our
            // pdf view on below line using input stream.
            mypdfView.fromStream(result).load()

        }
    }
}
