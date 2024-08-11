package com.brucepang.prpc.common.beans;

import com.brucepang.prpc.beans.strategy.InstantiationStrategy;
import com.brucepang.prpc.common.beans.model.ExBeanWithGlobalModel;
import com.brucepang.prpc.scope.model.ApplicationModel;
import com.brucepang.prpc.scope.model.ScopeModel;
import com.brucepang.prpc.scope.model.ScopeModelAccessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InstantiationStrategyTest {

    private ScopeModelAccessor scopeModelAccessor = new ScopeModelAccessor() {
        @Override
        public ScopeModel getScopeModel() {
            return ApplicationModel.defaultModel().getDefaultModule();
        }
    };

    @Test
    void testCreateBeanWithScopeModelArgument() throws ReflectiveOperationException {
        InstantiationStrategy instantiationStrategy = new InstantiationStrategy(scopeModelAccessor);

        ExBeanWithGlobalModel beanWithGlobalModel = instantiationStrategy.instantiate(ExBeanWithGlobalModel.class);
        Assertions.assertSame(scopeModelAccessor.getGlobalModel(), beanWithGlobalModel.getGlobalModel());

    }


}