package com.example.testarcanitonefile.di

import androidx.lifecycle.SavedStateHandle
import com.example.testarcanitonefile.data.GitHubApiService
import com.example.testarcanitonefile.viewmodels.RepositoryContentViewModel
import com.example.testarcanitonefile.viewmodels.SearchViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideRetrofit() }
    single { provideApi(get()) }
}

val viewModelModule = module {
    viewModel { (handle: SavedStateHandle) -> RepositoryContentViewModel(handle) }
    viewModel { SearchViewModel() }
}



fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideApi(retrofit: Retrofit): GitHubApiService {
    return retrofit.create(GitHubApiService::class.java)
}