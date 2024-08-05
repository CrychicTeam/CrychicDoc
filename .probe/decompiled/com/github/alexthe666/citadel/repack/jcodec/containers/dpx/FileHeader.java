package com.github.alexthe666.citadel.repack.jcodec.containers.dpx;

import java.util.Date;

public class FileHeader {

    public int magic;

    public int imageOffset;

    public String version;

    public int ditto;

    public String filename;

    public Date created;

    public int filesize;

    public String creator;

    public String projectName;

    public String copyright;

    public int encKey;

    public int genericHeaderLength;

    public int industryHeaderLength;

    public int userHeaderLength;
}