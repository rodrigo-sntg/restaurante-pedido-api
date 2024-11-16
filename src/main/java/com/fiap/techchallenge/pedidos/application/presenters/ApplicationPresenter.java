package com.fiap.techchallenge.pedidos.application.presenters;

public interface ApplicationPresenter <T extends ModelView>{
	T toModelView(Object object);
}
