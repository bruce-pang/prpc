package com.brucepang.prpc.beans.strategy;

import com.brucepang.prpc.scope.model.ScopeModelAccessor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BrucePang
 */
public class InstantiationStrategy {
    private final ScopeModelAccessor scopeModelAccessor;

    public InstantiationStrategy() {
        this(null);
    }

    public InstantiationStrategy(ScopeModelAccessor scopeModelAccessor) {
        this.scopeModelAccessor = scopeModelAccessor;
    }

    public <T> T instantiate(Class<T> type) throws ReflectiveOperationException {
        // 1.Try to get the default constructor.
        Constructor<T> defaultConstructor = null;
        try {
            defaultConstructor = type.getConstructor();
        } catch (NoSuchMethodException e) {
            // Ignore the absence of the default constructor.
        }
        // 2. Find all matching constructors.
        Constructor<?>[] constructors = type.getConstructors();
        List<Constructor<?>> matchedConstructors = new ArrayList<>();
        for (Constructor<?> constructor : constructors) {
            if (isMatched(constructor)) {
                // 3. If a matching constructor is found, add it to the list of matching constructors.
                matchedConstructors.add(constructor);
            }
        }
        // 4. Remove the default constructor from the list of matching constructors.
        if (defaultConstructor != null) {
            matchedConstructors.remove(defaultConstructor);
        }
        // 5. Select a target constructor: if there are multiple matching constructors, throw an exception; if there is only one matching constructor, select it; if there is no matching constructor but there is a default constructor, select the default constructor; if there is no matching constructor and no default constructor, throw an exception.

        Constructor<?> targetConstructor;
        if (matchedConstructors.size() > 1) {
            throw new IllegalArgumentException("Expect only one but found " +
                    matchedConstructors.size() + " matched constructors for type: " + type.getName() +
                    ", matched constructors: " + matchedConstructors);
        } else if (matchedConstructors.size() == 1) {
            targetConstructor = matchedConstructors.get(0);
        } else if (defaultConstructor != null) {
            targetConstructor = defaultConstructor;
        } else {
            throw new IllegalArgumentException("None matched constructor was found for type: " + type.getName());
        }
        Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = getArgumentValueForType(parameterTypes[i]);
        }
        return (T) targetConstructor.newInstance(args);

    }

    private Object getArgumentValueForType(Class<?> parameterType) {
        // todo: implement this method
        return null;
    }


    /**
     * Whether all parameter types of the constructor are ScopeModel classes or their subclasses.
     * @param constructor the constructor to be checked.
     * @return true if all parameter types of the constructor are ScopeModel classes or their subclasses, false otherwise.
     */
    private boolean isMatched(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            if (!ScopeModelAccessor.class.isAssignableFrom(parameterType)) {
                return false;
            }
        }
        return true;
    }
}
