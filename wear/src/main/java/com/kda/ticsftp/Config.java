package com.kda.ticsftp;

final class Config {
    // Constant fields
    static final String TAG_SHP = "com.kda.ticftp";
    // FIXME: This shouldn't be set in preferences, add server detection state
    static final String SHP_SRV_ENABLED = "com.kda.ticftp.SrvEnabled";
    static final String SHP_AP_PASS = "com.kda.ticftp.APPass";
    // Stuff that could be configurable
    static final int port = 9988;
    static final int pubKeyTimeout =  5000;
    static final String serverRoot = "/sdcard/sftp";
    static final String serverKey = "/sdcard/sftp/key.srv";
    static final String serverKeyType = "RSA";
    static final String sftpRoot = serverRoot + "/root";
    static final String hotspotName = "TicSFTP_AP";
    static final String hotspotPassword = "12345678";
}
