TicSFTP - Android Wear SFTP server app
======================================

This project aims to create a convenient and secure file space on your android watch. It allows creating a SFTP server on the watch that user can connect to using only public key authentication. Any time a new user tries to login, a dialog on the watch will appear that user has to accept to add that user's public key to the keychain. The keychain is cleared after stopping the SFTP server.

User has to manually enable WiFi connection on the watch (perhaps something to change later).

For people who have their machines in different networks than their WiFi network, a WPA WiFi AP can be setup.

In both those cases, the SFTP server listens on `0.0.0.0` so it is possible to connect through any valid IP on the watch.

What has been tested is access via `sftp` and `sshfs`.

__!! WARNING !!__

This is a very alpha version for several reasons:

1. Uses a rather old Apache SSHD library (0.6.0)
2. All password settings for the WiFi hotspot are stored inside APK.
3. There is no mobile application for configuration.
4. It assumes you have `/sdcard/sftp` and `/sdcard/sftp/root` directories on your watch.
5. Code is not very pretty and there are potentially several pitfalls including battery drainage (not tested).
6. I haven't chosen a target license yet. For now it is LGPLv3.

Any suggestion is welcome.
