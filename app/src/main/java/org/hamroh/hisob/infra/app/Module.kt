package org.hamroh.hisob.infra.app

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.hamroh.hisob.data.transaction.TransactionDao
import org.hamroh.hisob.data.AppDatabase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideYourDao(appDatabase: AppDatabase): TransactionDao = appDatabase.transactionDao()
}
