package nuntium.fhooe.at.nuntium.viewconversation.mvvm

import android.content.Context

class ViewConversationViewModel(val view: ViewConversationMVVM.View, val context: Context) :
    ViewConversationMVVM.ViewModel {

    private val model:ViewConversationMVVM.Model

    init {
        model = ViewConversationModel(this)
    }
}