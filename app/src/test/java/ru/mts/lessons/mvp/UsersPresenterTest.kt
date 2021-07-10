package ru.mts.lessons.mvp

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import ru.mts.lessons.common.User
import ru.mts.lessons.common.UserData
import ru.mts.lessons.common.UsersModelApi

class UsersPresenterTest {

    private val view: ViewApi = mockk(relaxed = true)
    private val model: UsersModelApi = mockk()

    @Test
    fun `add user test`() {
        val presenter = UsersPresenter(model)

        every { view.getUsers() } returns UserData("111", "111")
        every { model.addUser(any(), any()) } answers { presenter.loadUsers() }
        every { model.loadUsers(any()) } answers { view.showUsers(listOf(User(1L, "111", "111"))) }

        presenter.attachView(view)
        presenter.add()

        verify { view.showUsers(listOf(User(1L, "111", "111"))) }
    }
}
