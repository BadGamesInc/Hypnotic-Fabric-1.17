package badgamesinc.hypnotic.ui.altmanager.account;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.utils.ISerializable;
import badgamesinc.hypnotic.utils.StreamUtils;

public abstract class System<T> implements ISerializable<T> {
    private File file;

    public System(String name) {
        if (name != null) {
            this.file = new File(Hypnotic.hypnoticDir, name + ".nbt");
        }
    }

    public void init() {}

    public void save(File folder) {
        File file = getFile();
        if (file == null) return;

        NbtCompound tag = toTag();
        if (tag == null) return;

        try {
            File tempFile = File.createTempFile("meteor-client", file.getName());
            NbtIo.write(tag, tempFile);

            if (folder != null) file = new File(folder, file.getName());

            file.getParentFile().mkdirs();
            StreamUtils.copy(tempFile, file);
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        save(null);
    }

    public void load(File folder) {
        File file = getFile();
        if (file == null) return;

        try {
            if (folder != null) file = new File(folder, file.getName());

            if (file.exists()) {
                fromTag(NbtIo.read(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        load(null);
    }

    public File getFile() {
        return file;
    }

    @Override
    public NbtCompound toTag() {
        return null;
    }

    @Override
    public T fromTag(NbtCompound tag) {
        return null;
    }
}