package com.kristiana.presentation;


import com.kristiana.application.impl.ServiceFactory;
import com.kristiana.domain.entities.User.Role;
import com.kristiana.presentation.forms.CategoryAddForm;
import com.kristiana.presentation.forms.CategoryDeleteForm;
import com.kristiana.presentation.forms.CategoryUpdateForm;
import com.kristiana.presentation.forms.EventAddForm;
import com.kristiana.presentation.forms.EventDeleteForm;
import com.kristiana.presentation.forms.EventUpdateForm;
import com.kristiana.presentation.forms.LocationAddForm;
import com.kristiana.presentation.forms.LocationDeleteForm;
import com.kristiana.presentation.forms.LocationUpdateForm;
import com.kristiana.presentation.pages.AuthView;
import com.kristiana.presentation.pages.CategoryView;
import com.kristiana.presentation.pages.EventView;
import com.kristiana.presentation.pages.MenuView;

public class PageFactory {

    private static PageFactory instance;
    private final ServiceFactory serviceFactory;

    public PageFactory(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public static PageFactory getInstance(ServiceFactory serviceFactory) {
        if (instance == null) {
            instance = new PageFactory(serviceFactory);
        }
        return instance;
    }

    public Renderable createAuthView() {
        return new AuthView(serviceFactory);
    }

    public Renderable createCategoryView() {
        return new CategoryView(serviceFactory);
    }

    public Renderable createEventView() {
        return new EventView(serviceFactory);
    }

    public Renderable createCategoryAddForm() {
        return new CategoryAddForm(serviceFactory);
    }

    public Renderable createCategoryUpdateForm() {
        return new CategoryUpdateForm(serviceFactory);
    }

    public Renderable createCategoryDeleteForm() {
        return new CategoryDeleteForm(serviceFactory);
    }
    public Renderable createMenuView(Role userRole) {
        return new MenuView(userRole, serviceFactory);
    }

    public Renderable createEventAddForm() {
        return new EventAddForm(serviceFactory);
    }

    public Renderable createLocationAddForm() {
        return new LocationAddForm(serviceFactory);
    }

    public Renderable createDeleteEventForm() {
        return new EventDeleteForm(serviceFactory);
    }

    public Renderable createDeleteLocationForm() {
        return new LocationDeleteForm(serviceFactory);
    }

    public Renderable createEventUpdateForm() {
        return new EventUpdateForm(serviceFactory);
    }

    public Renderable createLocationUpdateForm() {
        return new LocationUpdateForm(serviceFactory);
    }
}
