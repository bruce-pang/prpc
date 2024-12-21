package com.brucepang.prpc.common.context;

import com.brucepang.prpc.common.lifecycle.Lifecycle;
import com.brucepang.prpc.extension.ExtensionScope;
import com.brucepang.prpc.extension.SPI;

@SPI(scope = ExtensionScope.APPLICATION)
public interface ApplicationExt extends Lifecycle {}