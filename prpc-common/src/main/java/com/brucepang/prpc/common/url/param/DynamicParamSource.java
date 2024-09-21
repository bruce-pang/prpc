package com.brucepang.prpc.common.url.param;

import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.extension.SPI;

import java.util.List;

@SPI(scope = ExtensionScope.GLOBAL)
public interface DynamicParamSource {

    void init(List<String> keys, List<ParamValue> values);
}