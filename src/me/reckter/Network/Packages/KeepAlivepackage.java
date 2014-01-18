package me.reckter.Network.Packages;

import me.reckter.Network.Network;

/**
 * Created by reckter on 1/15/14.
 */
public class KeepAlivePackage extends BasePackage {
    /**
     * header:
     * type = 2
     *
     * as this is just a keep alive package it has no other data then the header ^^
     *
     */
    public KeepAlivePackage(Network network) {
        super(network);
        this.type = 2;
    }
}
