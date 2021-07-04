package com.example.pointstestapp.presenting

import com.example.pointstestapp.network.Datastore
import com.example.pointstestapp.view.MvpView
import kotlin.reflect.KClass

abstract class BasePresenter {
    //ну тут по-хорошему должна быть очередь сообщений вьюхе итд...
    companion object BasePresenter {
        //неубиваемый пул
        private val З00presenters =
            mutableMapOf<Any, com.example.pointstestapp.presenting.BasePresenter>()

        fun bindPresenter(
            forView: MvpView, clasz: KClass<*>
        ): com.example.pointstestapp.presenting.BasePresenter {
            var presenter = З00presenters[forView.javaClass.name]
            if (presenter == null) {
                presenter = clasz.constructors.first()
                    .call() as com.example.pointstestapp.presenting.BasePresenter
                З00presenters[forView.javaClass.name] = presenter
            }
            presenter.bindView(view = forView)
            return presenter
        }
    }

    //обязательно инжектирумый стор
    protected val store = Datastore();

    //тут можно новоиспечпенную вьюху актуальной сделать
    abstract fun bindView(view: MvpView)
    abstract fun unBindView(view: MvpView)
}