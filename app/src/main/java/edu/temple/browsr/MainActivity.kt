package edu.temple.browsr

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), ControlFragment.BrowserInterface, PageFragment.UrlUpdateListener {
    private var pageFragment: PageFragment? = null
    private var controlFragment: ControlFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the control fragment that was added at design time
        controlFragment = supportFragmentManager.findFragmentById(R.id.control) as? ControlFragment
        controlFragment?.setBrowserInterface(this)

        // Only create a new PageFragment if we don't have one from a configuration change
        if (savedInstanceState == null) {
            pageFragment = PageFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.page, pageFragment!!)
                .commit()
        } else {
            pageFragment = supportFragmentManager.findFragmentById(R.id.page) as? PageFragment
        }

        pageFragment?.setUrlUpdateListener(this)
    }

    // BrowserInterface implementation
    override fun loadUrl(url: String) {
        pageFragment?.loadUrl(url)
    }

    override fun goBack() {
        pageFragment?.goBack()
    }

    override fun goForward() {
        pageFragment?.goForward()
    }

    override fun canGoBack(): Boolean = pageFragment?.canGoBack() == true

    override fun canGoForward(): Boolean = pageFragment?.canGoForward() == true

    // UrlUpdateListener implementation
    override fun onUrlUpdate(url: String) {
        controlFragment?.updateUrl(url)
    }
}