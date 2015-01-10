package net.gtaun.shoebill.amx;

import net.gtaun.shoebill.SampNativeFunction;

public class AmxInstanceImpl implements AmxInstance {
    private final int handle;

    public AmxInstanceImpl(int handle) {
        this.handle = handle;
    }

    public int getHandle() {
        return handle;
    }

    @Override
    public AmxCallable getPublic(String name) {
        int funchandle = SampNativeFunction.getPublic(handle, name);
        if (funchandle == AmxCallable.INVALID_CALLABLE)
            return null;
        return new AmxCallableImpl(this, funchandle, name, AmxCallableType.PUBLIC);
    }

    @Override
    public AmxCallable getNative(String name) {
        int funchandle = SampNativeFunction.getNative(handle, name);
        if (funchandle == AmxCallable.INVALID_CALLABLE)
            return null;
        return new AmxCallableImpl(this, funchandle, name, AmxCallableType.NATIVE);
    }

    @Override
    public void registerPublic(String name, AmxCallable callable) {
        SampNativeFunction.registerPublic(handle, name, callable);
    }

    @Override
    public void registerNative(String name, AmxCallable callable) {
        SampNativeFunction.registerNative(handle, name, callable);
    }
}
