package com.example.budgetapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgetapp.db.BudgetDatabase
import com.example.budgetapp.netowork.Api
import com.example.budgetapp.util.Constants.BASE_URL
import com.example.budgetapp.util.Constants.BUDGET_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBudgetDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        BudgetDatabase::class.java,
        BUDGET_DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideProfileDao(db: BudgetDatabase) = db.getProfileDao()

    @Singleton
    @Provides
    fun provideBudgetDao(db: BudgetDatabase) = db.getBudgetDao()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesAPI(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

}