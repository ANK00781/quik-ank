package dev.octoshrimpy.quik.interactor

import dev.octoshrimpy.quik.repository.ConversationRepository
import io.reactivex.Flowable
import javax.inject.Inject

class SetConversationCategory @Inject constructor(
    private val conversationRepo: ConversationRepository
) : Interactor<SetConversationCategory.Params>() {

    data class Params(val threadId: Long, val category: String)

    override fun buildObservable(params: Params): Flowable<*> =
        conversationRepo.setConversationCategory(params.threadId, params.category).toFlowable()
}
