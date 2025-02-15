package com.brucepang.prpc.context;

import com.brucepang.prpc.common.lifecycle.Lifecycle;
import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.extension.SPI;

@SPI(scope = ExtensionScope.MODULE)
public interface ModuleExt extends Lifecycle {}