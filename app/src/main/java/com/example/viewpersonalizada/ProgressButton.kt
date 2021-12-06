package com.example.viewpersonalizada

import android.content.Context
import android.opengl.Visibility
import android.text.BoringLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.viewpersonalizada.databinding.ProgressButtonBinding

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
    private var loadingTitle: String? = null

    private val binding = ProgressButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var state: ProgressButtonState = ProgressButtonState.Normal
        set(value) {
            field = value
            refreshState()
        }

    init {
        // Inicia o layout e da um refresh nos estados.
        setLayout(attrs)
        refreshState()
    }

    /**
     * Atribui o layout com os atributos disponíveis.
     */
    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            // Pega o atributo do XML attrs (styleable) e do Constraint padrão.
            val attribute = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.ProgressButton
            )
            // Adiciona o seletor como background.
            setBackgroundResource(R.drawable.progress_button_background)

            // Pega o atributo do styleable e adiciona o valor na variável "title".
            val titleResId = attribute.getResourceId(R.styleable.ProgressButton_progress_button_title, 0)
            if (titleResId != 0) {
                title = context.getString(titleResId)
            }

            // Pega o atributo do styleable e adiciona o valor na variável "loadingTitle".
            val loadingTitleResId = attribute.getResourceId(R.styleable.ProgressButton_progress_button_loading_title, 0)
            if (loadingTitleResId != 0) {
                loadingTitle = context.getString(loadingTitleResId)
            }
            // Recicla a view para atualizar.
            attribute.recycle()
        }
    }

    fun setLoading() {
        state = ProgressButtonState.Loading
    }

    fun setNormal() {
        state = ProgressButtonState.Normal
    }

    /**
     * Atualiza o estatdo do botão.
     */
    private fun refreshState() {
        // Baseado no estado é possível definir se o botão está habilitado e se é clicável.
        isEnabled = state.isEnabled
        isClickable = state.isEnabled
        // Atualiza o drawable
        refreshDrawableState()

        // Baseado no estado é possível definir se o texto está habilitado e se é clicável.
        binding.textTitle.run {
            isEnabled = state.isEnabled
            isClickable = state.isEnabled
        }
        // Baseado no estado é possível definir se o ProgressBar aparece ou não.
        binding.progressButton.visibility = state.progressVisibility

        // Dependendo do estado do botão é usado e definido a String no TextView.
        when (state) {
            ProgressButtonState.Normal -> binding.textTitle.text = title
            ProgressButtonState.Loading -> binding.textTitle.text = loadingTitle
        }
    }

    /**
     * Classe selada para criar os estado do botão.
     */
    sealed class ProgressButtonState(val isEnabled: Boolean, val progressVisibility: Int) {
        object Normal : ProgressButtonState(true, View.GONE)
        object Loading : ProgressButtonState(false, View.VISIBLE)
    }
}