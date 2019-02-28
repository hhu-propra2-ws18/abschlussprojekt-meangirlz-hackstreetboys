package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.service;

import org.mockito.ArgumentMatcher;

public class ClassOrSubclassMatcher<T> implements ArgumentMatcher<Class<T>> {

    private final Class<T> targetClass;

    public ClassOrSubclassMatcher(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public boolean matches(Class<T> obj) {
        if (obj != null) {
            if (obj instanceof Class) {
                return targetClass.isAssignableFrom( obj);
            }
        }
        return false;
    }
}

