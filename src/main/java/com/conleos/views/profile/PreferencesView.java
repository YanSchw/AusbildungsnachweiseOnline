package com.conleos.views.profile;

import com.conleos.core.Session;
import com.conleos.data.entity.User;
import com.conleos.data.service.UserService;
import com.conleos.i18n.TranslationProvider;
import com.conleos.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;
import jakarta.servlet.http.Cookie;

import java.util.Locale;
import java.util.Objects;

@PageTitle("Preference")
@Route(value = "preferences", layout = MainLayout.class)
public class PreferencesView extends VerticalLayout {


    private static TranslationProvider translationProvider;
    private Locale locale = UI.getCurrent().getLocale();
    ComboBox<Locale> langBox;
    ComboBox<String> themeBox;


    public PreferencesView(UserService service, TranslationProvider translationProvider) {
        this.translationProvider = translationProvider;
        Session session = Session.getSessionFromVaadinSession(VaadinSession.getCurrent());

        add(createContent(session.getUser()));
    }


    private Component createContent(User user) {

        langBox = new ComboBox<>(translationProvider.getTranslation("view.preference.comboBox.label.language", locale));
        langBox.setItems(translationProvider.getProvidedLocales());
        langBox.setItemLabelGenerator(l -> getTranslation(l.getLanguage()));
        langBox.setValue(locale);
        langBox.addValueChangeListener(e -> {
            changeLocalPreference(e.getValue());
            UI.getCurrent().getPage().reload();
        });

        themeBox = new ComboBox<>(translationProvider.getTranslation("view.preference.comboBox.label.theme", locale));
        themeBox.setItems(new String[]{"☀\uFE0F"+translationProvider.getTranslation("view.preference.comboBox.label.brightTheme", locale), "\uD83C\uDF19"+translationProvider.getTranslation("view.preference.comboBox.label.darkTheme", locale)});
        if(UI.getCurrent().getElement().getThemeList().isEmpty()){
            UI.getCurrent().getElement().getThemeList().add("☀\uFE0F"+translationProvider.getTranslation("view.preference.comboBox.label.brightTheme", locale));
        }
        themeBox.setValue(UI.getCurrent().getElement().getThemeList().stream().toList().getFirst()) ;
        themeBox.addValueChangeListener(e -> {
            setTheme(Objects.equals(e.getValue(), "\uD83C\uDF19"+translationProvider.getTranslation("view.preference.comboBox.label.darkTheme", locale)));
        });
        return new VerticalLayout(langBox, themeBox);
    }



    /**
     * function to switch theme(darkmode on off)
     * @param dark
     */
    private void setTheme(boolean dark) {
        var js = "document.documentElement.setAttribute('theme', $0)";
        UI.getCurrent().getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);

        if(dark){
            UI.getCurrent().getElement().getThemeList().remove("☀\uFE0F");
            UI.getCurrent().getElement().getThemeList().remove(translationProvider.getTranslation("view.preference.comboBox.label.brightTheme", locale));
            UI.getCurrent().getElement().getThemeList().add("\uD83C\uDF19"+translationProvider.getTranslation("view.preference.comboBox.label.darkTheme", locale));
        }
        else {
            UI.getCurrent().getElement().getThemeList().remove("\uD83C\uDF19");
            UI.getCurrent().getElement().getThemeList().remove(translationProvider.getTranslation("view.preference.comboBox.label.darkTheme", locale));
            UI.getCurrent().getElement().getThemeList().add("☀\uFE0F"+translationProvider.getTranslation("view.preference.comboBox.label.brightTheme", locale));
        }
    }


    private void changeLocalPreference(Locale locale) {
        getUI().get().setLocale(locale);
        VaadinService.getCurrentResponse().addCookie(new Cookie("locale", locale.toLanguageTag()));
    }
}