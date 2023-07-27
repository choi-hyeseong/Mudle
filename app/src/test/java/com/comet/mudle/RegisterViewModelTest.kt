package com.comet.mudle

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.comet.mudle.viewmodel.RegisterViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing


class RegisterViewModelTest {

    lateinit var viewModel: RegisterViewModel
    @Mock
    lateinit var context : Context
    @Before
    fun load() {
        context = Mockito.mock(Context::class.java)
        val preferences = Mockito.mock(SharedPreferences::class.java)
        val editor = Mockito.mock(Editor::class.java)
        Mockito.`when`(editor.putString(any(), any())).thenReturn(editor)
        Mockito.`when`(context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)).thenReturn(preferences)
        Mockito.`when`(preferences.edit()).thenReturn(editor)
        DependencyUtil.injectPreference(context)
        viewModel = RegisterViewModel()

    }

    @Test
    fun testValidInput() {
        val username = "NewUser"
        assertTrue(viewModel.checkValidInput(username))
    }

    @Test
    fun testInvalidInput() {
        val username = "system"
        assertFalse(viewModel.checkValidInput(username))
        val blank = ""
        assertFalse(viewModel.checkValidInput(blank))
    }


}