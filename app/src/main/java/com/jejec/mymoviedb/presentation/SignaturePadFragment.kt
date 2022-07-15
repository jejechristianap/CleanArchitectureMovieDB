package com.jejec.mymoviedb.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jejec.mymoviedb.databinding.FragmentSignaturePadBinding
import dagger.hilt.android.AndroidEntryPoint
import se.warting.signaturecore.ExperimentalSignatureApi
import se.warting.signatureview.views.SignedListener
import timber.log.Timber
import kotlin.math.sign


@AndroidEntryPoint
class SignaturePadFragment : Fragment() {

    private lateinit var bind: FragmentSignaturePadBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentSignaturePadBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        onClick()
        observe()
    }

    private fun initView() {

    }

    @ExperimentalSignatureApi
    private fun onClick() {
        with(bind) {
            signaturePad.setOnSignedListener(object : SignedListener {
                override fun onStartSigning() {

                }

                override fun onSigned() {
                    saveButton.isEnabled = true
                    clearButton.isEnabled = true
                }

                override fun onClear() {
                    saveButton.isEnabled = false
                    clearButton.isEnabled = false
                }
            })

            clearButton.setOnClickListener { signaturePad.clear() }
            saveButton.setOnClickListener {
                val signatureBitmap = signaturePad.getSignatureBitmap()
                val signatureSvg = signaturePad.getSignatureSvg()
                val transparentSignatureBitmap = signaturePad.getTransparentSignatureBitmap()
                val signature = signaturePad.getSignature()
                signaturePad
                Timber.d("Bitmap size: %s", signatureBitmap.byteCount)
                Timber.d("Bitmap trasparent size: %s", transparentSignatureBitmap.byteCount)
                Timber.e("Svg length: %s", signature)
            }
        }
    }

    private fun observe() {

    }
}