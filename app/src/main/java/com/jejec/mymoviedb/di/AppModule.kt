package com.jejec.mymoviedb.di

import android.content.Context
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.jejec.mymoviedb.data.data_source.local.MovieDatabase
import com.jejec.mymoviedb.data.data_source.local.MovieDatabase.Companion.DATABASE_NAME
import com.jejec.mymoviedb.data.data_source.remote.MovieApi
import com.jejec.mymoviedb.data.repository.MovieRepositoryImpl
import com.jejec.mymoviedb.domain.repository.MovieRepository
import com.jejec.mymoviedb.domain.use_case.*
import com.jejec.mymoviedb.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        MovieDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .callTimeout(40, TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .callTimeout(40, TimeUnit.SECONDS)
        .connectTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MovieApi =
        retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideMovieRepository(
        db: MovieDatabase,
        api: MovieApi
    ): MovieRepository = MovieRepositoryImpl(db.movieDao, api)

    @Provides
    @Singleton
    fun provideTestUseCase(repository: MovieRepository): MovieUseCases = MovieUseCases(
        deleteMovie = DeleteMovie(repository),
        getComingSoonMovies = GetComingSoonMovies(repository),
        getDetailMovie = GetDetailMovie(repository),
        getMovies = GetMoviesLocal(repository),
        getMoviesRemote = GetMoviesRemote(repository),
        getPopularMovies = GetPopularMovies(repository),
        insertMovie = InsertMovie(repository)
    )
}