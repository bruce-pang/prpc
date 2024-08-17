package com.brucepang.prpc.common.beans;

import com.brucepang.prpc.beans.strategy.InstantiationStrategy;
import com.brucepang.prpc.common.beans.model.ExBeanWithApplicationModel;
import com.brucepang.prpc.common.beans.model.ExBeanWithGlobalModel;
import com.brucepang.prpc.common.beans.model.ExBeanWithModuleModel;
import com.brucepang.prpc.common.beans.model.ExBeanWithoutUniqueConstructors;
import com.brucepang.prpc.scope.model.ApplicationModel;
import com.brucepang.prpc.scope.model.ScopeModel;
import com.brucepang.prpc.scope.model.ScopeModelAccessor;
import com.brucepang.prpc.util.StrUtil;
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

        ExBeanWithApplicationModel beanWithApplicationModel = instantiationStrategy.instantiate(ExBeanWithApplicationModel.class);
        Assertions.assertSame(scopeModelAccessor.getApplicationModel(), beanWithApplicationModel.getApplicationModel());

        ExBeanWithModuleModel beanWithModuleModel = instantiationStrategy.instantiate(ExBeanWithModuleModel.class);
        Assertions.assertSame(scopeModelAccessor.getModuleModel(), beanWithModuleModel.getModuleModel());

        // test not unique matched constructors
        try {
            instantiationStrategy.instantiate(ExBeanWithoutUniqueConstructors.class);
            Assertions.fail("Expect throwing exception");
        } catch (Exception e) {
            Assertions.assertTrue(e.getMessage().contains("Expect only one but found 2 matched constructors"), StrUtil.toString(e));
        }

    }


}