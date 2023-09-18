package com.brucepang.prpc.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 从配置文件Address中解析出来 host 与 port;
 * @author BrucePang
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class HostAndPort {
    private String host; // 主机ip
    private int port; // 主机端口
}
