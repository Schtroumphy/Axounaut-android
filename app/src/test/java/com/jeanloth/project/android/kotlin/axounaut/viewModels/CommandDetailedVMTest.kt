package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class CommandDetailedVMTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel : CommandDetailedVM

    private val mockObserveCommandByIdUseCase = mock(ObserveCommandByIdUseCase::class.java)
    private val mockSaveCommandUseCase = mock(SaveCommandUseCase::class.java)
    private val mockDeleteCommandUseCase = mock(DeleteCommandUseCase::class.java)
    private val mockDeleteArticleWrapperUseCase = mock(DeleteArticleWrapperUseCase::class.java)
    private val mockSaveArticleWrapperUseCase = mock(SaveArticleWrapperUseCase::class.java)

    @Before
    fun setUp() {
        //Used for initiation of Mockk
        Dispatchers.setMain(dispatcher)
        viewModel = CommandDetailedVM(0L, mockObserveCommandByIdUseCase, mockSaveCommandUseCase, mockDeleteCommandUseCase, mockDeleteArticleWrapperUseCase, mockSaveArticleWrapperUseCase)

        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        MockKAnnotations.init(this)

    }

    /**
     * Check if we return the good new stats for the command in parameter
     */
    @Test
    fun `command with all AW in TODO should return TODO status`() {
        val command = Command(
            articleWrappers = listOf(
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.TO_DO.code
                ),
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.TO_DO.code
                ),
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.TO_DO.code
                )
            )
        )

        val result = viewModel.getCommandStatusToUpdate(command)
        println("Result : $result")

        assertThat(result).isEqualTo(CommandStatusType.TO_DO)
    }

    @Test
    fun `command with all AW with TODO and DONE or CANCELED should return IN_PROGRESS status`() {
        val command = Command(
            articleWrappers = listOf(
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.TO_DO.code
                ),
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.DONE.code
                ),
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.CANCELED.code
                )
            )
        )

        val result = viewModel.getCommandStatusToUpdate(command)
        println("Result : $result")

        assertThat(result).isEqualTo(CommandStatusType.IN_PROGRESS)
    }

    @Test
    fun `command with all AW with all DONE or CANCELED should return DONE status`() {
        val command = Command(
            articleWrappers = listOf(
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.DONE.code
                ),
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.DONE.code
                ),
                ArticleWrapper(
                    article = Article(),
                    statusCode = ArticleWrapperStatusType.CANCELED.code
                )
            )
        )

        val result = viewModel.getCommandStatusToUpdate(command)
        println("Result : $result")

        assertThat(result).isEqualTo(CommandStatusType.DONE)
    }
}