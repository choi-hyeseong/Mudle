package com.comet.mudle

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.comet.mudle.repository.user.LocalUserRepository
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.ServerUserService
import com.comet.mudle.viewmodel.RegisterViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock


class RegisterViewModelTest {

    lateinit var userService: LocalUserService

    @Before
    fun init() {
        userService = LocalUserService(Mockito.mock(ServerUserService::class.java), Mockito.mock(LocalUserRepository::class.java))
    }

    @Test
    fun testValidInput() {
        // mock 할시 method도 mock 됨
        val username = "NewUser"
        assertTrue(userService.checkValidInput(username))
    }

    @Test
    fun testInvalidInput() {
        val username = "system"
        assertFalse(userService.checkValidInput(username))
        val blank = ""
        assertFalse(userService.checkValidInput(blank))
    }


}