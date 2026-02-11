package com.videoeditor.pro.core.di

import com.videoeditor.pro.data.repository.InMemoryTimelineRepository
import com.videoeditor.pro.domain.repository.TimelineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTimelineRepository(
        impl: InMemoryTimelineRepository
    ): TimelineRepository
}
