package com.comet.mudle.repository

import com.comet.mudle.model.LocalUser
import com.comet.mudle.repository.user.LocalUserRepository
import com.comet.mudle.service.LocalUserService
import com.comet.mudle.service.ServerUserService
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

class LocalUserRepositoryTest {

    lateinit var repo : LocalUserRepository
    lateinit var service : LocalUserService
    lateinit var argumentCaptor: ArgumentCaptor<LocalUser>

    @Before
    fun init() {
        repo = Mockito.mock(LocalUserRepository::class.java)
        service = LocalUserService(Mockito.mock(ServerUserService::class.java), repo)
        argumentCaptor = ArgumentCaptor.forClass(LocalUser::class.java)
    }
    @Test
    fun testRepositoryRegister() {
        service.register("test")
        Mockito.verify(repo, Mockito.atLeast(1)).saveUser(capture(argumentCaptor))

    }

    @Test
    fun testRepositoryExist() {
        service.isUserExists()
        Mockito.verify(repo, Mockito.atLeast(1)).existUser()
    }

    @Test
    fun testRepositoryGet() {
        service.getUser()
        Mockito.verify(repo, Mockito.atLeast(1)).getUser()
    }

    // notnull하게
    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}