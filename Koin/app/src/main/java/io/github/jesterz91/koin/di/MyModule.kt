package io.github.jesterz91.koin.di

import io.github.jesterz91.koin.component.HelloRepository
import io.github.jesterz91.koin.component.HelloRepositoryImpl
import io.github.jesterz91.koin.component.MySimplePresenter
import org.koin.dsl.module

// 의존성 주입을 위한 컴포넌트를 생성
val appModule = module {

    // 싱글톤 인스턴스
    single<HelloRepository> { HelloRepositoryImpl() }

    // 의존성 주입이 일어날때마다 객체를 생성
    // HelloRepositoryImpl 에 있는 데이터가 get() 함수를 통해 MySimplePresenter 로 참조
    factory { MySimplePresenter(get()) }
}