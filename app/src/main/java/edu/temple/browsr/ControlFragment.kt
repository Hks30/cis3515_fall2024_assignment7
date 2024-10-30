package edu.temple.browsr

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment

class ControlFragment : Fragment() {
    private var urlEditText: EditText? = null
    private var browserInterface: BrowserInterface? = null

    interface BrowserInterface {
        fun loadUrl(url: String)
        fun goBack()
        fun goForward()
        fun canGoBack(): Boolean
        fun canGoForward(): Boolean
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        urlEditText = view.findViewById<EditText>(R.id.urlEditText).apply {
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_GO ||
                    (event?.action == KeyEvent.ACTION_DOWN &&
                            event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                    v.text?.toString()?.let { url ->
                        browserInterface?.loadUrl(url)
                        v.clearFocus()
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    true
                } else {
                    false
                }
            }
        }

        view.findViewById<ImageView>(R.id.goButton).setOnClickListener {
            urlEditText?.text?.toString()?.let { url ->
                browserInterface?.loadUrl(url)
                urlEditText?.clearFocus()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(urlEditText?.windowToken, 0)
            }
        }

        view.findViewById<ImageView>(R.id.backButton).setOnClickListener {
            if (browserInterface?.canGoBack() == true) {
                browserInterface?.goBack()
            }
        }

        view.findViewById<ImageView>(R.id.nextButton).setOnClickListener {
            if (browserInterface?.canGoForward() == true) {
                browserInterface?.goForward()
            }
        }
    }

    fun setBrowserInterface(browserInterface: BrowserInterface) {
        this.browserInterface = browserInterface
    }

    fun updateUrl(url: String) {
        urlEditText?.setText(url)
    }
}