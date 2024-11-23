package com.ntech.theyardhub.di

import com.ntech.theyardhub.datalayer.di.UserModule
import com.ntech.theyardhub.datalayer.di.ChatModule
import com.ntech.theyardhub.datalayer.di.PostModule
import com.ntech.theyardhub.datalayer.di.ProductModule
import com.ntech.theyardhub.datalayer.implementation.repository.AuthenticationRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.ChatRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.PostRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.ProductRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.UserRepositoryImp
import com.ntech.theyardhub.feature.login.LoginViewModel
import com.ntech.theyardhub.feature.chat.ChatViewModel
import com.ntech.theyardhub.feature.detailpost.DetailPostViewModel
import com.ntech.theyardhub.feature.detailuser.DetailUserViewModel
import com.ntech.theyardhub.feature.detailyard.DetailYardViewModel
import com.ntech.theyardhub.feature.login.RegisterViewModel
import com.ntech.theyardhub.feature.main.MainActivityViewModel
import com.ntech.theyardhub.feature.post.PostViewModel
import com.ntech.theyardhub.feature.product.ProductViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    // Shared Preference
    single { SharedPreference().provideSharedPreference(androidContext()) }
    single { SharedPreference().providePreferenceManager(get()) }

    // Auth Collection
    single(named("AUTH")) { UserModule.provideAuthRef() }
    single { UserModule.provideAuthRepository(get(named("AUTH")), get()) }

    // Chat Collection
    single(named("CHAT")) { ChatModule.provideChatRef() }
    single { ChatModule.provideChatRepository(get(named("CHAT")), get()) }

    // Chat Collection
    single(named("POST")) { PostModule.providePostRef() }
    single { PostModule.providePostRepository(get(named("POST")), get()) }

    // Chat Collection
    single(named("PRODUCT")) { ProductModule.provideProductRef() }
    single { ProductModule.provideProductRepository(get(named("PRODUCT")), get()) }

    // Repository
    single { AuthenticationRepositoryImpl(get(), get()) }
    single { ChatRepositoryImpl(get(), get()) }
    single { PostRepositoryImpl(get(), get()) }
    single { ProductRepositoryImpl(get(), get()) }
    single { UserRepositoryImp(get(), get()) }

    // View Model
    factory { LoginViewModel(get()) }
    factory { ChatViewModel(get()) }
    factory { PostViewModel(get()) }
    factory { DetailPostViewModel(get()) }
    factory { DetailYardViewModel(get()) }
    factory { ProductViewModel(get()) }
    factory { RegisterViewModel(get()) }
    factory { MainActivityViewModel() }
    factory { DetailUserViewModel(get()) }


}