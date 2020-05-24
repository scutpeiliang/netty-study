package netty_rpc.provider;

import netty_rpc.api.SimpleService;

/**
 * 服务提供者实现了简单接口
 */
public class SimpleServiceImpl implements SimpleService {
    @Override
    public String doSth() {
        return "调用成功!";
    }
}
