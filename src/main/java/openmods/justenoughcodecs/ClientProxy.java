package openmods.codecs;

import openmods.codecs.adapters.CodecFLAC;
import openmods.codecs.adapters.CodecMP3;
import paulscode.sound.ICodec;
import paulscode.sound.SoundSystemConfig;

public class ClientProxy implements IProxy {
    private static void registerCodec(Class<? extends ICodec> cls, String ext, String... mimeTypes) {
        try {
            SoundSystemConfig.setCodec(ext, cls);
            for (String type : mimeTypes)
                JustEnoughCodecs.KNOWN_MIME_TYPES.put(type, ext);
        } catch (Throwable t) {
            Log.warn(t, "Can't register codec %s for extension %s", cls.getName(), ext);
        }
    }

    private static void registerCodec(String clsName, String ext, String... mimeTypes) {
        try {
            Class<?> cls = Class.forName(clsName);
            Class<? extends ICodec> castedCls = cls.asSubclass(ICodec.class);
            registerCodec(castedCls, ext, mimeTypes);
        } catch (Throwable t) {
            Log.warn(t, "Can't register codec %s for extension %s", clsName, ext);
        }
    }

    @Override
    public void registerCodecs() {
        registerCodec(CodecMP3.class, "MP3", "audio/mpeg", "audio/x-mpeg", "audio/mpeg3", "audio/x-mpeg3");
        registerCodec(CodecFLAC.class, "FLAC", "audio/flac");

    }
}
