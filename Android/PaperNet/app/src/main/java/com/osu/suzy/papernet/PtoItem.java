package com.osu.suzy.papernet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by suzy on 5/16/15.
 */
public class PtoItem {
    private byte[] photo;

    private PtoItem() {
    }

    public PtoItem(byte[] photo){
        this.photo=photo;
    }
    public byte[] getPhoto() {
        return photo != null ? photo : new byte[0];
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void serialize(DataOutputStream dos) throws IOException {
        dos.writeInt(getPhoto().length);
        dos.write(getPhoto());
        dos.flush();
    }

    public static PtoItem deserialize(DataInputStream dis) throws IOException{
        PtoItem pto = new PtoItem();
        int photoLength = dis.readInt();
        if (photoLength < 0)
            photoLength = 0;
        byte[] photo = new byte[photoLength];
        dis.read(photo);
        pto.setPhoto(photo);
        return pto;
    }
}
