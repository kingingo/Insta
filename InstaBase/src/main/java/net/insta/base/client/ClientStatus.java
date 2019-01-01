package net.insta.base.client;

public enum ClientStatus {
NOT_AUTHENTICATED, // Warte auf Handshake -> HANDSHAKED
HANDSHAKED, //Handshake done... wait for LoginPacket
LOGGEDIN, //Login done ... wait for InstagrammAuthDataPacket
AUTHENTICATED; //Completely Authenticated and all function work!
}
