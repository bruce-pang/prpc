package com.brucepang.prpc.constants;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author BrucePang
 * 常量
 */
@Data
public class RpcConstant {

    public final static short MAGIC=0xca; //魔数【用于数据校验】

    public final static int HEAD_TOTOAL_LEN=16; //header总的字节数量
}
